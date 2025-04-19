package com.example.kukuafya;

import android.content.Context;
import android.content.SharedPreferences;

public class Userinfo {
    private static final String PREFS_NAME = "logininfo";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STATUS = "status";

    private final Context context;

    public Userinfo(Context context) {
        this.context = context;
    }

    public void saveUserInfo(String username, String email) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_STATUS, "logged_in");
        editor.apply();
    }

    public String getUsername() {
        return getPrefs().getString(KEY_USERNAME, "");
    }

    public String getEmail() {
        return getPrefs().getString(KEY_EMAIL, "");
    }

    public boolean isLoggedIn() {
        return "logged_in".equals(getPrefs().getString(KEY_STATUS, ""));
    }

    public void clearUserInfo() {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.clear();
        editor.apply();
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}