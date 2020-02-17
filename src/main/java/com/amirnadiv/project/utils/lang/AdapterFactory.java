package com.amirnadiv.project.utils.common.lang;

import com.amirnadiv.project.utils.common.able.Adaptable;

public class AdapterFactory<O, N> {

    public static <O, N> N create(Adaptable<O, N> adaptable, O old) {
        return adaptable.forNew(old);
    }

}
