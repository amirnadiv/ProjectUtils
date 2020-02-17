package com.amirnadiv.project.utils.common.file;

import java.io.IOException;
import java.util.List;

import com.amirnadiv.project.utils.common.collection.ListMap;
import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.io.ByteArray;

public interface FileProcessor {

    void importBean(Object bean, String filePath) throws IOException;

    void importBeans(Class<?> clazz, Object[] beans, String filePath) throws IOException;

    void importBeans(String[] contents, String filePath) throws IOException;

    void importBeans(List<String> contents, String filePath) throws IOException;

    // FIXME
    void importBeans(String[] headers, Class<?> clazz, Object[] beans, String filePath) throws IOException;

    void importBeans(ListMap<String, String> headers, Class<?> clazz, Object[] beans, String filePath)
            throws IOException;

    List<String> collectData(FileBeanInfo beanInfo) throws IOException;

    void importBeans(FileBeanInfo beanInfo, String filePath) throws IOException;

    <T> T exportBean(Class<T> clazz, String filePath) throws IOException;

    <T> List<T> exportBeans(Class<T> clazz, String filePath) throws IOException;

    ByteArray toByteArray(FileBeanInfo beanInfo) throws IOException;

    ByteArray toByteArray(List<String> contents) throws IOException;

    ObjectConverter<Object> CONVERTER = ObjectConverter.getInstance();

}
