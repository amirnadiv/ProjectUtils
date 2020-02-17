package com.amirnadiv.project.utils.common.io;

import static com.amirnadiv.project.utils.common.StringPool.Charset.UTF_8;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;

import com.amirnadiv.project.utils.common.CollectionUtil;
import com.amirnadiv.project.utils.common.FileUtil;
import com.amirnadiv.project.utils.common.ObjectUtil;

public abstract class WriterUtil {

    // public static ByteArray

    public static boolean writeLinesAndClose(String filePath, Collection<?> datas) throws IOException {
        return writeLinesAndClose(filePath, datas, false);
    }

    public static boolean writeLinesAndClose(String filePath, Collection<?> datas, String charsetName)
            throws IOException {
        return writeLinesAndClose(filePath, datas, false, charsetName);
    }

    public static boolean writeLinesAndClose(String filePath, Object...datas) throws IOException {
        return writeLinesAndClose(filePath, false, datas);
    }

    public static boolean writeLinesAndClose(String filePath, Object data) throws IOException {
        return writeLinesAndClose(filePath, data, false);
    }

    public static boolean writeLinesAndClose(File file, Object data) throws IOException {
        return writeLinesAndClose(file, data, false);
    }

    public static boolean writeLinesAndClose(String filePath, Collection<?> datas, boolean append) throws IOException {
        return writeLinesAndClose(filePath, datas, append, UTF_8);

    }

    public static boolean writeLinesAndClose(String filePath, Collection<?> datas, boolean append, String charsetName)
            throws IOException {
        if (filePath == null || CollectionUtil.isEmpty(datas)) {
            return false;
        }
        if (!FileUtil.exist(filePath)) {
            FileUtil.createParentDir(filePath);
        }

        Writer writer = null;
        try {
            writer = getWriter(filePath, append, charsetName);
            for (Object data : datas) {
                writer.append(data.toString()).append("\n");
            }
            return true;
        } finally {
            StreamUtil.close(writer);
        }

    }

    public static boolean writeLinesAndClose(String filePath, boolean append, Object...datas) throws IOException {
        return writeLinesAndClose(filePath, append, UTF_8, datas);
    }

    public static boolean writeLinesAndClose(String filePath, boolean append, String charsetName, Object...datas)
            throws IOException {
        if (filePath == null || ObjectUtil.isEmpty(datas)) {
            return false;
        }
        if (!FileUtil.exist(filePath)) {
            FileUtil.createParentDir(filePath);
        }

        Writer writer = null;
        try {
            writer = getWriter(filePath, append, charsetName);
            for (Object data : datas) {
                writer.append(data.toString()).append("\n");
            }
            return true;
        } finally {
            StreamUtil.close(writer);
        }
    }

    public static boolean writeLinesAndClose(String filePath, Object data, boolean append) throws IOException {
        return writeLinesAndClose(filePath, data, append, UTF_8);
    }

    public static boolean writeLinesAndClose(File file, Object data, boolean append) throws IOException {
        return writeLinesAndClose(file, data, append, UTF_8);
    }

    public static boolean writeLinesAndClose(String filePath, Object data, boolean append, String charsetName)
            throws IOException {
        if (filePath == null || data == null) {
            return false;
        }
        if (!FileUtil.exist(filePath)) {
            FileUtil.createParentDir(filePath);
        }

        Writer writer = null;
        try {
            writer = getWriter(filePath, append, charsetName);
            writer.append(data.toString()).append("\n");
            return true;
        } finally {
            StreamUtil.close(writer);
        }
    }

    public static boolean writeLinesAndClose(File file, Object data, boolean append, String charsetName)
            throws IOException {
        if (file == null || data == null) {
            return false;
        }
        if (!FileUtil.exist(file)) {
            FileUtil.createParentDir(file);
        }

        Writer writer = null;
        try {
            writer = getWriter(file, append, charsetName);
            writer.append(data.toString()).append("\n");
            return true;
        } finally {
            StreamUtil.close(writer);
        }
    }

    public static void writeLinesAndClose(Collection<?> datas, OutputStream out, String charsetName) throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(out, charsetName);
            for (Object data : datas) {
                writer.append(data.toString()).append("\n");
            }

        } finally {
            StreamUtil.close(writer);
        }
    }

    public static void writeLinesAndClose(OutputStream out, String charsetName, Object...datas) throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(out, charsetName);
            for (Object data : datas) {
                writer.append(data.toString()).append("\n");
            }

        } finally {
            StreamUtil.close(writer);
        }
    }

    private static Writer getWriter(String filePath, boolean append, String charsetName) throws IOException {
        OutputStream output = new FileOutputStream(filePath, append);

        Writer writer = new OutputStreamWriter(output, charsetName);
        return new BufferedWriter(writer);
    }

    private static Writer getWriter(File file, boolean append, String charsetName) throws IOException {
        OutputStream output = new FileOutputStream(file, append);

        Writer writer = new OutputStreamWriter(output, charsetName);
        return new BufferedWriter(writer);
    }

    // private static Writer getWriter(String filePath, boolean append)
    // throws IOException {
    // return getWriter(filePath, append, UTF_8);
    // }

}
