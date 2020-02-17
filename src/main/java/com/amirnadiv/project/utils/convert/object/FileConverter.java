package com.amirnadiv.project.utils.common.convert.object;

import java.io.File;
import java.io.IOException;

import com.amirnadiv.project.utils.common.FileUtil;
import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.TypeConverter;
import com.amirnadiv.project.utils.common.io.StreamUtil;
import com.amirnadiv.project.utils.common.io.WriterUtil;

public class FileConverter extends ObjectConverter<File> implements TypeConverter<File> {

    public FileConverter() {
        register(File.class);
    }

    @Override
    public File toConvert(String value) {
        return convert(value);
    }

    @Override
    public String fromConvert(File value) {
        return value.getName();
    }

    public File toConvert(Object value) {
        if (value instanceof File) {
            return (File) value;
        }

        Class<?> type = value.getClass();
        if (type == byte[].class) {
            try {
                File tempFile = FileUtil.createTempFile();
                StreamUtil.writeBytes((byte[]) value, tempFile, true);

                return tempFile;
            } catch (IOException e) {
                throw new ConvertException(e);
            }
        }
        if (type == String.class) {
            return convert(value.toString());
        }

        throw new ConvertException(value);
    }

    private File convert(String value) {
        try {
            File tempFile = FileUtil.createTempFile();
            WriterUtil.writeLinesAndClose(tempFile, value);
            return tempFile;
        } catch (IOException e) {
            throw new ConvertException(e);
        }
    }

}
