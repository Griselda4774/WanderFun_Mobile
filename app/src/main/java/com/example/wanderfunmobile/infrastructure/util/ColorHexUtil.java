package com.example.wanderfunmobile.infrastructure.util;

public class ColorHexUtil {
    public static String getColorHexString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
