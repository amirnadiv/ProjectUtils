package com.amirnadiv.project.utils.common;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageUtil {
    public static String getMessage(ResourceBundle bundle, String key, Object...params) {
        if (bundle == null || key == null) {
            return key;
        }

        try {
            return formatMessage(bundle.getString(key), params);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String formatMessage(String message, Object...params) {
        if (message == null || params == null || params.length == 0) {
            return message;
        }

        return MessageFormat.format(message, params);
    }
}
