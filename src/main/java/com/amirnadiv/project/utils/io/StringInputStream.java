package com.amirnadiv.project.utils.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.amirnadiv.project.utils.common.CharUtil;

public class StringInputStream extends InputStream implements Serializable {

    private static final long serialVersionUID = 7989260165741751591L;
    protected final String string;
    protected final Mode mode;
    protected int index;
    protected int charOffset;
    protected int available;

    public enum Mode {
        ALL,
        STRIP,
        ASCII
    }

    public StringInputStream(String string, Mode mode) {
        this.string = string;
        this.mode = mode;
        available = string.length();

        if (mode == Mode.ALL) {
            available <<= 1;
        }
    }

    @Override
    public int read() throws IOException {
        if (available == 0) {
            return -1;
        }
        available--;
        char c = string.charAt(index);

        switch (mode) {
            case ALL:
                if (charOffset == 0) {
                    charOffset = 1;
                    return (c & 0x0000ff00) >> 8;
                }
                charOffset = 0;
                index++;
                return c & 0x000000ff;
            case STRIP:
                index++;
                return c & 0x000000ff;
            case ASCII:
                index++;
                return CharUtil.toAscii(c);
        }
        return -1;
    }

    @Override
    public int available() throws IOException {
        return available;
    }

}
