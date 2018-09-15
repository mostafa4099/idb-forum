package info.javaknowledge.idbforum.util;

/**
 * Created by User on 7/31/2018.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import info.javaknowledge.idbforum.LoginActivity;
import info.javaknowledge.idbforum.MainActivity;


public class UserSessionManager {
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    Editor editor;
    // Context
    Context context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREFER_NAME = "idb_forum";
    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ROLE = "role";
    // Constructor
    public UserSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String role, String name) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
        // Storing role in pref
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_NAME, name);
        // commit changes
        editor.commit();
    }

    //Create email
    public void createEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        // commit changes
        editor.commit();
    }

    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {
            return true;
        }
        return false;
    }


    /**
     * Get stored session data
     */
    public String getUserRole() {
        return pref.getString(KEY_ROLE, null);
    }
    public String getUserName() {
        return pref.getString(KEY_NAME, null);
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all user data from Shared Preferences
        if (this.isUserLoggedIn()) {
            editor.clear();
            editor.commit();
        }
//
        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
