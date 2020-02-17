package com.amirnadiv.project.utils.common.access;

public interface Access {

    String store(Resource accessable) throws AccessException;

    Resource retrieve(long id, String ext) throws AccessException;

    boolean remove(long id, String ext) throws AccessException;

    String getWhere(long id, String ext);

    String name();
}
