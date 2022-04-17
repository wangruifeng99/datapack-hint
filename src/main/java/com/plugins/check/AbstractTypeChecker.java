package com.plugins.check;

import com.plugins.util.FieldTypeUtils;

public abstract class AbstractTypeChecker {
    protected String type;

    /**
     * 校验类型是否匹配
     */
    public boolean valid(String targetType) {
        if(!FieldTypeUtils.finished) {
            return true;
        }
        return type.equalsIgnoreCase(targetType);
    }

    public String getType() {
        return type;
    }
}
