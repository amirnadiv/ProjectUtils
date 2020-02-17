package com.amirnadiv.project.utils.common.file;

import java.util.List;

import com.amirnadiv.project.utils.common.collection.ListMap;

public class FileBeanInfo {

    private ListMap<String, String> headers;

    private Object[] beans;

    private List<String> fields;

    private List<String> titles;

    public ListMap<String, String> getHeaders() {
        return headers;
    }

    public FileBeanInfo setHeaders(ListMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Object[] getBeans() {
        return beans;
    }

    public FileBeanInfo setBeans(Object[] beans) {
        this.beans = beans;
        return this;
    }

    public List<String> getFields() {
        return fields;
    }

    public FileBeanInfo setFields(List<String> fields) {
        this.fields = fields;
        return this;
    }

    public List<String> getTitles() {
        return titles;
    }

    public FileBeanInfo setTitles(List<String> titles) {
        this.titles = titles;
        return this;
    }

}
