package com.fengk.constants;
/**
 * Used to print the Constants class information by java reflection
 * @author fengk
 */

import java.lang.reflect.Field;

public class ConstantsPrinter {
    
    private String defaultClassName = "com.fengk.constants.Constants";
    
    public String getDefaultClassName() {
        return defaultClassName;
    }
    
    public static void printClassField(String className) {
        if (className == null || className.isEmpty()) {
            className = new ConstantsPrinter().getDefaultClassName();
        }
        
        System.out.println("--------------------------------------");
        Class<?> classInfo;
        try {
            classInfo = Class.forName(className);
            Field[] fieldsInfo = classInfo.getFields();
            for (Field field : fieldsInfo) {
                try {
                    System.out.println(field.getName() + "=" + field.get(field));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("--------------------------------------");
    }
}
