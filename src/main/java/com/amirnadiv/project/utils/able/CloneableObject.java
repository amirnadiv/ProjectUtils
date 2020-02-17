package com.amirnadiv.project.utils.common.able;

public interface CloneableObject<T> extends Cloneable {


    T clone() throws CloneNotSupportedException;

}
