package com.amirnadiv.project.utils.common;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public abstract class PackageUtil {

    private static final List<String> EMPTY_LIST = Collections.emptyList();

    public static List<String> getResourceInPackage(String packageName) throws IOException {
        if (StringUtil.isBlank(packageName)) {
            return null;
        }

        boolean recursive = packageName.endsWith(".*");
        String packagePath = getPackagePath(packageName);
        List<String> resources = CollectionUtil.createArrayList();
        String packageDirName = packagePath.replace('.', '/');

        URL[] dirs = ClassLoaderUtil.getResources(packageDirName);
        for (URL url : dirs) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                findResourceInDirPackage(packagePath, URLDecoder.decode(url.getFile(), "UTF-8"), resources);
            } else if ("jar".equals(protocol)) {
                findResourceInJarPackage(url, packageName, packageDirName, recursive, resources);
            }

        }
        return resources;
    }

    public static List<String> getClassesInPackage(String packageName) throws IOException {
        return getClassesInPackage(packageName, null, null);
    }

    public static List<String> getClassesInPackage(String packageName, List<String> included, List<String> excluded)
            throws IOException {
        if (StringUtil.isBlank(packageName)) {
            return null;
        }

        boolean recursive = packageName.endsWith(".*");
        String packagePath = getPackagePath(packageName);
        List<String> classes = CollectionUtil.createArrayList();
        String packageDirName = packagePath.replace('.', '/');

        URL[] dirs = ClassLoaderUtil.getResources(packageDirName);
        for (URL url : dirs) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                findClassesInDirPackage(packagePath, included, excluded, URLDecoder.decode(url.getFile(), "UTF-8"),
                        recursive, classes);
            } else if ("jar".equals(protocol)) {
                findClassesInJarPackage(url, packageName, included, excluded, packageDirName, recursive, classes);
            }
        }
        return classes;
    }

    private static void findClassesInJarPackage(URL url, String packageName, List<String> included,
            List<String> excluded, String packageDirName, final boolean recursive, List<String> classes)
            throws IOException {
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }

            if (name.startsWith(packageDirName)) {
                int idx = name.lastIndexOf('/');
                if (idx != -1) {
                    packageName = name.substring(0, idx).replace('/', '.');
                }

                if ((idx != -1) || recursive) {
                    // it's not inside a deeper dir
                    if (name.endsWith(".class") && !entry.isDirectory()) {
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        filterClass(packageName, className, included, excluded, classes);
                    }
                }
            }
        }
    }

    private static void findClassesInDirPackage(String packageName, List<String> included, List<String> excluded,
            String packagePath, final boolean recursive, List<String> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });

        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findClassesInDirPackage(packageName + "." + file.getName(), included, excluded, file.getAbsolutePath(),
                        recursive, classes);
                continue;
            }

            filterClass(packageName, file.getName().substring(0, file.getName().length() - 6), included, excluded,
                    classes);

        }
    }

    private static void filterClass(String packageName, String className, List<String> included, List<String> excluded,
            List<String> classes) {
        if (isIncluded(className, included, excluded)) {
            classes.add(packageName + '.' + className);
        }
    }

    private static void findResourceInJarPackage(URL url, String packageName, String packageDirName, boolean recursive,
            List<String> resources) throws IOException {
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }

            if (name.startsWith(packageDirName)) {
                int idx = name.lastIndexOf('/');
                if (idx != -1) {
                    packageName = name.substring(0, idx).replace('/', '.');
                }

                if ((idx != -1) || recursive) {
                    // it's not inside a deeper dir
                    if (!entry.isDirectory()) {
                        resources.add(packageName + "." + name.substring(packageName.length() + 1));
                    }

                }
            }
        }
    }

    private static void findResourceInDirPackage(String packageName, String packagePath, List<String> resources) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] dirfiles = dir.listFiles();
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findResourceInDirPackage(packageName + "." + file.getName(), file.getAbsolutePath(), resources);
                continue;
            }
            resources.add(packageName + "." + file.getName());
        }
    }

    private static String getPackagePath(String packageName) {
        if (packageName.endsWith(".*")) {
            packageName = packageName.substring(0, packageName.lastIndexOf(".*"));
        }

        if (packageName.endsWith("/")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        return packageName;
    }

    private static boolean isIncluded(String name, List<String> included, List<String> excluded) {
        if (CollectionUtil.isEmpty(included) && CollectionUtil.isEmpty(excluded)) {
            return true;
        }

        included = (null == included) ? EMPTY_LIST : included;
        excluded = (null == excluded) ? EMPTY_LIST : excluded;
        boolean isIncluded = PackageUtil.isMatched(name, included);
        boolean isExcluded = PackageUtil.isMatched(name, excluded);

        if (isIncluded && !isExcluded) {
            return true;
        }

        if (isExcluded) {
            return false;
        }

        return included.size() == 0;
    }

    private static boolean isMatched(String name, List<String> list) {
        for (String regexpStr : list) {
            if (Pattern.matches(regexpStr, name)) {
                return true;
            }

        }
        return false;
    }

    public static Package getPackage(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return clazz.getPackage();
    }

    public static String getPackageNameForObject(Object object) {
        if (object == null) {
            return Emptys.EMPTY_STRING;
        }

        return getPackageName(object.getClass().getName());
    }

    public static String getPackageName(Class<?> clazz) {
        if (clazz == null) {
            return Emptys.EMPTY_STRING;
        }

        return getPackageName(clazz.getName());
    }

    public static String getPackageName(String javaClassName) {
        String friendlyClassName = ClassUtil.toFriendlyClassName(javaClassName, false, null);

        if (friendlyClassName == null) {
            return Emptys.EMPTY_STRING;
        }

        int i = friendlyClassName.lastIndexOf('.');

        if (i == -1) {
            return Emptys.EMPTY_STRING;
        }

        return friendlyClassName.substring(0, i);
    }

    // ==========================================================================
    // 取得类名和package名的resource名的方法。
    //
    // 和类名、package名不同的是，resource名符合文件名命名规范，例如：
    // java/lang/String.class
    // com/baidu/unbiz/common
    // etc.
    // ==========================================================================

    public static String getResourceNameForObjectClass(Object object) {
        if (object == null) {
            return null;
        }

        return object.getClass().getName().replace('.', '/') + ".class";
    }

    public static String getResourceNameForClass(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return clazz.getName().replace('.', '/') + ".class";
    }

    public static String getResourceNameForClass(String className) {
        if (className == null) {
            return null;
        }

        return className.replace('.', '/') + ".class";
    }

    public static String getResourceNameForObjectPackage(Object object) {
        if (object == null) {
            return null;
        }

        return getPackageNameForObject(object).replace('.', '/');
    }

    public static String getResourceNameForPackage(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return getPackageName(clazz).replace('.', '/');
    }

    public static String getResourceNameForPackage(String className) {
        if (className == null) {
            return null;
        }

        return getPackageName(className).replace('.', '/');
    }

}
