package com.plugins.factory;

import com.plugins.check.AbstractTypeChecker;
import com.plugins.check.checker.DoubleTypeChecker;
import com.plugins.check.checker.IntTypeChecker;
import com.plugins.check.checker.LongTypeChecker;
import com.plugins.check.checker.StringTypeChecker;

public class TypeCheckerFactory {
    private static final String Method_GetIntValue = "getIntValue";
    private static final String Method_GetLongValue = "getLongValue";
    private static final String Method_GetDoubleValue = "getDoubleValue";
    private static final String Method_GetStringValue = "getStringValue";

    private static final String Method_SetIntValue = "setIntValue";
    private static final String Method_SetLongValue = "setLongValue";
    private static final String Method_SetDoubleValue = "setDoubleValue";
    private static final String Method_SetStringValue = "setStringValue";

    public AbstractTypeChecker getChecker(String method) {
        AbstractTypeChecker typeChecker;
        switch (method) {
            case Method_GetIntValue:
            case Method_SetIntValue:
                typeChecker = new IntTypeChecker();
                break;
            case Method_GetLongValue:
            case Method_SetLongValue:
                typeChecker = new LongTypeChecker();
                break;
            case Method_GetDoubleValue:
            case Method_SetDoubleValue:
                typeChecker = new DoubleTypeChecker();
                break;
            case Method_GetStringValue:
            case Method_SetStringValue:
                typeChecker = new StringTypeChecker();
                break;
            default:
                typeChecker = null;
                break;
        }
        return typeChecker;
    }

    public String getRealDataPackGetType(String type) {
        String dataPackType;
        if(type == null) {
            return null;
        }
        type = type.toLowerCase();
        switch (type) {
            case "int":
                dataPackType = "getIntValue";
                break;
            case "long":
                dataPackType = "getLongValue";
                break;
            case "double":
                dataPackType = "getDoubleValue";
                break;
            case "string":
                dataPackType = "getStringValue";
                break;
            default:
                dataPackType = null;
                break;
        }
        return dataPackType;
    }

    public String getRealDataPackSetType(String type) {
        String dataPackType;
        if(type == null) {
            return null;
        }
        type = type.toLowerCase();
        switch (type) {
            case "int":
                dataPackType = "setIntValue";
                break;
            case "long":
                dataPackType = "setLongValue";
                break;
            case "double":
                dataPackType = "setDoubleValue";
                break;
            case "string":
                dataPackType = "setStringValue";
                break;
            default:
                dataPackType = null;
                break;
        }
        return dataPackType;
    }
}
