package com.bpm.bpm_ver4.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bpm.bpm_ver4.R;

public class AppPref {

    public static final String KEY_TOKEN = "key_token";
    public static final String KEY_REGION = "key_region";

    private final String APP_NAME;
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;
    private Context mContext;


    public AppPref(Context context) {
        this.mContext = context;
        this.APP_NAME = context.getString(R.string.app_name);
        this.pref = context.getSharedPreferences(APP_NAME, Context.MODE_APPEND | Context.MODE_MULTI_PROCESS);
        this.prefEditor = pref.edit();
    }


    public Context getmContext() {
        return mContext;
    }

    public void setStringPref(String key, String value) {
        prefEditor.putString(key, value);
        prefEditor.commit();
    }


    public String getStringPref(String key) {
        return pref.getString(key, "");
    }

    public void setIntegerPref(String key, int value) {
        prefEditor.putInt(key, value);
        prefEditor.commit();
    }

    public int getIntegerPref(String key) {
        return pref.getInt(key, -1);
    }


    public void removeAppPref(String key) {
        prefEditor.remove(key);
        prefEditor.commit();
    }

    public void clearPref() {
        prefEditor.clear();
        prefEditor.commit();
    }


    public void setBoolean(String key, Boolean value) {
        prefEditor.putBoolean(key, value);
        prefEditor.commit();
    }


    public Boolean getBooleanValuePref(String key) {
        return pref.getBoolean(key, false);
    }
}
