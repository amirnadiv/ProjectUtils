package com.amirnadiv.project.utils.common.access.strategy;

import com.amirnadiv.project.utils.common.access.AccessStrategy;

public class TimestampStrategy implements AccessStrategy {

    @Override
    public String find(long id) {
        return id + "_" + System.currentTimeMillis();
    }

}
