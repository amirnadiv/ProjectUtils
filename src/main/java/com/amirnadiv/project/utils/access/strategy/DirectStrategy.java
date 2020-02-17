package com.amirnadiv.project.utils.common.access.strategy;

import com.amirnadiv.project.utils.common.access.AccessStrategy;

public class DirectStrategy implements AccessStrategy {

    @Override
    public String find(long id) {
        return String.valueOf(id);
    }

}
