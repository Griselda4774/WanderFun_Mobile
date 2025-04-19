package com.example.wanderfunmobile.core.util;

public class ColorHexUtil {
    public static String getColorHexString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
