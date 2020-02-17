
package com.amirnadiv.project.utils.common;

import java.security.ProtectionDomain;

public abstract class RuntimeUtil {

    public static String currentClassMethod() {
        StackTraceElement[] ste = new Exception().getStackTrace();
        int ndx = (ste.length > 1) ? 1 : 0;
        StackTraceElement element = new Exception().getStackTrace()[ndx];

        return element.getClassName() + "." + element.getMethodName();
    }

    public static String currentMethodName() {
        StackTraceElement[] ste = new Exception().getStackTrace();
        int ndx = (ste.length > 1) ? 1 : 0;
        return new Exception().getStackTrace()[ndx].getMethodName();
    }

    public static String currentClassName() {
        StackTraceElement[] ste = new Exception().getStackTrace();
        int ndx = (ste.length > 1) ? 1 : 0;
        return new Exception().getStackTrace()[ndx].getClassName();
    }

    public static String currentNamespace() {
        StackTraceElement[] ste = new Exception().getStackTrace();
        int ndx = (ste.length > 1) ? 1 : 0;
        StackTraceElement current = new Exception().getStackTrace()[ndx];
        return current.getClassName() + "." + current.getMethodName();
    }

    public static String classLocation(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        ProtectionDomain domain = clazz.getProtectionDomain();

        return (domain.getClassLoader() != null) ? domain.getCodeSource().getLocation().getPath() : clazz.getName();
    }

    public static String classLocation() {
        return classLocation(RuntimeUtil.class);
    }

}
