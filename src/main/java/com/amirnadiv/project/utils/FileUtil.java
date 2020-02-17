package com.amirnadiv.project.utils.common;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amirnadiv.project.utils.common.exception.IllegalPathException;
import com.amirnadiv.project.utils.common.io.StreamUtil;

public abstract class FileUtil {



    private static final char COLON_CHAR = ':';
    private static final String UNC_PREFIX = "//";
    private static final String SLASH = "/";
    private static final char SLASH_CHAR = '/';
    private static final char BACKSLASH_CHAR = '\\';
    private static final String ALL_SLASH = "/\\";

    public static final String CURRENT_DIR = ".";

    public static final String UP_LEVEL_DIR = "..";

    private static final char EXTENSION_SEPARATOR = '.';

    private static final char UNIX_SEPARATOR = '/';

    private static final char WINDOWS_SEPARATOR = '\\';

    private static final String TEMP_FILE_PREFIX = "temp_file_prefix-";

    public static File toFile(URL url) {
        if (url == null) {
            return null;
        }

        if (!"file".equals(url.getProtocol())) {
            return null;
        }

        String path = url.getPath();

        return (path != null) ? new File(StringEscapeUtil.unescapeURL(path)) : null;

    }

    public static boolean exist(String path) {
        return (path == null) ? false : new File(path).exists();
    }

    public static boolean exist(File file) {
        return (file == null) ? false : file.exists();
    }

    public static boolean exist(String directory, String regexp) {
        File file = new File(directory);
        if (!file.exists()) {
            return false;
        }

        String[] fileList = file.list();
        if (fileList == null) {
            return false;
        }

        for (String fileName : fileList) {
            if (fileName.matches(regexp)) {
                return true;
            }

        }
        return false;
    }

    public static boolean isDirectory(String path) {
        return (path == null) ? false : new File(path).isDirectory();
    }

    public static boolean isDirectory(File file) {
        return (file == null) ? false : file.isDirectory();
    }

    public static boolean isFile(String path) {
        return (path == null) ? false : new File(path).isDirectory();
    }

    public static boolean isFile(File file) {
        return (file == null) ? false : file.isDirectory();
    }

    public static File[] listDirSuffixFiles(File dir, final String suffix) {
        if (dir == null) {
            return null;
        }
        if (!dir.exists() || dir.isFile()) {
            return null;
        }

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return StringUtil.isEmpty(suffix) ? true : (file.getName().endsWith(suffix));
            }
        });
    }

    public static File[] listDirSuffixFiles(String dirPath, final String suffix) {
        if (!exist(dirPath)) {
            return null;
        }
        File dir = new File(dirPath);

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return StringUtil.isEmpty(suffix) ? true : (file.getName().endsWith(suffix));
            }
        });
    }

    public static File[] listDirAllConditionFiles(File dir, final boolean...conditions) {
        if (dir == null) {
            return null;
        }
        if (!dir.exists() || dir.isFile()) {
            return null;
        }

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (ArrayUtil.isEmpty(conditions)) {
                    return true;
                }
                for (boolean condition : conditions) {
                    if (!condition) {
                        return false;
                    }
                }

                return true;
            }
        });
    }

    public static File[] listDirAllConditionFiles(String dirPath, final boolean...conditions) {
        if (!exist(dirPath)) {
            return null;
        }
        File dir = new File(dirPath);

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (ArrayUtil.isEmpty(conditions)) {
                    return true;
                }
                for (boolean condition : conditions) {
                    if (!condition) {
                        return false;
                    }
                }

                return true;
            }
        });
    }

    public static File[] listDirAnyConditionFiles(File dir, final boolean...conditions) {
        if (dir == null) {
            return null;
        }
        if (!dir.exists() || dir.isFile()) {
            return null;
        }

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (ArrayUtil.isEmpty(conditions)) {
                    return true;
                }
                for (boolean condition : conditions) {
                    if (condition) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    public static File[] listDirAnyConditionFiles(String dirPath, final boolean...conditions) {
        if (!exist(dirPath)) {
            return null;
        }
        File dir = new File(dirPath);

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (ArrayUtil.isEmpty(conditions)) {
                    return true;
                }
                for (boolean condition : conditions) {
                    if (condition) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    public static File file(String filename) {
        if (filename == null) {
            return null;
        }
        return new File(filename);
    }

    public static File file(File parent, String child) {
        if (child == null) {
            return null;
        }

        return new File(parent, child);
    }

    public static byte[] readBytes(String file) throws IOException {
        return readBytes(file(file));
    }

    public static byte[] readBytes(File file) throws IOException {
        if (file == null || (!file.exists()) || !file.isFile()) {
            return null;
        }

        long length = file.length();
        if (length >= Integer.MAX_VALUE) {
            throw new RuntimeException("File is larger then max array size");
        }

        byte[] bytes = new byte[(int) length];
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.readFully(bytes);

            return bytes;
        } finally {
            StreamUtil.close(randomAccessFile);
        }

    }

    public static int getFileSize(String fileName) throws IOException {
        if (StringUtil.isBlank(fileName)) {
            return 0;
        }

        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            if (file.exists()) {
                fis = new FileInputStream(file);
                return fis.available();
            }

            return 0;
        } finally {
            StreamUtil.close(fis);
        }

    }

    public static boolean createFile(String path) throws IOException {
        return createFile(path, false);
    }

    public static boolean createFile(String path, boolean override) throws IOException {
        if (path == null) {
            return false;
        }

        File file = new File(path);
        if (file.exists() && !override) {
            return false;
        }

        if (file.isDirectory()) {
            return file.mkdirs();
        }

        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        return file.createNewFile();
    }

    public static boolean createDir(String path, boolean override) {
        if (path == null) {
            return false;
        }

        File file = new File(path);
        if (file.exists() && !override) {
            return false;
        }

        return file.mkdirs();
    }

    public static boolean createDir(String path) {
        return createDir(path, false);
    }

    public static boolean createParentDir(String path) {
        return createParentDir(path, false);
    }

    public static boolean createParentDir(File file) {
        return createParentDir(file, false);
    }

    public static boolean createParentDir(String path, boolean override) {
        if (path == null) {
            return false;
        }

        return createDir(new File(path).getParent(), override);
    }

    public static boolean createParentDir(File file, boolean override) {
        if (file == null) {
            return false;
        }

        return createDir(file.getParent(), override);
    }

    public static boolean delete(File file) {
        if (file == null) {
            return false;
        }

        return file.delete();
    }

    public static boolean delete(String path) {
        if (path == null) {
            return false;
        }

        return new File(path).delete();
    }

    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        }

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    public static boolean deleteDir(String path) {
        if (path == null) {
            return false;
        }

        return deleteDir(new File(path));
    }

    // FIXME
    public static Date lastModifiedTime(File file) {
        if (!exist(file)) {
            return null;
        }

        return new Date(file.lastModified());
    }

    public static Date lastModifiedTime(String path) {
        File file = new File(path);
        if (!exist(file)) {
            return null;
        }

        return new Date(file.lastModified());
    }

    // ==========================================================================

    // ==========================================================================

    public static String normalizeAbsolutePath(String path) throws IllegalPathException {
        return normalizePath(path, true, false, false);
    }

    public static String normalizeAbsolutePath(String path, boolean removeTrailingSlash) throws IllegalPathException {
        return normalizePath(path, true, false, removeTrailingSlash);
    }

    public static String normalizeWindowsPath(String path) {
        return normalizePath(path, true);
    }

    public static String normalizeUnixPath(String path) {
        return normalizePath(path, false);
    }

    public static String normalizeRelativePath(String path) throws IllegalPathException {
        return normalizePath(path, false, true, false);
    }

    public static String normalizeRelativePath(String path, boolean removeTrailingSlash) throws IllegalPathException {
        return normalizePath(path, false, true, removeTrailingSlash);
    }

    public static String normalizePath(String path) throws IllegalPathException {
        return normalizePath(path, false, false, false);
    }

    public static String normalizePath(String path, boolean removeTrailingSlash) throws IllegalPathException {
        return normalizePath(path, false, false, removeTrailingSlash);
    }

    private static String normalizePath(String path, boolean forceAbsolute, boolean forceRelative,
            boolean removeTrailingSlash) throws IllegalPathException {
        char[] pathChars = StringUtil.trimToEmpty(path).toCharArray();
        int length = pathChars.length;


        boolean startsWithSlash = false;
        boolean endsWithSlash = false;

        if (length > 0) {
            char firstChar = pathChars[0];
            char lastChar = pathChars[length - 1];

            startsWithSlash = firstChar == '/' || firstChar == '\\';
            endsWithSlash = lastChar == '/' || lastChar == '\\';
        }

        StringBuilder buf = new StringBuilder(length);
        boolean isAbsolutePath = forceAbsolute || !forceRelative && startsWithSlash;
        int index = startsWithSlash ? 0 : -1;
        int level = 0;

        if (isAbsolutePath) {
            buf.append("/");
        }

        while (index < length) {

            index = indexOfSlash(pathChars, index + 1, false);

            if (index == length) {
                break;
            }


            int nextSlashIndex = indexOfSlash(pathChars, index, true);

            String element = new String(pathChars, index, nextSlashIndex - index);
            index = nextSlashIndex;


            if (".".equals(element)) {
                continue;
            }


            if ("..".equals(element)) {
                if (level == 0) {

                    if (isAbsolutePath) {
                        throw new IllegalPathException(path);
                    } else {
                        buf.append("../");
                    }
                } else {
                    buf.setLength(pathChars[--level]);
                }

                continue;
            }

            pathChars[level++] = (char) buf.length();
            buf.append(element).append('/');
        }


        if (buf.length() > 0) {
            if (!endsWithSlash || removeTrailingSlash) {
                buf.setLength(buf.length() - 1);
            }
        }

        return buf.toString();
    }

    private static int indexOfSlash(char[] chars, int beginIndex, boolean slash) {
        int i = beginIndex;

        for (; i < chars.length; i++) {
            char ch = chars[i];

            if (slash) {
                if (ch == '/' || ch == '\\') {
                    break; // if a slash
                }
            } else {
                if (ch != '/' && ch != '\\') {
                    break; // if not a slash
                }
            }
        }

        return i;
    }

    // ==========================================================================

    // ==========================================================================

    public static String getAbsolutePathBasedOn(String basedir, String path) throws IllegalPathException {

        boolean isAbsolutePath = false;

        path = StringUtil.trimToEmpty(path);

        if (path.length() > 0) {
            char firstChar = path.charAt(0);
            isAbsolutePath = firstChar == '/' || firstChar == '\\';
        }

        if (!isAbsolutePath) {

            if (path.length() > 0) {
                path = StringUtil.trimToEmpty(basedir) + "/" + path;
            } else {
                path = StringUtil.trimToEmpty(basedir);
            }
        }

        return normalizeAbsolutePath(path);
    }

    public static String getSystemDependentAbsolutePathBasedOn(String basedir, String path) {
        path = StringUtil.trimToEmpty(path);

        boolean endsWithSlash = path.endsWith("/") || path.endsWith("\\");

        File pathFile = new File(path);

        if (pathFile.isAbsolute()) {

            path = pathFile.getAbsolutePath();
        } else {

            basedir = StringUtil.trimToEmpty(basedir);

            File baseFile = new File(basedir);

            if (baseFile.isAbsolute()) {
                path = new File(baseFile, path).getAbsolutePath();
            } else {
                throw new IllegalPathException("Basedir is not absolute path: " + basedir);
            }
        }

        if (endsWithSlash) {
            path = path + '/';
        }

        return normalizePath(path);
    }

    private static String getSystemDependentPrefix(String path, boolean isWindows) {
        if (isWindows) {

            if (path.startsWith(UNC_PREFIX)) {

                if (path.length() == UNC_PREFIX.length()) {
                    return null;
                }


                int index = path.indexOf(SLASH, UNC_PREFIX.length());

                if (index != -1) {
                    return path.substring(0, index);
                } else {
                    return path;
                }
            }


            if ((path.length() > 1) && (path.charAt(1) == COLON_CHAR)) {
                return path.substring(0, 2).toUpperCase();
            }
        }

        return "";
    }

    // ==========================================================================

    // ==========================================================================

    public static String getRelativePath(String basedir, String path) throws IllegalPathException {

        basedir = normalizeAbsolutePath(basedir);


        path = getAbsolutePathBasedOn(basedir, path);


        boolean endsWithSlash = path.endsWith("/");


        String[] baseParts = StringUtil.split(basedir, '/');
        String[] parts = StringUtil.split(path, '/');
        StringBuilder buf = new StringBuilder();
        int i = 0;

        while (i < baseParts.length && i < parts.length && baseParts[i].equals(parts[i])) {
            i++;
        }

        if (i < baseParts.length && i < parts.length) {
            for (int j = i; j < baseParts.length; j++) {
                buf.append("..").append('/');
            }
        }

        for (; i < parts.length; i++) {
            buf.append(parts[i]);

            if (i < parts.length - 1) {
                buf.append('/');
            }
        }

        if (endsWithSlash && buf.length() > 0 && buf.charAt(buf.length() - 1) != '/') {
            buf.append('/');
        }

        return buf.toString();
    }

    public static String getPathBasedOn(String basedir, String path) {
        return getPathBasedOn(basedir, path, SystemUtil.getOsInfo().isWindows());
    }

    public static String getWindowsPathBasedOn(String basedir, String path) {
        return getPathBasedOn(basedir, path, true);
    }

    public static String getUnixPathBasedOn(String basedir, String path) {
        return getPathBasedOn(basedir, path, false);
    }

    private static String getPathBasedOn(String basedir, String path, boolean isWindows) {

        if (path == null) {
            return null;
        }

        path = path.trim();


        path = path.replace(BACKSLASH_CHAR, SLASH_CHAR);


        String prefix = getSystemDependentPrefix(path, isWindows);

        if (prefix == null) {
            return null;
        }


        if ((prefix.length() > 0)
                || ((path.length() > prefix.length()) && (path.charAt(prefix.length()) == SLASH_CHAR))) {
            return normalizePath(path, isWindows);
        }


        if (basedir == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        builder.append(basedir.trim());


        if ((basedir.length() > 0) && (path.length() > 0) && (basedir.charAt(basedir.length() - 1) != SLASH_CHAR)) {
            builder.append(SLASH_CHAR);
        }

        builder.append(path);

        return normalizePath(builder.toString(), isWindows);
    }

    public static String getWindowsRelativePath(String basedir, String path) {
        return getRelativePath(basedir, path, true);
    }

    public static String getUnixRelativePath(String basedir, String path) {
        return getRelativePath(basedir, path, false);
    }

    private static String getRelativePath(String basedir, String path, boolean isWindows) {

        basedir = normalizePath(basedir, isWindows);

        if (basedir == null) {
            return null;
        }

        String basePrefix = getSystemDependentPrefix(basedir, isWindows);

        if ((basePrefix == null) || ((basePrefix.length() == 0) && !basedir.startsWith(SLASH))) {
            return null;
        }


        path = getPathBasedOn(basedir, path, isWindows);

        if (path == null) {
            return null;
        }

        String prefix = getSystemDependentPrefix(path, isWindows);


        if (!basePrefix.equals(prefix)) {
            return path;
        }


        boolean endsWithSlash = path.endsWith(SLASH);


        String[] baseParts = StringUtil.split(basedir.substring(basePrefix.length()), SLASH_CHAR);
        String[] parts = StringUtil.split(path.substring(prefix.length()), SLASH_CHAR);
        StringBuilder builder = new StringBuilder();
        int i = 0;

        if (isWindows) {
            while ((i < baseParts.length) && (i < parts.length) && baseParts[i].equalsIgnoreCase(parts[i])) {
                i++;
            }
        } else {
            while ((i < baseParts.length) && (i < parts.length) && baseParts[i].equals(parts[i])) {
                i++;
            }
        }

        if ((i < baseParts.length) && (i < parts.length)) {
            for (int j = i; j < baseParts.length; j++) {
                builder.append(UP_LEVEL_DIR).append(SLASH_CHAR);
            }
        }

        for (; i < parts.length; i++) {
            builder.append(parts[i]);

            if (i < (parts.length - 1)) {
                builder.append(SLASH_CHAR);
            }
        }

        if (builder.length() == 0) {
            builder.append(CURRENT_DIR);
        }

        String relpath = builder.toString();

        if (endsWithSlash && !relpath.endsWith(SLASH)) {
            relpath += SLASH;
        }

        return relpath;
    }


    public static String getExtension(String fileName) {
        return getExtension(fileName, null, false);
    }

    public static String getExtension(String fileName, boolean toLowerCase) {
        return getExtension(fileName, null, toLowerCase);
    }

    public static String getExtension(String fileName, String nullExt) {
        return getExtension(fileName, nullExt, false);
    }

    public static String getExtension(String fileName, String nullExt, boolean toLowerCase) {
        fileName = StringUtil.trimToNull(fileName);

        if (fileName == null) {
            return null;
        }

        fileName = fileName.replace('\\', '/');
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

        int index = fileName.lastIndexOf(".");
        String ext = null;

        if (index >= 0) {
            ext = StringUtil.trimToNull(fileName.substring(index + 1));
        }

        if (ext == null) {
            return nullExt;
        }

        return toLowerCase ? ext.toLowerCase() : ext;
    }

    public static FileNameAndExtension getFileNameAndExtension(String path) {
        return getFileNameAndExtension(path, false);
    }

    public static FileNameAndExtension getFileNameAndExtension(String path, boolean extensionToLowerCase) {
        path = StringUtil.trimToEmpty(path);

        String fileName = path;
        String extension = null;

        if (!StringUtil.isEmpty(path)) {

            int index = path.lastIndexOf('.');

            if (index >= 0) {
                extension = StringUtil.trimToNull(StringUtil.substring(path, index + 1));

                if (!StringUtil.containsNone(extension, "/\\")) {
                    extension = null;
                    index = -1;
                }
            }

            if (index >= 0) {
                fileName = StringUtil.substring(path, 0, index);
            }
        }

        return new FileNameAndExtension(fileName, extension, extensionToLowerCase);
    }

    public static String normalizeExtension(String ext) {
        ext = StringUtil.trimToNull(ext);

        if (ext != null) {
            ext = ext.toLowerCase();

            if (ext.startsWith(".")) {
                ext = StringUtil.trimToNull(ext.substring(1));
            }
        }

        return ext;
    }

    // FIXME
    public static class FileNameAndExtension {
        private final String fileName;
        private final String extension;

        private FileNameAndExtension(String fileName, String extension, boolean extensionToLowerCase) {
            this.fileName = fileName;
            this.extension = extensionToLowerCase ? StringUtil.toLowerCase(extension) : extension;
        }

        public String getFileName() {
            return fileName;
        }

        public String getExtension() {
            return extension;
        }

        @Override
        public String toString() {
            return extension == null ? fileName : fileName + "." + extension;
        }
    }


    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }

        int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }

        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        int lastSeparator = indexOfLastSeparator(filename);
        return (lastSeparator > extensionPos ? -1 : extensionPos);
    }

    public static String[] parseExtension(String path) {
        path = StringUtil.trimToEmpty(path);

        String[] parts = { path, null };

        if (StringUtil.isEmpty(path)) {
            return parts;
        }


        int index = StringUtil.lastIndexOf(path, EXTENSION_SEPARATOR);
        String extension = null;

        if (index >= 0) {
            extension = StringUtil.trimToNull(StringUtil.substring(path, index + 1));

            if (!StringUtil.containsNone(extension, ALL_SLASH)) {
                extension = null;
                index = -1;
            }
        }

        if (index >= 0) {
            parts[0] = StringUtil.substring(path, 0, index);
        }

        parts[1] = extension;

        return parts;
    }


    private static final Pattern schemePrefixPattern = Pattern.compile(
            "(file:/*[a-z]:)|(\\w+://.+?/)|((jar|zip):.+!/)|(\\w+:)", Pattern.CASE_INSENSITIVE);

    public static String resolve(String url, String relativePath) {
        url = StringUtil.trimToEmpty(url);

        Matcher m = schemePrefixPattern.matcher(url);
        int index = 0;

        if (m.find()) {
            index = m.end();

            if (url.charAt(index - 1) == '/') {
                index--;
            }
        }

        return url.substring(0, index) + normalizeAbsolutePath(url.substring(index) + "/../" + relativePath);
    }

    public static File getResourcesFile(String resourceName) {
        URL url = ClassLoaderUtil.getResource(resourceName);
        if (url == null) {
            return null;
        }

        String filePath = url.getFile();
        return new File(filePath);
    }

    public static File getResourcesFile(URL url) {
        if (url == null) {
            return null;
        }

        String filePath = url.getFile();
        return new File(filePath);
    }

    public static File createAndReturnFile(String filename) throws IOException {
        File file = newFile(filename);
        if (file != null && !file.canWrite()) {
            String dirName = file.getPath();
            int i = dirName.lastIndexOf(File.separator);
            if (i > -1) {
                dirName = dirName.substring(0, i);
                File dir = newFile(dirName);
                dir.mkdirs();
            }

            file.createNewFile();
        }
        return file;
    }

    public static File newFile(String pathName) throws IOException {
        if (StringUtil.isBlank(pathName)) {
            return null;
        }

        return new File(pathName).getCanonicalFile();
    }


    public static File createTempDirectory(String prefix, String suffix) throws IOException {
        return createTempDirectory(prefix, suffix, (File) null);
    }

    public static File createTempDirectory(String prefix, String suffix, String tempDirName) throws IOException {
        return createTempDirectory(prefix, suffix, new File(tempDirName));
    }

    public static File createTempDirectory(String prefix, String suffix, File tempDir) throws IOException {
        File file = doCreateTempFile(prefix, suffix, tempDir);
        file.delete();
        file.mkdir();
        return file;
    }

    public static File createTempFile() throws IOException {
        return createTempFile(true);
    }

    public static File createTempFile(boolean create) throws IOException {
        return createTempFile(TEMP_FILE_PREFIX, ".tmp", (File) null, create);
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        return createTempFile(prefix, suffix, (File) null, true);
    }

    public static File createTempFile(String prefix, String suffix, boolean create) throws IOException {
        return createTempFile(prefix, suffix, (File) null, create);
    }

    public static File createTempFile(String prefix, String suffix, String tempDirName) throws IOException {
        return createTempFile(prefix, suffix, new File(tempDirName), true);
    }

    public static File createTempFile(String prefix, String suffix, File tempDir) throws IOException {
        return createTempFile(prefix, suffix, tempDir, true);
    }

    public static File createTempFile(String prefix, String suffix, String tempDirName, boolean create)
            throws IOException {
        return createTempFile(prefix, suffix, new File(tempDirName), create);
    }

    public static File createTempFile(String prefix, String suffix, File tempDir, boolean create) throws IOException {
        // FIXME
        if (StringUtil.isBlank(prefix)) {
            return null;
        }

        File file = doCreateTempFile(prefix, suffix, tempDir);
        file.delete();
        if (create) {
            file.createNewFile();
        }
        return file;
    }

    private static File doCreateTempFile(String prefix, String suffix, File dir) throws IOException {
        int exceptionsCount = 0;
        while (true) {
            try {
                return File.createTempFile(prefix, suffix, dir).getCanonicalFile();
            } catch (IOException e) { // fixes

                if (++exceptionsCount >= 100) {
                    throw e;
                }
            }
        }
    }



    public static String getClassFilePath(Class<?> clazz) throws IOException {
        if (clazz == null) {
            return null;
        }

        URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
        File file = new File(filePath);
        return file.getAbsolutePath();
    }

}
