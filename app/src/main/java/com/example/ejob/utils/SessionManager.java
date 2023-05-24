package com.example.ejob.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY1 = "session_user";
    String SESSION_KEY2 = "session_employer";
    String SESSION_KEY3 = "session_admin";
    int accessLevel = 1;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
    }

    public void saveSession(FirebaseUser user) {
        String uid = user.getUid();
        sharedEditor.putString(SESSION_KEY1, uid).commit();
    }

}
