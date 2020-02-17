package com.amirnadiv.project.utils.common.bo;

import java.io.Serializable;

public class BaseObject<KEY extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    public BaseObject() {
    }

    protected KEY id;

    public KEY getId() {
        return id;
    }

    public void setId(KEY id) {
        this.id = id;
    }

}
