package com.amirnadiv.project.utils.common.convert.primitive;

import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class CharacterConverter extends ObjectConverter<Character> implements TypeConverter<Character> {

    public CharacterConverter() {
        register(Character.class);
        register(char.class);
    }

    @Override
    public Character toConvert(String value) {
        return value.charAt(0);
    }

    @Override
    public String fromConvert(Character value) {
        return String.valueOf(value);
    }

    public Character toConvert(Object value) {
        if (value.getClass() == Character.class) {
            return (Character) value;
        }
        if (value instanceof Number) {
            char c = (char) ((Number) value).intValue();
            return Character.valueOf(c);
        }

        return Character.valueOf(value.toString().charAt(0));
    }

}
