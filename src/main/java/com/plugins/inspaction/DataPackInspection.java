package com.plugins.inspaction;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl;
import com.intellij.util.IncorrectOperationException;
import com.plugins.check.AbstractTypeChecker;
import com.plugins.factory.TypeCheckerFactory;
import com.plugins.util.FieldTypeUtils;
import org.jetbrains.annotations.NotNull;

public class DataPackInspection extends AbstractBaseJavaLocalInspectionTool {

    private static final Logger LOG = Logger.getInstance("#com.plugins.datapack.DataPackInspection");

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {

        return new JavaElementVisitor() {
            @Override
            public void visitField(PsiField field) {
            }

            @Override
            public void visitFile(@NotNull PsiFile file) {
            }

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                // 发生异常后不影响插件正常运行
                try {
                    validate(expression);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }

            private void validate(PsiMethodCallExpression expression) {
                PsiReferenceExpression methodExpression = expression.getMethodExpression();
                PsiExpression qualifierExpression = methodExpression.getQualifierExpression();
                if(qualifierExpression == null) {
                    return;
                }
                PsiType type = qualifierExpression.getType();
                if(type == null) {
                    return;
                }
                // 字段名，如dataPack
                String objFieldName = qualifierExpression.getText();
                if(objFieldName == null) {
                    return;
                }
                // 类型
                String canonicalText = type.getCanonicalText();
                if(!"com.stock.framework.communication.DataPack".equals(canonicalText)) {
                    return;
                }
                // 方法名 如：getStringValue
                String methodName = methodExpression.getReferenceName();
                if(methodName == null) {
                    return;
                }
                // 如果不是set...Value或get...Value，不校验
                if(!methodName.endsWith("Value")) {
                    return;
                }
                // 所有参数
                PsiExpression[] expressions = expression.getArgumentList().getExpressions();
                // 需要校验的入参字段在第2个
                if(expressions.length < 2) {
                    return;
                }
                PsiExpression field = expressions[1];
                if(field == null) {
                    return;
                }
                String value = field.getText();
                if(value == null) {
                    return;
                }
                String fieldName = value.replaceAll("\"", "");
                String fieldType = FieldTypeUtils.getFieldType(fieldName);
                TypeCheckerFactory typeCheckerFactory = new TypeCheckerFactory();
                AbstractTypeChecker checker = typeCheckerFactory.getChecker(methodName);
                if(checker == null) {
                    return;
                }
                String realType;
                if(methodName.startsWith("get")) {
                    realType = typeCheckerFactory.getRealDataPackGetType(fieldType);
                } else {
                    realType = typeCheckerFactory.getRealDataPackSetType(fieldType);
                }
                boolean valid = checker.valid(fieldType);

                if(fieldType == null) {
                    holder.registerProblem(expression, "Field " + fieldName + " is not defined in dict.txt");
                    return;
                }
                if(!valid) {
                    CriQuickFix myQuickFix = new CriQuickFix("Change to " + realType, objFieldName, realType);
                    holder.registerProblem(expression, "Should be " + objFieldName + "." + realType, myQuickFix);
                }
            }

        };
    }

    private static class CriQuickFix implements LocalQuickFix {
        private final String name;
        private final String fieldName;
        private final String realMethodName;
        public CriQuickFix(String name, String fieldName, String methodName) {
            this.name = name;
            this.fieldName = fieldName;
            this.realMethodName = methodName;
        }

        @NotNull
        @Override
        public String getName() {
            return name;
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            try {
                PsiElement element = descriptor.getPsiElement();
                if(!(element instanceof PsiMethodCallExpressionImpl)) {
                    return;
                }
                PsiMethodCallExpression expression = (PsiMethodCallExpressionImpl) descriptor.getPsiElement();
                PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                PsiExpression newExpression = factory.createExpressionFromText(fieldName + "." + this.realMethodName, null);
                expression.getMethodExpression().replace(newExpression);

            } catch (IncorrectOperationException e) {
                e.printStackTrace();
                LOG.error(e);
            }
        }

        @NotNull
        @Override
        public String getFamilyName() {
            return getName();
        }

    }
}
