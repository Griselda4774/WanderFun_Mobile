package com.example.wanderfunmobile.infrastructure.util;

import android.content.Context;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryUtil {
    public static void init(Context context) {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "justvvu");
        config.put("secure", true);
        MediaManager.init(context, config);
    }
}
