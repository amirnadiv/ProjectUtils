package com.amirnadiv.project.utils.common;

public interface StringPool {

    interface Symbol {

        String EMPTY = "";
        String SPACE = " ";
        String DOT = ".";
        String DOTDOT = "..";
        String COMMA = ",";
        String COLON = ":";
        String SEMICOLON = ";";
        String NEWLINE = "\n";
        String SLASH = "/";
        String QUESTION = "?";
        String QUOTATION = "'";
        String UNDERLINE = "_";
        String VERTICAL = "|";
        String DOLLARS = "$";
        String RMB = "￥";
        String PERCENT = "%";
        String ZERO = "0";
        String ONE = "1";
        String TEN = "10";
        String NULL = "null";
        String BRACE = "{}";
        String LEFT_BRACE = "{";
        String RIGHT_BRACE = "}";
        String BRACKET = "[]";
        String LEFT_BRACKET = "[";
        String RIGHT_BRACKET = "]";
        String PARENTHESES = "()";
        String LEFT_PARENTHESES = "(";
        String RIGHT_PARENTHESES = ")";
        String DOUBLE_QUOTE = "\"";
        String SINGLE_QUOTE = "\'";
        String EQUALS = "=";
        String HASH = "#";

        // add
        String AMPERSAND = "&";
        String AT = "@";
        String ASTERISK = "*";
        String STAR = ASTERISK;
        String BACK_SLASH = "\\";
        String DASH = "-";
        String DOT_CLASS = ".class";
        String DOT_JAVA = ".java";
        String FALSE = "false";
        String TRUE = "true";
        String HAT = "^";
        String LEFT_CHEV = "<";
        String N = "n";
        String NO = "no";
        String OFF = "off";
        String ON = "on";
        String PIPE = "|";
        String PLUS = "+";
        String QUESTION_MARK = "?";
        String EXCLAMATION_MARK = "!";
        String QUOTE = "\"";
        String RETURN = "\r";
        String TAB = "\t";
        String RIGHT_CHEV = ">";
        String BACKTICK = "`";
        String LEFT_SQ_BRACKET = "[";
        String RIGHT_SQ_BRACKET = "]";
        String UNDERSCORE = "_";
        String UTF_8 = "UTF-8";
        String US_ASCII = "US-ASCII";
        String ISO_8859_1 = "ISO-8859-1";
        String Y = "y";
        String YES = "yes";
        String DOLLAR_LEFT_BRACE = "${";
        String CRLF = "\r\n";

        String HTML_NBSP = "&nbsp;";
        String HTML_AMP = "&amp";
        String HTML_QUOTE = "&quot;";
        String HTML_LT = "&lt;";
        String HTML_GT = "&gt;";
    }

    interface Charset {

        String GBK = "GBK";

        String GB2312 = "GB2312";

        String UTF_8 = "UTF-8";

        String ISO_8859_1 = "ISO-8859-1";

        String US_ASCII = "US-ASCII";

        String UTF_16 = "UTF-16";

        String UTF_16BE = "UTF-16BE";

        String UTF_16LE = "UTF-16LE";

    }

    interface Format {
        String JSON = "json";

        String XML = "xml";

        String CSV = "csv";
    }

    interface Word {
        String N = "n";
        String NO = "no";
        String NULL = "null";
        String OFF = "off";
        String ON = "on";
        String Y = "y";
        String YES = "yes";
        String ONE = "1";
        String ZERO = "0";
        String FALSE = "false";
        String TRUE = "true";
    }

    interface Protocol {

        String HTTP = "http";

        String HTTPS = "https";

        String FTP = "ftp";

        String POP = "pop";

        String SMTP = "smtp";

        String IMAP = "imap";

        String P2P = "p2p";

        String LADP = "ladp";

        String TELNET = "telnet";

        String SSH = "ssh";

    }

}
