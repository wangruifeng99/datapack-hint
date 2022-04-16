package com.plugins.check;

public abstract class AbstractTypeChecker {
    protected String type;

    /**
     * 校验类型是否匹配
     */
    public boolean valid(String targetType) {
        return type.equalsIgnoreCase(targetType);
    }

    public String getType() {
        return type;
    }
}
