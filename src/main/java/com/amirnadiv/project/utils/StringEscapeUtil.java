package com.amirnadiv.project.utils.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import com.amirnadiv.project.utils.common.i18n.LocaleUtil;
import com.amirnadiv.project.utils.common.internal.Entities;

public abstract class StringEscapeUtil {

    // ==========================================================================
    // Java和JavaScript。
    // ==========================================================================

    public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false, false);
    }

    public static String escapeJava(String str, boolean strict) {
        return escapeJavaStyleString(str, false, strict);
    }

    public static void escapeJava(String str, Appendable out) throws IOException {
        escapeJavaStyleString(str, false, out, false);
    }

    public static void escapeJava(String str, Appendable out, boolean strict) throws IOException {
        escapeJavaStyleString(str, false, out, strict);
    }

    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true, false);
    }

    public static String escapeJavaScript(String str, boolean strict) {
        return escapeJavaStyleString(str, true, strict);
    }

    public static void escapeJavaScript(String str, Appendable out) throws IOException {
        escapeJavaStyleString(str, true, out, false);
    }

    public static void escapeJavaScript(String str, Appendable out, boolean strict) throws IOException {
        escapeJavaStyleString(str, true, out, strict);
    }

    private static String escapeJavaStyleString(String str, boolean javascript, boolean strict) {
        if (str == null) {
            return null;
        }

        try {
            StringBuilder out = new StringBuilder(str.length() * 2);

            if (escapeJavaStyleString(str, javascript, out, strict)) {
                return out.toString();
            }

            return str;
        } catch (IOException e) {
            return str; // StringBuilder不可能发生这个异常
        }
    }

    private static boolean escapeJavaStyleString(String str, boolean javascript, Appendable out, boolean strict)
            throws IOException {
        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Appendable must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);

            if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.append('\\');
                        out.append('b');
                        break;

                    case '\n':
                        out.append('\\');
                        out.append('n');
                        break;

                    case '\t':
                        out.append('\\');
                        out.append('t');
                        break;

                    case '\f':
                        out.append('\\');
                        out.append('f');
                        break;

                    case '\r':
                        out.append('\\');
                        out.append('r');
                        break;

                    default:

                        if (ch > 0xf) {
                            out.append("\\u00" + Integer.toHexString(ch).toUpperCase());
                        } else {
                            out.append("\\u000" + Integer.toHexString(ch).toUpperCase());
                        }

                        break;
                }

                // 设置改变标志
                needToChange = true;
            } else if (strict && ch > 0xff) {
                if (ch > 0xfff) {
                    out.append("\\u").append(Integer.toHexString(ch).toUpperCase());
                } else {
                    out.append("\\u0").append(Integer.toHexString(ch).toUpperCase());
                }

                // 设置改变标志
                needToChange = true;
            } else {
                switch (ch) {
                    case '\'':
                    case '/': // 注意：对于javascript，对/进行escape是重要的安全措施。

                        if (javascript) {
                            out.append('\\');

                            // 设置改变标志
                            needToChange = true;
                        }

                        out.append(ch);

                        break;

                    case '"':
                        out.append('\\');
                        out.append('"');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case '\\':
                        out.append('\\');
                        out.append('\\');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    default:
                        out.append(ch);
                        break;
                }
            }
        }

        return needToChange;
    }

    public static String unescapeJava(String str) {
        return unescapeJavaStyleString(str);
    }

    public static void unescapeJava(String str, Appendable out) throws IOException {
        unescapeJavaStyleString(str, out);
    }

    public static String unescapeJavaScript(String str) {
        return unescapeJavaStyleString(str);
    }

    public static void unescapeJavaScript(String str, Appendable out) throws IOException {
        unescapeJavaStyleString(str, out);
    }

    private static String unescapeJavaStyleString(String str) {
        if (str == null) {
            return null;
        }

        try {
            StringBuilder out = new StringBuilder(str.length());

            if (unescapeJavaStyleString(str, out)) {
                return out.toString();
            }

            return str;
        } catch (IOException e) {
            return str; // StringBuilder不可能发生这个异常
        }
    }

    private static boolean unescapeJavaStyleString(String str, Appendable out) throws IOException {
        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Appendable must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        int length = str.length();
        StringBuilder unicode = new StringBuilder(4);
        boolean hadSlash = false;
        boolean inUnicode = false;

        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);

            if (inUnicode) {
                unicode.append(ch);

                if (unicode.length() == 4) {
                    String unicodeStr = unicode.toString();

                    try {
                        int value = Integer.parseInt(unicodeStr, 16);

                        out.append((char) value);
                        unicode.setLength(0);
                        inUnicode = false;
                        hadSlash = false;

                        // 设置改变标志
                        needToChange = true;
                    } catch (NumberFormatException e) {
                        out.append("\\u" + unicodeStr);
                    }
                }

                continue;
            }

            if (hadSlash) {
                hadSlash = false;

                switch (ch) {
                    case '\\':
                        out.append('\\');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case '\'':
                        out.append('\'');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case '\"':
                        out.append('"');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case 'r':
                        out.append('\r');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case 'f':
                        out.append('\f');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case 't':
                        out.append('\t');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case 'n':
                        out.append('\n');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case 'b':
                        out.append('\b');

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case 'u': {
                        inUnicode = true;
                        break;
                    }

                    default:
                        out.append(ch);
                        break;
                }

                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }

            out.append(ch);
        }

        if (hadSlash) {
            out.append('\\');
        }

        return needToChange;
    }

    // ==========================================================================
    // HTML和XML。
    // ==========================================================================

    public static String escapeHtml(String str) {
        return escapeEntities(Entities.HTML40_MODIFIED, str);
    }

    public static void escapeHtml(String str, Appendable out) throws IOException {
        escapeEntities(Entities.HTML40_MODIFIED, str, out);
    }

    public static String escapeXml(String str) {
        return escapeEntities(Entities.XML, str);
    }

    public static void escapeXml(String str, Appendable out) throws IOException {
        escapeEntities(Entities.XML, str, out);
    }

    public static String escapeEntities(Entities entities, String str) {
        if (str == null) {
            return null;
        }

        try {
            StringBuilder out = new StringBuilder(str.length());

            if (escapeEntitiesInternal(entities, str, out)) {
                return out.toString();
            }

            return str;
        } catch (IOException e) {
            return str; // StringBuilder不可能发生这个异常
        }
    }

    public static void escapeEntities(Entities entities, String str, Appendable out) throws IOException {
        escapeEntitiesInternal(entities, str, out);
    }

    public static String unescapeHtml(String str) {
        return unescapeEntities(Entities.HTML40, str);
    }

    public static void unescapeHtml(String str, Appendable out) throws IOException {
        unescapeEntities(Entities.HTML40, str, out);
    }

    public static String unescapeXml(String str) {
        return unescapeEntities(Entities.XML, str);
    }

    public static void unescapeXml(String str, Appendable out) throws IOException {
        unescapeEntities(Entities.XML, str, out);
    }

    public static String unescapeEntities(Entities entities, String str) {
        if (str == null) {
            return null;
        }

        try {
            StringBuilder out = new StringBuilder(str.length());

            if (unescapeEntitiesInternal(entities, str, out)) {
                return out.toString();
            }

            return str;
        } catch (IOException e) {
            return str; // StringBuilder不可能发生这个异常
        }
    }

    public static void unescapeEntities(Entities entities, String str, Appendable out) throws IOException {
        unescapeEntitiesInternal(entities, str, out);
    }

    private static boolean escapeEntitiesInternal(Entities entities, String str, Appendable out) throws IOException {
        boolean needToChange = false;

        if (entities == null) {
            throw new IllegalArgumentException("The Entities must not be null");
        }

        if (out == null) {
            throw new IllegalArgumentException("The Appendable must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            String entityName = entities.getEntityName(ch);

            if (entityName == null) {
                out.append(ch);
            } else {
                out.append('&');
                out.append(entityName);
                out.append(';');

                // 设置改变标志
                needToChange = true;
            }
        }

        return needToChange;
    }

    private static boolean unescapeEntitiesInternal(Entities entities, String str, Appendable out) throws IOException {
        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Appendable must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);

            if (ch == '&') {
                // 查找&xxxx;
                int semi = str.indexOf(';', i + 1);

                if (semi == -1 || i + 1 >= semi - 1) {
                    out.append(ch);
                    continue;
                }

                // 如果是&#xxxxx;
                if (str.charAt(i + 1) == '#') {
                    int firstCharIndex = i + 2;
                    int radix = 10;

                    if (firstCharIndex >= semi - 1) {
                        out.append(ch);
                        out.append('#');
                        i++;
                        continue;
                    }

                    char firstChar = str.charAt(firstCharIndex);

                    if (firstChar == 'x' || firstChar == 'X') {
                        firstCharIndex++;
                        radix = 16;

                        if (firstCharIndex >= semi - 1) {
                            out.append(ch);
                            out.append('#');
                            i++;
                            continue;
                        }
                    }

                    try {
                        int entityValue = Integer.parseInt(str.substring(firstCharIndex, semi), radix);

                        out.append((char) entityValue);

                        // 设置改变标志
                        needToChange = true;
                    } catch (NumberFormatException e) {
                        out.append(ch);
                        out.append('#');
                        i++;
                        continue;
                    }
                } else {
                    String entityName = str.substring(i + 1, semi);
                    int entityValue = -1;

                    if (entities != null) {
                        entityValue = entities.getEntityValue(entityName);
                    }

                    if (entityValue == -1) {
                        out.append('&');
                        out.append(entityName);
                        out.append(';');
                    } else {
                        out.append((char) entityValue);

                        // 设置改变标志
                        needToChange = true;
                    }
                }

                i = semi;
            } else {
                out.append(ch);
            }
        }

        return needToChange;
    }

    // ==========================================================================
    // SQL语句。
    // ==========================================================================

    public static String escapeSql(String str) {
        return StringUtil.replace(str, "'", "''");
    }

    public static void escapeSql(String str, Appendable out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Appendable must not be null");
        }

        String result = StringUtil.replace(str, "'", "''");

        if (result != null) {
            out.append(result);
        }
    }

    // ==========================================================================
    // URL/URI encoding/decoding。
    // 根据RFC2396：http://www.ietf.org/rfc/rfc2396.txt
    // ==========================================================================

    private static final BitSet ALPHA = new BitSet(256);

    static {
        for (int i = 'a'; i <= 'z'; i++) {
            ALPHA.set(i);
        }

        for (int i = 'A'; i <= 'Z'; i++) {
            ALPHA.set(i);
        }
    }

    private static final BitSet ALPHANUM = new BitSet(256);

    static {
        ALPHANUM.or(ALPHA);

        for (int i = '0'; i <= '9'; i++) {
            ALPHANUM.set(i);
        }
    }

    private static final BitSet MARK = new BitSet(256);

    static {
        MARK.set('-');
        MARK.set('_');
        MARK.set('.');
        MARK.set('!');
        MARK.set('~');
        MARK.set('*');
        MARK.set('\'');
        MARK.set('(');
        MARK.set(')');
    }

    private static final BitSet RESERVED = new BitSet(256);

    static {
        RESERVED.set(';');
        RESERVED.set('/');
        RESERVED.set('?');
        RESERVED.set(':');
        RESERVED.set('@');
        RESERVED.set('&');
        RESERVED.set('=');
        RESERVED.set('+');
        RESERVED.set('$');
        RESERVED.set(',');
    }

    private static final BitSet UNRESERVED = new BitSet(256);

    static {
        UNRESERVED.or(ALPHANUM);
        UNRESERVED.or(MARK);
    }

    private static char[] HEXADECIMAL = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F' };

    public static String escapeURL(String str) {
        try {
            return escapeURLInternal(str, null, true);
        } catch (UnsupportedEncodingException e) {
            return str; // 不可能发生这个异常
        }
    }

    public static String escapeURL(String str, String encoding) throws UnsupportedEncodingException {
        return escapeURLInternal(str, encoding, true);
    }

    public static String escapeURL(String str, String encoding, boolean strict) throws UnsupportedEncodingException {
        return escapeURLInternal(str, encoding, strict);
    }

    public static void escapeURL(String str, String encoding, Appendable out) throws IOException {
        escapeURLInternal(str, encoding, out, true);
    }

    public static void escapeURL(String str, String encoding, Appendable out, boolean strict) throws IOException {
        escapeURLInternal(str, encoding, out, strict);
    }

    private static String escapeURLInternal(String str, String encoding, boolean strict)
            throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }

        try {
            StringBuilder out = new StringBuilder(64);

            if (escapeURLInternal(str, encoding, out, strict)) {
                return out.toString();
            }

            return str;
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {
            return str; // StringBuilder不可能发生这个异常
        }
    }

    private static boolean escapeURLInternal(String str, String encoding, Appendable out, boolean strict)
            throws IOException {
        if (encoding == null) {
            encoding = LocaleUtil.getContext().getCharset().name();
        }

        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Appendable must not be null");
        }

        if (str == null) {
            return needToChange;
        }

        char[] charArray = str.toCharArray();
        int length = charArray.length;

        for (int i = 0; i < length; i++) {
            int ch = charArray[i];

            if (isSafeCharacter(ch, strict)) {
                // “安全”的字符，直接输出
                out.append((char) ch);
            } else if (ch == ' ') {
                // 特殊情况：空格（0x20）转换成'+'
                out.append('+');

                // 设置改变标志
                needToChange = true;
            } else {
                // 对ch进行URL编码。
                // 首先按指定encoding取得该字符的字节码。
                byte[] bytes = String.valueOf((char) ch).getBytes(encoding);

                for (byte toEscape : bytes) {
                    out.append('%');

                    int low = toEscape & 0x0F;
                    int high = (toEscape & 0xF0) >> 4;

                    out.append(HEXADECIMAL[high]);
                    out.append(HEXADECIMAL[low]);
                }

                // 设置改变标志
                needToChange = true;
            }
        }

        return needToChange;
    }

    private static boolean isSafeCharacter(int ch, boolean strict) {
        if (strict) {
            return UNRESERVED.get(ch);
        }
        return ch > ' ' && !RESERVED.get(ch) && !Character.isWhitespace((char) ch);
    }

    public static String unescapeURL(String str) {
        try {
            return unescapeURLInternal(str, null);
        } catch (UnsupportedEncodingException e) {
            return str; // 不可能发生这个异常
        }
    }

    public static String unescapeURL(String str, String encoding) throws UnsupportedEncodingException {
        return unescapeURLInternal(str, encoding);
    }

    public static void unescapeURL(String str, String encoding, Appendable out) throws IOException {
        unescapeURLInternal(str, encoding, out);
    }

    private static String unescapeURLInternal(String str, String encoding) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }

        try {
            StringBuilder out = new StringBuilder(str.length());

            if (unescapeURLInternal(str, encoding, out)) {
                return out.toString();
            }

            return str;
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {
            return str; // StringBuilder不可能发生这个异常
        }
    }

    private static boolean unescapeURLInternal(String str, String encoding, Appendable out) throws IOException {
        if (encoding == null) {
            encoding = LocaleUtil.getContext().getCharset().name();
        }

        boolean needToChange = false;

        if (out == null) {
            throw new IllegalArgumentException("The Appendable must not be null");
        }

        byte[] buffer = null;
        int pos = 0;
        int startIndex = 0;

        char[] charArray = str.toCharArray();
        int length = charArray.length;

        for (int i = 0; i < length; i++) {
            int ch = charArray[i];

            if (ch < 256) {
                // 读取连续的字节，并将它按指定编码转换成字符。
                if (buffer == null) {
                    buffer = new byte[length - i]; // 最长只需要length - i
                }

                if (pos == 0) {
                    startIndex = i;
                }

                switch (ch) {
                    case '+':

                        // 将'+'转换成' '
                        buffer[pos++] = ' ';

                        // 设置改变标志
                        needToChange = true;
                        break;

                    case '%':

                        if (i + 2 < length) {
                            try {
                                byte b = (byte) Integer.parseInt(str.substring(i + 1, i + 3), 16);

                                buffer[pos++] = b;
                                i += 2;

                                // 设置改变标志
                                needToChange = true;
                            } catch (NumberFormatException e) {
                                // 如果%xx不是合法的16进制数，则原样输出
                                buffer[pos++] = (byte) ch;
                            }
                        } else {
                            buffer[pos++] = (byte) ch;
                        }

                        break;

                    default:

                        // 写到bytes中，到时一起输出。
                        buffer[pos++] = (byte) ch;
                        break;
                }
                continue;
            }
            // 先将buffer中的字节串转换成字符串。
            if (pos > 0) {
                String s = new String(buffer, 0, pos, encoding);

                out.append(s);

                if (!needToChange && !s.equals(new String(charArray, startIndex, pos))) {
                    needToChange = true;
                }

                pos = 0;
            }

            // 如果ch是ISO-8859-1以外的字符，直接输出即可
            out.append((char) ch);
        }

        // 先将buffer中的字节串转换成字符串。
        if (pos > 0) {
            String s = new String(buffer, 0, pos, encoding);

            out.append(s);

            if (!needToChange && !s.equals(new String(charArray, startIndex, pos))) {
                needToChange = true;
            }

            pos = 0;
        }

        return needToChange;
    }
}
