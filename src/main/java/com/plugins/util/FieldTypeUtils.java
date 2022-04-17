package com.plugins.util;

import com.plugins.bean.DataPackField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FieldTypeUtils {

    private static final Map<String, DataPackField> typeDic = new HashMap<>();
    private static final Map<String, String> aliasDic;
    public static boolean finished;

    static {
        aliasDic = new HashMap<>();
        aliasDic.put("date", "String");
        try {
            readFile();
            finished = true;
        } catch (IOException e) {
            finished = false;
            e.printStackTrace();
        }
    }

    public static String getFieldType(String fieldName) {
        if(fieldName == null) {
            return null;
        }
        DataPackField field = typeDic.get(fieldName.toLowerCase());
        if(field != null) {
            String fieldType = field.getType();
            String realType = aliasDic.get(fieldType);
            if(realType != null) {
                return realType;
            }
            return field.getType();
        }
        return null;
    }

    private static void readFile() throws IOException {
        InputStream is = FieldTypeUtils.class.getClassLoader().getResourceAsStream("dict.txt");
        if(is == null) {
            System.out.println("dict.txt not found");
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = reader.readLine()) != null) {
            String[] infos = line.split("\\s+");
            if(infos.length >= 4) {
                DataPackField field = new DataPackField();
                field.setId(infos[0]);
                String fieldName = infos[1];
                String fieldType = infos[2];
                String len = infos[3];
                String additionalType;
                if("double".equalsIgnoreCase(fieldType) && !len.contains(",")) {
                    fieldType = "long";
                    additionalType = "double";
                } else {
                    additionalType = aliasDic.get(fieldType);
                }
                field.setName(fieldName);
                field.setType(fieldType);
                field.setAdditionalType(additionalType);
                typeDic.put(fieldName.toLowerCase(), field);
            }
        }
    }
}
