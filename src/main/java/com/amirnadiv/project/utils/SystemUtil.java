package com.amirnadiv.project.utils.common;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.amirnadiv.project.utils.common.i18n.LocaleUtil;

public abstract class SystemUtil {
    private static final JvmSpecInfo JVM_SPEC_INFO = new JvmSpecInfo();
    private static final JvmInfo JVM_INFO = new JvmInfo();
    private static final JavaSpecInfo JAVA_SPEC_INFO = new JavaSpecInfo();
    private static final JavaInfo JAVA_INFO = new JavaInfo();
    private static final OsInfo OS_INFO = new OsInfo();
    private static final UserInfo USER_INFO = new UserInfo();
    private static final HostInfo HOST_INFO = new HostInfo();
    private static final JavaRuntimeInfo JAVA_RUNTIME_INFO = new JavaRuntimeInfo();

    private SystemUtil() {

    }

    public static final JvmSpecInfo getJvmSpecInfo() {
        return JVM_SPEC_INFO;
    }

    public static final JvmInfo getJvmInfo() {
        return JVM_INFO;
    }

    public static final JavaSpecInfo getJavaSpecInfo() {
        return JAVA_SPEC_INFO;
    }

    public static final JavaInfo getJavaInfo() {
        return JAVA_INFO;
    }

    public static final JavaRuntimeInfo getJavaRuntimeInfo() {
        return JAVA_RUNTIME_INFO;
    }

    public static final OsInfo getOsInfo() {
        return OS_INFO;
    }

    public static final UserInfo getUserInfo() {
        return USER_INFO;
    }

    public static final HostInfo getHostInfo() {
        return HOST_INFO;
    }

    public static final class JvmSpecInfo {
        private final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name", false);
        private final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version", false);
        private final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor", false);


        private JvmSpecInfo() {
        }


        public final String getName() {
            return JAVA_VM_SPECIFICATION_NAME;
        }


        public final String getVersion() {
            return JAVA_VM_SPECIFICATION_VERSION;
        }


        public final String getVendor() {
            return JAVA_VM_SPECIFICATION_VENDOR;
        }


        public final String toString() {
            StringBuilder builder = new StringBuilder();

            append(builder, "JavaVM Spec. Name:    ", getName());
            append(builder, "JavaVM Spec. Version: ", getVersion());
            append(builder, "JavaVM Spec. Vendor:  ", getVendor());

            return builder.toString();
        }
    }

    public static final class JvmInfo {
        private final String JAVA_VM_NAME = getSystemProperty("java.vm.name", false);
        private final String JAVA_VM_VERSION = getSystemProperty("java.vm.version", false);
        private final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor", false);
        private final String JAVA_VM_INFO = getSystemProperty("java.vm.info", false);


        private JvmInfo() {
        }


        public final String getName() {
            return JAVA_VM_NAME;
        }


        public final String getVersion() {
            return JAVA_VM_VERSION;
        }


        public final String getVendor() {
            return JAVA_VM_VENDOR;
        }


        public final String getInfo() {
            return JAVA_VM_INFO;
        }


        public final String toString() {
            StringBuilder builder = new StringBuilder();

            append(builder, "JavaVM Name:    ", getName());
            append(builder, "JavaVM Version: ", getVersion());
            append(builder, "JavaVM Vendor:  ", getVendor());
            append(builder, "JavaVM Info:    ", getInfo());

            return builder.toString();
        }
    }

    public static final class JavaSpecInfo {
        private final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name", false);
        private final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version", false);
        private final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor", false);


        private JavaSpecInfo() {
        }


        public final String getName() {
            return JAVA_SPECIFICATION_NAME;
        }


        public final String getVersion() {
            return JAVA_SPECIFICATION_VERSION;
        }


        public final String getVendor() {
            return JAVA_SPECIFICATION_VENDOR;
        }


        public final String toString() {
            StringBuilder builder = new StringBuilder();

            append(builder, "Java Spec. Name:    ", getName());
            append(builder, "Java Spec. Version: ", getVersion());
            append(builder, "Java Spec. Vendor:  ", getVendor());

            return builder.toString();
        }
    }

    public static final class JavaInfo {
        private final String JAVA_VERSION = getSystemProperty("java.version", false);
        private final float JAVA_VERSION_FLOAT = getJavaVersionAsFloat();
        private final int JAVA_VERSION_INT = getJavaVersionAsInt();
        private final String JAVA_VENDOR = getSystemProperty("java.vendor", false);
        private final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url", false);


        private final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");
        private final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");
        private final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");
        private final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");
        private final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");
        private final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");
        private final boolean IS_JAVA_1_7 = getJavaVersionMatches("1.7");


        private JavaInfo() {
        }


        public final String getVersion() {
            return JAVA_VERSION;
        }


        public final float getVersionFloat() {
            return JAVA_VERSION_FLOAT;
        }


        public final int getVersionInt() {
            return JAVA_VERSION_INT;
        }


        private final float getJavaVersionAsFloat() {
            if (JAVA_VERSION == null) {
                return 0f;
            }

            String str = JAVA_VERSION.substring(0, 3);

            if (JAVA_VERSION.length() >= 5) {
                str = str + JAVA_VERSION.substring(4, 5);
            }

            return Float.parseFloat(str);
        }


        private final int getJavaVersionAsInt() {
            if (JAVA_VERSION == null) {
                return 0;
            }

            String str = JAVA_VERSION.substring(0, 1);

            str = str + JAVA_VERSION.substring(2, 3);

            if (JAVA_VERSION.length() >= 5) {
                str = str + JAVA_VERSION.substring(4, 5);
            } else {
                str = str + "0";
            }

            return Integer.parseInt(str);
        }


        public final String getVendor() {
            return JAVA_VENDOR;
        }


        public final String getVendorURL() {
            return JAVA_VENDOR_URL;
        }


        public final boolean isJava11() {
            return IS_JAVA_1_1;
        }


        public final boolean isJava12() {
            return IS_JAVA_1_2;
        }


        public final boolean isJava13() {
            return IS_JAVA_1_3;
        }


        public final boolean isJava14() {
            return IS_JAVA_1_4;
        }


        public final boolean isJava15() {
            return IS_JAVA_1_5;
        }


        public final boolean isJava16() {
            return IS_JAVA_1_6;
        }


        public final boolean isJava17() {
            return IS_JAVA_1_7;
        }


        private final boolean getJavaVersionMatches(String versionPrefix) {
            if (JAVA_VERSION == null) {
                return false;
            }

            return JAVA_VERSION.startsWith(versionPrefix);
        }


        public final boolean isJavaVersionAtLeast(float requiredVersion) {
            return getVersionFloat() >= requiredVersion;
        }


        public final boolean isJavaVersionAtLeast(int requiredVersion) {
            return getVersionInt() >= requiredVersion;
        }


        public final String toString() {
            StringBuilder builder = new StringBuilder();

            append(builder, "Java Version:    ", getVersion());
            append(builder, "Java Vendor:     ", getVendor());
            append(builder, "Java Vendor URL: ", getVendorURL());

            return builder.toString();
        }
    }

    public static final class JavaRuntimeInfo {
        private final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name", false);
        private final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version", false);
        private final String JAVA_HOME = getSystemProperty("java.home", false);
        private final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs", false);
        private final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs", false);
        private final String JAVA_CLASS_PATH = getSystemProperty("java.class.path", false);
        private final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version", false);
        private final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path", false);

        private final String SUN_BOOT_CLASS_PATH = getSystemProperty("sun.boot.class.path", false);

        private final String SUN_ARCH_DATA_MODEL = getSystemProperty("sun.arch.data.model", false);


        private JavaRuntimeInfo() {
        }

        public final String getSunBoothClassPath() {
            return SUN_BOOT_CLASS_PATH;
        }


        public final String getSunArchDataModel() {
            return SUN_ARCH_DATA_MODEL;
        }


        public final String getName() {
            return JAVA_RUNTIME_NAME;
        }


        public final String getVersion() {
            return JAVA_RUNTIME_VERSION;
        }


        public final String getHomeDir() {
            return JAVA_HOME;
        }


        public final String getExtDirs() {
            return JAVA_EXT_DIRS;
        }


        public final String getEndorsedDirs() {
            return JAVA_ENDORSED_DIRS;
        }

        public final String getClassPath() {
            return JAVA_CLASS_PATH;
        }

        public final String[] getClassPathArray() {
            return StringUtil.split(getClassPath(), getOsInfo().getPathSeparator());
        }

        public final String getClassVersion() {
            return JAVA_CLASS_VERSION;
        }

        public final String getLibraryPath() {
            return JAVA_LIBRARY_PATH;
        }

        public final String[] getLibraryPathArray() {
            return StringUtil.split(getLibraryPath(), getOsInfo().getPathSeparator());
        }

        public final String getProtocolPackages() {
            return getSystemProperty("java.protocol.handler.pkgs", true);
        }

        public final String toString() {
            StringBuilder builder = new StringBuilder();

            append(builder, "Java Runtime Name:      ", getName());
            append(builder, "Java Runtime Version:   ", getVersion());
            append(builder, "Java Home Dir:          ", getHomeDir());
            append(builder, "Java Extension Dirs:    ", getExtDirs());
            append(builder, "Java Endorsed Dirs:     ", getEndorsedDirs());
            append(builder, "Java Class Path:        ", getClassPath());
            append(builder, "Java Class Version:     ", getClassVersion());
            append(builder, "Java Library Path:      ", getLibraryPath());
            append(builder, "Java Protocol Packages: ", getProtocolPackages());

            return builder.toString();
        }
    }

    public static final class OsInfo {
        private final String OS_VERSION = getSystemProperty("os.version", false);
        private final String OS_ARCH = getSystemProperty("os.arch", false);
        private final String OS_NAME = getSystemProperty("os.name", false);
        private final boolean IS_OS_AIX = getOSMatches("AIX");
        private final boolean IS_OS_HP_UX = getOSMatches("HP-UX");
        private final boolean IS_OS_IRIX = getOSMatches("Irix");
        private final boolean IS_OS_LINUX = getOSMatches("Linux") || getOSMatches("LINUX");
        private final boolean IS_OS_MAC = getOSMatches("Mac");
        private final boolean IS_OS_MAC_OSX = getOSMatches("Mac OS X");
        private final boolean IS_OS_OS2 = getOSMatches("OS/2");
        private final boolean IS_OS_SOLARIS = getOSMatches("Solaris");
        private final boolean IS_OS_SUN_OS = getOSMatches("SunOS");
        private final boolean IS_OS_WINDOWS = getOSMatches("Windows");
        private final boolean IS_OS_WINDOWS_2000 = getOSMatches("Windows", "5.0");
        private final boolean IS_OS_WINDOWS_95 = getOSMatches("Windows 9", "4.0");
        private final boolean IS_OS_WINDOWS_98 = getOSMatches("Windows 9", "4.1");
        private final boolean IS_OS_WINDOWS_ME = getOSMatches("Windows", "4.9");
        private final boolean IS_OS_WINDOWS_NT = getOSMatches("Windows NT");
        private final boolean IS_OS_WINDOWS_XP = getOSMatches("Windows", "5.1");



        private final String FILE_ENCODING = LocaleUtil.getSystem().getCharset().name();
        private final String FILE_SEPARATOR = getSystemProperty("file.separator", false);
        private final String LINE_SEPARATOR = getSystemProperty("line.separator", false);
        private final String PATH_SEPARATOR = getSystemProperty("path.separator", false);

        private OsInfo() {
        }

        public final String getArch() {
            return OS_ARCH;
        }

        public final String getName() {
            return OS_NAME;
        }

        public final String getVersion() {
            return OS_VERSION;
        }

        public final boolean isAix() {
            return IS_OS_AIX;
        }

        public final boolean isHpUx() {
            return IS_OS_HP_UX;
        }

        public final boolean isIrix() {
            return IS_OS_IRIX;
        }

        public final boolean isLinux() {
            return IS_OS_LINUX;
        }

        public final boolean isMac() {
            return IS_OS_MAC;
        }

        public final boolean isMacOsX() {
            return IS_OS_MAC_OSX;
        }

        public final boolean isOs2() {
            return IS_OS_OS2;
        }

        public final boolean isSolaris() {
            return IS_OS_SOLARIS;
        }

        public final boolean isSunOS() {
            return IS_OS_SUN_OS;
        }

        public final boolean isWindows() {
            return IS_OS_WINDOWS;
        }

        public final boolean isWindows2000() {
            return IS_OS_WINDOWS_2000;
        }

        public final boolean isWindows95() {
            return IS_OS_WINDOWS_95;
        }

        public final boolean isWindows98() {
            return IS_OS_WINDOWS_98;
        }

        public final boolean isWindowsME() {
            return IS_OS_WINDOWS_ME;
        }

        public final boolean isWindowsNT() {
            return IS_OS_WINDOWS_NT;
        }

        public final boolean isWindowsXP() {
            return IS_OS_WINDOWS_XP;
        }

        private final boolean getOSMatches(String osNamePrefix) {
            if (OS_NAME == null) {
                return false;
            }

            return OS_NAME.startsWith(osNamePrefix);
        }

        private final boolean getOSMatches(String osNamePrefix, String osVersionPrefix) {
            if ((OS_NAME == null) || (OS_VERSION == null)) {
                return false;
            }

            return OS_NAME.startsWith(osNamePrefix) && OS_VERSION.startsWith(osVersionPrefix);
        }

        public final String getFileEncoding() {
            return FILE_ENCODING;
        }

        public final String getFileSeparator() {
            return FILE_SEPARATOR;
        }

        public final String getLineSeparator() {
            return LINE_SEPARATOR;
        }

        public final String getPathSeparator() {
            return PATH_SEPARATOR;
        }

        public final String toString() {
            StringBuilder builder = new StringBuilder();

            append(builder, "OS Arch:        ", getArch());
            append(builder, "OS Name:        ", getName());
            append(builder, "OS Version:     ", getVersion());
            append(builder, "File Encoding:  ", getFileEncoding());
            append(builder, "File Separator: ", getFileSeparator());
            append(builder, "Line Separator: ", getLineSeparator());
            append(builder, "Path Separator: ", getPathSeparator());

            return builder.toString();
        }
    }

    public static final class UserInfo {
        private final String USER_NAME = getSystemProperty("user.name", false);
        private final String USER_HOME = getSystemProperty("user.home", false);
        private final String USER_DIR = getSystemProperty("user.dir", false);
        private final String USER_LANGUAGE = getSystemProperty("user.language", false);
        private final String USER_COUNTRY = ((getSystemProperty("user.country", false) == null) ? getSystemProperty(
                "user.region", false) : getSystemProperty("user.country", false));
        private final String JAVA_IO_TMPDIR = getSystemProperty("java.io.tmpdir", false);

        private UserInfo() {
        }

        public final String getName() {
            return USER_NAME;
        }

        public final String getHomeDir() {
            return USER_HOME;
        }

        public final String getCurrentDir() {
            return USER_DIR;
        }

        public final String getTempDir() {
            return JAVA_IO_TMPDIR;
        }

        public final String getLanguage() {
            return USER_LANGUAGE;
        }

        public final String getCountry() {
            return USER_COUNTRY;
        }

        public final String toString() {
            StringBuilder builder = new StringBuilder();

            append(builder, "User Name:        ", getName());
            append(builder, "User Home Dir:    ", getHomeDir());
            append(builder, "User Current Dir: ", getCurrentDir());
            append(builder, "User Temp Dir:    ", getTempDir());
            append(builder, "User Language:    ", getLanguage());
            append(builder, "User Country:     ", getCountry());

            return builder.toString();
        }
    }

    public static final class HostInfo {
        private final String HOST_NAME;
        private final String HOST_ADDRESS;

        private HostInfo() {
            String hostName;
            String hostAddress;

            try {
                InetAddress localhost = InetAddress.getLocalHost();

                hostName = localhost.getHostName();
                hostAddress = localhost.getHostAddress();
            } catch (UnknownHostException e) {
                hostName = "localhost";
                hostAddress = "127.0.0.1";
            }

            HOST_NAME = hostName;
            HOST_ADDRESS = hostAddress;
        }

        public final String getName() {
            return HOST_NAME;
        }

        public final String getAddress() {
            return HOST_ADDRESS;
        }

        public final String toString() {
            StringBuilder builder = new StringBuilder();

            append(builder, "Host Name:    ", getName());
            append(builder, "Host Address: ", getAddress());

            return builder.toString();
        }
    }

    public static final void dumpSystemInfo() {
        dumpSystemInfo(new PrintWriter(System.out));
    }

    public static final void dumpSystemInfo(PrintWriter out) {
        out.println("--------------");
        out.println(getJvmSpecInfo());
        out.println("--------------");
        out.println(getJvmInfo());
        out.println("--------------");
        out.println(getJavaSpecInfo());
        out.println("--------------");
        out.println(getJavaInfo());
        out.println("--------------");
        out.println(getJavaRuntimeInfo());
        out.println("--------------");
        out.println(getOsInfo());
        out.println("--------------");
        out.println(getUserInfo());
        out.println("--------------");
        out.println(getHostInfo());
        out.println("--------------");
        out.flush();
    }

    private static String getSystemProperty(String name, boolean quiet) {
        try {
            return System.getProperty(name);
        } catch (SecurityException e) {
            if (!quiet) {
                System.err.println("Caught a SecurityException reading the system property '" + name
                        + "'; the SystemUtil property value will default to null.");
            }

            return null;
        }
    }

    private static void append(StringBuilder builder, String caption, String value) {
        builder.append(caption).append(StringUtil.defaultIfNull(StringEscapeUtil.escapeJava(value), "[n/a]"))
                .append("\n");
    }

}
