package com.bpm.bpm_ver4;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bpm.bpm_ver4.util.AppPref;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Fabric.with(this, new Crashlytics());
    }

    public String getToken() {
        return new AppPref(this).getStringPref(AppPref.KEY_TOKEN);
    }

    public void showToast(int message) {
        Toast.makeText(getApplicationContext(), getString(message), Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
