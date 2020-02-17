package com.amirnadiv.project.utils.common.bean.introspector;

public abstract class Descriptor {

    protected final ClassDescriptor classDescriptor;
    protected final boolean isPublic;

    protected Descriptor(ClassDescriptor classDescriptor, boolean isPublic) {
        this.classDescriptor = classDescriptor;
        this.isPublic = isPublic;
    }

    public ClassDescriptor getClassDescriptor() {
        return classDescriptor;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean matchDeclared(boolean declared) {
        if (!declared) {
            return isPublic;
        }
        return true;
    }

    public abstract String getName();

}
