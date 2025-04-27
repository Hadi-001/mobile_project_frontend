package com.example.mobile_project_frontend;

import android.content.Context;
import android.content.SharedPreferences;

public class User {

    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_LOGGED_IN = "logged_in";
    private SharedPreferences sharedPreferences;

    public User(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Store the user_id after login
    public void setUserId(int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.putBoolean(KEY_LOGGED_IN, true);  // Set login status to true
        editor.apply();
    }


    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }


    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }


    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_LOGGED_IN);
        editor.apply();
    }
}
