package com.xxl.sayhello.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String PREF_NAME = "sayhello_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NICKNAME = "nickname";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isLoggedIn(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        getSharedPreferences(context).edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply();
    }

    public static void saveToken(Context context, String token) {
        getSharedPreferences(context).edit().putString(KEY_TOKEN, token).apply();
    }

    public static String getToken(Context context) {
        return getSharedPreferences(context).getString(KEY_TOKEN, "");
    }

    public static void saveUserInfo(Context context, String userId, String username, String nickname) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NICKNAME, nickname);
        editor.apply();
    }

    public static String getUsername(Context context) {
        return getSharedPreferences(context).getString(KEY_USERNAME, "");
    }

    public static String getNickname(Context context) {
        return getSharedPreferences(context).getString(KEY_NICKNAME, "");
    }

    public static void clearUserData(Context context) {
        getSharedPreferences(context).edit().clear().apply();
    }
}