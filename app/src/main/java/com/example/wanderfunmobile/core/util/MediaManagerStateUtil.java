package com.example.wanderfunmobile.core.util;

public class MediaManagerStateUtil {
    private static boolean isInitialized = false;

    public static boolean isInitialized() {
        return isInitialized;
    }

    public static void initialize() {
        isInitialized = true;
    }

    public static void reset() {
        isInitialized = false;
    }
}
