package com.amirnadiv.project.utils.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.amirnadiv.project.utils.common.exception.ClassInstantiationException;
import com.amirnadiv.project.utils.common.exception.ServiceNotFoundException;
import com.amirnadiv.project.utils.common.io.StreamUtil;


public abstract class ClassLoaderUtil {


    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    public static String getClasspath() {
        return ClassLoaderUtil.getResource("").getPath();
    }


    public static <T> Class<T> loadClass(String className) throws ClassNotFoundException {
        return loadClass(className, getContextClassLoader());
    }


    public static <T> Class<T> loadClass(String className, Class<?> referrer) throws ClassNotFoundException {
        ClassLoader classLoader = getReferrerClassLoader(referrer);


        return loadClass(className, classLoader);
    }


    public static <T> Class<T> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        if (StringUtil.isBlank(className)) {
            return null;
        }

        if (classLoader == null) {
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) Class.forName(className);
            return clazz;
        }
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) Class.forName(className, true, classLoader);
        return clazz;
    }


    public static <T> Class<T> loadServiceClass(String serviceId) throws ClassNotFoundException {
        return loadServiceClass(serviceId, getContextClassLoader());
    }


    public static <T> Class<T> loadServiceClass(String serviceId, Class<?> referrer) throws ClassNotFoundException {
        ClassLoader classLoader = getReferrerClassLoader(referrer);

        return loadServiceClass(serviceId, classLoader);
    }


    public static <T> Class<T> loadServiceClass(String serviceId, ClassLoader classLoader)
            throws ClassNotFoundException {
        if (StringUtil.isBlank(serviceId)) {
            return null;
        }

        serviceId = "META-INF/services/" + serviceId;

        InputStream istream = getResourceAsStream(serviceId, classLoader);

        if (istream == null) {
            throw new ServiceNotFoundException("Could not find " + serviceId);
        }

        String serviceClassName;

        try {
            serviceClassName = StringUtil.trimToEmpty(StreamUtil.readText(istream, "UTF-8", true));
        } catch (IOException e) {
            throw new ServiceNotFoundException("Failed to load " + serviceId, e);
        }

        return ClassLoaderUtil.loadClass(serviceClassName, classLoader);
    }


    public static <T> Class<T> loadServiceClass(String className, String serviceId) throws ClassNotFoundException {
        return loadServiceClass(className, serviceId, getContextClassLoader());
    }


    public static <T> Class<T> loadServiceClass(String className, String serviceId, Class<T> referrer)
            throws ClassNotFoundException {
        ClassLoader classLoader = getReferrerClassLoader(referrer);
        return loadServiceClass(className, serviceId, classLoader);
    }


    public static <T> Class<T> loadServiceClass(String className, String serviceId, ClassLoader classLoader)
            throws ClassNotFoundException {
        try {
            if (StringUtil.isNotBlank(className)) {
                return loadClass(className, classLoader);
            }
        } catch (ClassNotFoundException ignore) {

        }


        return loadServiceClass(serviceId, classLoader);
    }


    private static <T> ClassLoader getReferrerClassLoader(Class<T> referrer) {
        if (referrer == null) {
            return null;
        }

        ClassLoader classLoader = referrer.getClassLoader();


        return (classLoader == null) ? ClassLoader.getSystemClassLoader() : classLoader;

    }

    public static <T> T newInstance(String className) throws ClassNotFoundException, ClassInstantiationException {
        Class<T> clazz = loadClass(className);

        return newInstance(clazz);
    }

    public static Object newInstance(String className, Class<?> referrer) throws ClassNotFoundException,
            ClassInstantiationException {
        return newInstance(loadClass(className, referrer));
    }

    public static Object newInstance(String className, ClassLoader classLoader) throws ClassNotFoundException,
            ClassInstantiationException {
        return newInstance(loadClass(className, classLoader));
    }

    public static <T> T newInstance(Class<T> clazz) throws ClassInstantiationException {
        if (clazz == null) {
            return null;
        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new ClassInstantiationException(clazz, "Failed to instantiate class: " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            throw new ClassInstantiationException(clazz, "Failed to instantiate class: " + clazz.getName(), e);
        } catch (Exception e) {
            throw new ClassInstantiationException(clazz, "Failed to instantiate class: " + clazz.getName(), e);
        }
    }

    public static <T> T newServiceInstance(String serviceId) throws ClassNotFoundException, ClassInstantiationException {
        Class<T> clazz = loadServiceClass(serviceId);

        return newInstance(clazz);
    }

    public static <T> T newServiceInstance(String serviceId, Class<T> referrer) throws ClassNotFoundException,
            ClassInstantiationException {
        Class<T> clazz = loadServiceClass(serviceId, referrer);

        return newInstance(clazz);
    }

    public static <T> T newServiceInstance(String serviceId, ClassLoader classLoader) throws ClassNotFoundException,
            ClassInstantiationException {
        Class<T> clazz = loadServiceClass(serviceId, classLoader);

        return newInstance(clazz);
    }

    public static <T> T newServiceInstance(String className, String serviceId) throws ClassNotFoundException,
            ClassInstantiationException {
        Class<T> clazz = loadServiceClass(className, serviceId);

        return newInstance(clazz);
    }

    public static <T> T newServiceInstance(String className, String serviceId, Class<T> referrer)
            throws ClassNotFoundException, ClassInstantiationException {
        Class<T> clazz = loadServiceClass(className, serviceId, referrer);

        return newInstance(clazz);
    }

    public static <T> T newServiceInstance(String className, String serviceId, ClassLoader classLoader)
            throws ClassNotFoundException, ClassInstantiationException {
        Class<T> clazz = loadServiceClass(className, serviceId, classLoader);

        return newInstance(clazz);
    }

    public static URL[] getResources(String resourceName) {
        List<URL> urls = CollectionUtil.createLinkedList();
        boolean found = false;


        found = getResources(urls, resourceName, getContextClassLoader(), false);

        if (!found) {
            getResources(urls, resourceName, ClassLoaderUtil.class.getClassLoader(), false);
        }

        if (!found) {
            getResources(urls, resourceName, null, true);
        }


        return getDistinctURLs(urls);
    }

    public static URL[] getResources(String resourceName, Class<?> referrer) {
        ClassLoader classLoader = getReferrerClassLoader(referrer);
        List<URL> urls = CollectionUtil.createLinkedList();

        getResources(urls, resourceName, classLoader, classLoader == null);


        return getDistinctURLs(urls);
    }

    public static URL[] getResources(String resourceName, ClassLoader classLoader) {
        List<URL> urls = CollectionUtil.createLinkedList();

        getResources(urls, resourceName, classLoader, classLoader == null);

        return getDistinctURLs(urls);
    }

    private static boolean getResources(List<URL> urlSet, String resourceName, ClassLoader classLoader,
            boolean sysClassLoader) {
        if (StringUtil.isBlank(resourceName)) {
            return false;
        }

        Enumeration<URL> enums = null;

        try {
            if (classLoader != null) {
                enums = classLoader.getResources(resourceName);
            } else if (sysClassLoader) {
                enums = ClassLoader.getSystemResources(resourceName);
            }
        } catch (IOException ignore) {

        }

        if (CollectionUtil.hasNotItems(enums)) {
            return false;
        }

        while (enums.hasMoreElements()) {
            urlSet.add(enums.nextElement());
        }

        return true;
    }

    private static URL[] getDistinctURLs(List<URL> urls) {
        if (urls == null || urls.size() == 0) {
            return new URL[0];
        }

        Set<URL> urlSet = CollectionUtil.createHashSet();

        for (Iterator<URL> i = urls.iterator(); i.hasNext();) {
            URL url = i.next();

            if (urlSet.contains(url)) {
                i.remove();
            } else {
                urlSet.add(url);
            }
        }

        return urls.toArray(new URL[urls.size()]);
    }

    public static URL getResource(String resourceName) {
        if (resourceName == null) {
            return null;
        }

        ClassLoader classLoader = null;
        URL url = null;


        classLoader = getContextClassLoader();

        if (classLoader != null) {
            url = classLoader.getResource(resourceName);

            if (url != null) {
                return url;
            }
        }


        classLoader = ClassLoaderUtil.class.getClassLoader();

        if (classLoader != null) {
            url = classLoader.getResource(resourceName);

            if (url != null) {
                return url;
            }
        }

        return ClassLoader.getSystemResource(resourceName);
    }

    public static URL getResource(String resourceName, Class<?> referrer) {
        if (resourceName == null) {
            return null;
        }

        ClassLoader classLoader = getReferrerClassLoader(referrer);

        return (classLoader == null) ? ClassLoaderUtil.class.getClassLoader().getResource(resourceName) : classLoader
                .getResource(resourceName);
    }

    public static URL getResource(String resourceName, ClassLoader classLoader) {
        if (StringUtil.isBlank(resourceName)) {
            return null;
        }

        return (classLoader == null) ? ClassLoaderUtil.class.getClassLoader().getResource(resourceName) : classLoader
                .getResource(resourceName);
    }

    public static InputStream getResourceAsStream(String resourceName) {
        URL url = getResource(resourceName);

        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException ignore) {

        }

        return null;
    }

    public static InputStream getResourceAsStream(String resourceName, Class<?> referrer) {
        URL url = getResource(resourceName, referrer);

        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException ignore) {

        }

        return null;
    }

    public static InputStream getResourceAsStream(String resourceName, ClassLoader classLoader) {
        URL url = getResource(resourceName, classLoader);

        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException ignore) {

        }

        return null;
    }

    public static URL[] whichClasses(String className) {
        return getResources(ClassUtil.getClassNameAsResource(className));
    }

    public static <T> URL[] whichClasses(String className, Class<T> referrer) {
        return getResources(ClassUtil.getClassNameAsResource(className), referrer);
    }

    public static URL[] whichClasses(String className, ClassLoader classLoader) {
        return getResources(ClassUtil.getClassNameAsResource(className), classLoader);
    }

    public static URL whichClass(String className) {
        return getResource(ClassUtil.getClassNameAsResource(className));
    }

    public static <T> URL whichClass(String className, Class<T> referrer) {
        return getResource(ClassUtil.getClassNameAsResource(className), referrer);
    }

    public static URL whichClass(String className, ClassLoader classLoader) {
        return getResource(ClassUtil.getClassNameAsResource(className), classLoader);
    }
}
