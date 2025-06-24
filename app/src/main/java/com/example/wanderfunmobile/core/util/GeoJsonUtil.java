package com.example.wanderfunmobile.core.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class GeoJsonUtil {
    public static String loadGeoJsonFromAsset(Context context, String filename) {
        StringBuilder sb = new StringBuilder();

        try (InputStream is = context.getAssets().open(filename);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            return sb.toString();

        } catch (IOException e) {
            Log.e("GeoJsonUtil", "Error loading GeoJson from asset: " + filename, e);
            return null;
        }
    }
}