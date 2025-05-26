package com.example.wanderfunmobile.core.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String ACCOUNT_ID_KEY = "id";
    private static final String USER_ID_KEY = "userId";
    private static final String ROLE_KEY = "role";
    private static final String TOKEN_TYPE_KEY = "tokenType";
    private static final String ACCESS_TOKEN_KEY = "accessToken";
    private static final String REFRESH_TOKEN_KEY = "refreshToken";
    private static final String IS_LOGGED_IN_KEY = "isLoggedIn";

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

    public void login(Long id, Long userId, String role, String tokenType, String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(ACCOUNT_ID_KEY, id);
        editor.putLong(USER_ID_KEY, userId);
        editor.putString(ROLE_KEY, role);
        editor.putString(TOKEN_TYPE_KEY, tokenType);
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.putBoolean(IS_LOGGED_IN_KEY, true);
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCOUNT_ID_KEY);
        editor.remove(USER_ID_KEY);
        editor.remove(ROLE_KEY);
        editor.remove(TOKEN_TYPE_KEY);
        editor.remove(ACCESS_TOKEN_KEY);
        editor.remove(REFRESH_TOKEN_KEY);
        editor.putBoolean(IS_LOGGED_IN_KEY, false);
        editor.apply();
    }

    public void refresh(String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.apply();
    }

    public Long getAccountId() {
        return sharedPreferences.getLong(ACCOUNT_ID_KEY, -1);
    }

    public Long getUserId() {
        return sharedPreferences.getLong(USER_ID_KEY, -1);
    }

    public String getRole() {
        return sharedPreferences.getString(ROLE_KEY, null);
    }

    public String getTokenType() {
        return sharedPreferences.getString(TOKEN_TYPE_KEY, null);
    }

    public String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null);
    }

    public Boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN_KEY, false);
    }
}
