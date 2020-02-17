package com.amirnadiv.project.utils.common.i18n;

import java.util.Map;

import com.amirnadiv.project.utils.common.ClassLoaderUtil;
import com.amirnadiv.project.utils.common.CollectionUtil;
import com.amirnadiv.project.utils.common.exception.ClassInstantiationException;

public abstract class CharConverter {
    public static final String SIMPLIFIED_TO_TRADITIONAL_CHINESE = "SimplifiedToTraditionalChinese";

    public static final String TRADITIONAL_TO_SIMPLIFIED_CHINESE = "TraditionalToSimplifiedChinese";

    // 私有变量
    private static final Map<String, CharConverter> converters = CollectionUtil.createConcurrentMap();

    public static final CharConverter getInstance(String name) {
        CharConverter converter = converters.get(name);

        if (converter == null) {
            CharConverterProvider provider;

            try {
                provider = (CharConverterProvider) ClassLoaderUtil.newServiceInstance("char.converter." + name);
            } catch (ClassInstantiationException e) {
                throw new IllegalArgumentException("Failed to load char converter provider: " + name + ": "
                        + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Failed to load char converter provider: " + name + ": "
                        + e.getMessage());
            }

            converter = provider.createCharConverter();
            converters.put(name, converter);
        }

        return converter;
    }

    public abstract char convert(char ch);

    public String convert(CharSequence chars) {
        return convert(chars, 0, chars.length());
    }

    public String convert(CharSequence chars, int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }

        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }

        int end = offset + count;

        if (end > chars.length()) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = offset; i < end; i++) {
            buffer.append(convert(chars.charAt(i)));
        }

        return buffer.toString();
    }

    public void convert(char[] chars) {
        convert(chars, 0, chars.length);
    }

    public void convert(char[] chars, int offset, int count) {
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }

        if (count < 0) {
            throw new ArrayIndexOutOfBoundsException(count);
        }

        int end = offset + count;

        if (end > chars.length) {
            throw new ArrayIndexOutOfBoundsException(offset + count);
        }

        for (int i = offset; i < end; i++) {
            chars[i] = convert(chars[i]);
        }
    }
}
