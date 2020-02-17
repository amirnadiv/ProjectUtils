package com.amirnadiv.project.utils.common.access;

public class AccessDecorator implements Access {

    protected Access access;

    protected String name;

    public AccessDecorator(Access access, String name) {
        this.access = access;
        this.name = name;
    }

    @Override
    public String store(Resource accessable) throws AccessException {
        return access.store(accessable);
    }

    @Override
    public Resource retrieve(long id, String ext) throws AccessException {
        return access.retrieve(id, ext);
    }

    @Override
    public boolean remove(long id, String ext) throws AccessException {
        return access.remove(id, ext);
    }

    @Override
    public String getWhere(long id, String ext) {
        return access.getWhere(id, ext);
    }

    @Override
    public String name() {
        return name + "[" + access.name() + "]";
    }

}
