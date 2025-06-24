package com.example.wanderfunmobile.core.util;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {
    public static String formatNumberWithCommas(int number) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        return formatter.format(number);
    }
}
