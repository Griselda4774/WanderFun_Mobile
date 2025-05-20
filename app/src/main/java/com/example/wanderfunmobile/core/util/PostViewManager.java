package com.example.wanderfunmobile.core.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PostViewManager {
    private static final String CURSOR_KEY = "cursor";

    private static final String PREF_NAME = "PostView";
    private static PostViewManager instance;
    private final SharedPreferences sharedPreferences;

    private PostViewManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PostViewManager getInstance(Context context) {
        if (instance == null) {
            instance = new PostViewManager(context);
        }
        return instance;
    }

    public void init(Long cursor) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(CURSOR_KEY, cursor);
        editor.apply();
    }

    public void reset() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CURSOR_KEY);
        editor.apply();
    }

    public void setCursor(Long cursor) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(CURSOR_KEY, cursor);
        editor.apply();
    }

    public Long getCursor() {
        return sharedPreferences.getLong(CURSOR_KEY, -1);
    }
}
