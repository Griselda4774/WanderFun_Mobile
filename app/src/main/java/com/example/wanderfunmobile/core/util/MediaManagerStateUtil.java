package com.example.wanderfunmobile.core.util;

import com.cloudinary.android.MediaManager;

public class MediaManagerStateUtil {
    private static boolean isInitialized = false;

    public static boolean isInitialized() {
        try {
            isInitialized = MediaManager.get() != null;
        } catch (Exception e) {
            isInitialized = false;
        }
        return isInitialized;
    }

    public static void initialize() {
        isInitialized = true;
    }

    public static void reset() {
        isInitialized = false;
    }
}
