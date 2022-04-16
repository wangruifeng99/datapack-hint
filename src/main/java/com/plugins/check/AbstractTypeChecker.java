package com.plugins.check;

public abstract class AbstractTypeChecker {
    protected String type;

    /**
     * У�������Ƿ�ƥ��
     */
    public boolean valid(String targetType) {
        return type.equalsIgnoreCase(targetType);
    }

    public String getType() {
        return type;
    }
}
