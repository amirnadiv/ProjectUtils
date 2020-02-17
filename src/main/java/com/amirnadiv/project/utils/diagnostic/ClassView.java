package com.amirnadiv.project.utils.common.diagnostic;

import com.amirnadiv.project.utils.common.ClassLoaderUtil;
import com.amirnadiv.project.utils.common.ClassUtil;
import com.amirnadiv.project.utils.common.ObjectUtil;

public class ClassView {
    public static String toString(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return ClassUtil.getFriendlyClassName(clazz);
        }
        if (clazz.isArray()) {
            return "Array " + ClassUtil.getFriendlyClassName(clazz) + "\n"
                    + toString(ClassUtil.getArrayComponentType(clazz));
        }
        if (clazz.isInterface()) {
            return toInterfaceString(clazz, "");
        }

        return toClassString(clazz, "");
    }

    private static String toInterfaceString(Class<?> clazz, String indent) {
        StringBuilder builder = new StringBuilder();

        builder.append(indent).append("Interface ").append(clazz.getName()).append("  (").append(toClassString(clazz))
                .append(')');

        Class<?>[] interfaceClass = clazz.getInterfaces();

        for (int i = 0, c = interfaceClass.length; i < c; ++i) {
            clazz = interfaceClass[i];

            builder.append('\n').append(toInterfaceString(clazz, indent + "  "));
        }

        return builder.toString();
    }

    private static String toClassString(Class<?> clazz, String indent) {
        StringBuilder builder = new StringBuilder();

        builder.append(indent).append("Class ").append(clazz.getName()).append("  (").append(toClassString(clazz))
                .append(')');

        indent += "  ";

        Class<?>[] interfaceClass = clazz.getInterfaces();

        for (int i = 0, c = interfaceClass.length; i < c; ++i) {
            builder.append('\n').append(toInterfaceString(interfaceClass[i], indent));
        }

        clazz = clazz.getSuperclass();

        if (clazz != null) {
            builder.append('\n').append(toClassString(clazz, indent));
        }

        return builder.toString();
    }

    private static String toClassString(Class<?> clz) {
        ClassLoader loader = clz.getClassLoader();

        return "loaded by " + ObjectUtil.identityToString(loader, "System ClassLoader") + ", "
                + ClassLoaderUtil.whichClass(clz.getName());
    }

    public static void main(String[] args) throws ClassNotFoundException {
        if (args.length == 0) {
            System.out.println("\nUsage:");
            System.out.println("    java " + ClassView.class.getName() + " MyClass");
            System.out.println("    java " + ClassView.class.getName() + " my.package.MyClass");
            System.out.println("    java " + ClassView.class.getName() + " META-INF/MANIFEST.MF");
            System.exit(-1);
        }

        for (int i = 0; i < args.length; i++) {
            System.out.println(toString(ClassLoaderUtil.loadClass(args[i])));
        }
    }
}
