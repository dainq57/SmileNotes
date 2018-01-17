package com.example.dainq.smilenotes.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.dainq.smilenotes.model.request.UserRequest;
import com.example.dainq.smilenotes.ui.LoginActivity;

public class SessionManager {
    // Shared Preferences
    SharedPreferences mPref;
    // Editor for Shared preferences
    SharedPreferences.Editor mEditor;
    // Context
    Context mContext;
    // Shared mPref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "pref_user_session";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "user_isLogged";
    // UserRequest name (make variable public to access from outside)
    public static final String KEY_NAME = "user_name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "user_email";
    // id (make variable public to access from outside)
    public static final String KEY_ID = "user_id";
    // token (make variable public to access from outside)
    public static final String KEY_TOKEN = "user_token";
    // password (make variable public to access from outside)
    public static final String KEY_PASSWORD = "user_password";
    // version (make variable public to access from outside)
    public static final String KEY_VERSION = "user_version";

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.mContext = context;
        mPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String name, String email, String id, String token, String password, int version) {
        // Storing login value as TRUE
        mEditor.putBoolean(IS_LOGIN, true);

        // Storing name in mPref
        mEditor.putString(KEY_NAME, name);

        // Storing email in mPref
        mEditor.putString(KEY_EMAIL, email);

        // Storing id in mPref
        mEditor.putString(KEY_ID, id);

        // Storing token in mPref
        mEditor.putString(KEY_TOKEN, token);

        // Storing pass in mPref
        mEditor.putString(KEY_PASSWORD, password);

        // Storing version in mPref
        mEditor.putInt(KEY_VERSION, version);

        // commit changes
        mEditor.commit();
    }

    public void updateSession(String key, String value, int version){
        //update version to session
        mEditor.putInt(KEY_VERSION, version);

        //update value need to update into session
        mEditor.putString(key, value);

        //commit change
        mEditor.commit();
    }

    /**
     * Check login method wil check user login status If false it will redirect
     * user to login page Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        //TODO check token is here
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(mContext, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Add new Flag to start new Activity
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Staring Login Activity
            mContext.startActivity(i);
        }
    }

    /**
     * Get stored session data
     */
    public UserRequest getUserDetails() {
        UserRequest userRequest = new UserRequest();
        // userRequest name
        userRequest.setFullName(mPref.getString(KEY_NAME, null));

        // userRequest email
        userRequest.setEmail(mPref.getString(KEY_EMAIL, null));

        // userRequest id
        userRequest.setId(mPref.getString(KEY_ID, null));

        //token
        userRequest.setToken(mPref.getString(KEY_TOKEN, null));

        //pawordss
        userRequest.setPassword(mPref.getString(KEY_PASSWORD, null));

        //version
        userRequest.setVersion(mPref.getInt(KEY_VERSION, 0));

        // return userRequest
        return userRequest;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        mEditor.clear();
        mEditor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(mContext, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Add new Flag to start new Activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        mContext.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return mPref.getBoolean(IS_LOGIN, false);
    }
}