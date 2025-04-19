package com.example.wanderfunmobile.core.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "UserSession";
    private static SessionManager instance;
    private final SharedPreferences sharedPreferences;

    private SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void login(Long id, String email, String role, String tokenType, String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("id", id);
        editor.putString("email", email);
        editor.putString("role", role);
        editor.putString("tokenType", tokenType);
        editor.putString("accessToken", accessToken);
        editor.putString("refreshToken", refreshToken);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("id");
        editor.remove("email");
        editor.remove("role");
        editor.remove("tokenType");
        editor.remove("accessToken");
        editor.remove("refreshToken");
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }

    public void refresh(String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", accessToken);
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }

    public Long getUserId() {
        return sharedPreferences.getLong("id", -1);
    }

    public String getUserEmail() {
        return sharedPreferences.getString("email", null);
    }

    public String getUserRole() {
        return sharedPreferences.getString("role", null);
    }

    public String getTokenType() {
        return sharedPreferences.getString("tokenType", null);
    }

    public String getAccessToken() {
        return sharedPreferences.getString("accessToken", null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString("refreshToken", null);
    }

    public Boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}
