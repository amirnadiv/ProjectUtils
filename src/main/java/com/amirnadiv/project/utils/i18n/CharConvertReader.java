package com.amirnadiv.project.utils.common.i18n;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class CharConvertReader extends FilterReader {
    private CharConverter converter;

    public CharConvertReader(Reader in, String converterName) {
        this(in, CharConverter.getInstance(converterName));
    }

    public CharConvertReader(Reader in, CharConverter converter) {
        super(in);
        this.converter = converter;

        if (converter == null) {
            throw new NullPointerException("converter is null");
        }
    }

    @Override
    public int read() throws IOException {
        int ch = super.read();

        if (ch < 0) {
            return ch;
        }

        return converter.convert((char) ch);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int count = super.read(cbuf, off, len);

        if (count > 0) {
            converter.convert(cbuf, off, count);
        }

        return count;
    }
}
