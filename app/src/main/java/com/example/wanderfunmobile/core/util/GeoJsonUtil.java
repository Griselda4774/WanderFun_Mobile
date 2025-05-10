package com.example.wanderfunmobile.core.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class GeoJsonUtil {
    public static String loadGeoJsonFromAsset(Context context, String filename) {
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
