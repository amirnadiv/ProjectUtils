package com.amirnadiv.project.utils.common.convert.object;

import java.util.Locale;

import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.TypeConverter;
import com.amirnadiv.project.utils.common.i18n.LocaleUtil;

public class LocaleConverter extends ObjectConverter<Locale> implements TypeConverter<Locale> {

    public LocaleConverter() {
        register(Locale.class);
    }

    @Override
    public Locale toConvert(String value) {
        return LocaleUtil.parseLocale(value);
    }

    @Override
    public String fromConvert(Locale value) {
        return String.valueOf(value);
    }

    public Locale toConvert(Object value) {
        if (value.getClass() == Locale.class) {
            return (Locale) value;
        }

        return LocaleUtil.parseLocale(value.toString());
    }

}
