package com.ogsteam.ogspy.utils;

import java.text.DecimalFormat;

public class NumberUtils {

    public static String format(float number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number);
    }
}
