package com.amirnadiv.project.utils.common.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface TypeConverter<T> {

    T toConvert(String value);

    String fromConvert(T value);

    T toConvert(Object value);

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Convert {

    }

}
