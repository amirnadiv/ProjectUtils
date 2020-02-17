package com.amirnadiv.project.utils.common;

public abstract class EnumUtil {

    public static <E extends Enum<E>> E parseName(Class<E> enumType, String name) {
        if (enumType == null || StringUtil.isBlank(name)) {
            return null;
        }

        return Enum.valueOf(enumType, name);
    }

}
