package com.bpm202.SensorProject.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bpm202.SensorProject.R;

public class AppPreferences {
    public static final String KEY_AUTO_LOGIN = "key_auto_login";
    public static final String KEY_TOKEN = "key_token";
    public static final String KEY_REGION = "key_region";

    private final String APP_NAME;
    private Context mContext;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    @SuppressLint("WrongConstant")
    public AppPreferences(Context context) {
        this.mContext = context;
        this.APP_NAME = context.getString(R.string.app_name);
        this.pref = context.getSharedPreferences(APP_NAME, Context.MODE_APPEND | Context.MODE_MULTI_PROCESS);
        this.prefEditor = pref.edit();
    }

    public String getStringPref(String key) {
        return pref.getString(key, "");
    }

    public void setStringPref(@NonNull String key, @Nullable String value) {
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    public void setStringPref(@NonNull String key, @Nullable boolean value) {
        setStringPref(key, String.valueOf(value));
    }
}
