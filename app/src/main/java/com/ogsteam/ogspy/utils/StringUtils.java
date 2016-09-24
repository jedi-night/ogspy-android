package com.ogsteam.ogspy.utils;

import java.text.MessageFormat;
import java.util.Locale;

public class StringUtils {

    public static String formatPattern(String message, String... parameters) {
        if (message == null) {
            return null;
        } else {
            MessageFormat formatter = new MessageFormat(message.replace("'", "''"), Locale.FRANCE);
            return formatter.format(parameters);
        }
    }
}
