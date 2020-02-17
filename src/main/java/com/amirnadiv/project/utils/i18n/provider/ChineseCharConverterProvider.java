package com.amirnadiv.project.utils.common.i18n.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.amirnadiv.project.utils.common.i18n.CharConverter;
import com.amirnadiv.project.utils.common.i18n.CharConverterProvider;

public abstract class ChineseCharConverterProvider implements CharConverterProvider {
    public static final String CODE_TABLE_CHARSET = "UTF-16BE";
    private char[] codeTable;

    public CharConverter createCharConverter() {
        loadCodeTable();

        return new CharConverter() {
            @Override
            public char convert(char ch) {
                return codeTable[ch];
            }
        };
    }

    protected final char[] loadCodeTable() {
        if (codeTable == null) {
            InputStream istream = getClass().getResourceAsStream(getCodeTableName() + ".ctable");

            if (istream == null) {
                throw new RuntimeException("Could not find code table: " + getCodeTableName());
            }

            Reader reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(istream, CODE_TABLE_CHARSET));
                codeTable = new char[65536];

                for (int i = 0; i < 65536; i++) {
                    codeTable[i] = (char) reader.read();
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not read code table: " + getCodeTableName(), e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        return codeTable;
    }

    protected abstract String getCodeTableName();
}
