package com.bpm202.SensorProject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.bpm202.SensorProject.Account.LoginActivity;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.Data.SignInDataSource;
import com.bpm202.SensorProject.Data.SignInRepository;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.ValueObject.MemberObj;

public class SplashActivity extends BaseActivity {

    public static final String TAG = SignInRepository.class.getSimpleName();

    private LinearLayout content_layer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isAutoLoginCheckedFromPreference()) {
            initView();
            initListener();
        } else {
            String token = new AppPreferences(getApplicationContext()).getStringPref(AppPreferences.KEY_TOKEN);
            if (token.isEmpty()) {
                initView();
                initListener();
            } else {
                SignInRepository.getInstance().signInToken(token, callback);
            }
        }
    }

    private SignInDataSource.SignInCallback callback = new SignInDataSource.SignInCallback() {

        @Override
        public void onResponse(String token, MemberObj memberObj) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onDataNotAvailable() {
            Log.d(TAG, "onDataNotAvailable");
        }
    };

    private boolean isAutoLoginCheckedFromPreference() {
        boolean isAutoLogin = Boolean.parseBoolean(new AppPreferences(getApplicationContext()).getStringPref(AppPreferences.KEY_AUTO_LOGIN));
        return isAutoLogin;
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        content_layer = findViewById(R.id.content_layer);
    }

    private void initListener() {
        if (content_layer != null) {
            content_layer.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                        return true;
                }
                return false;
            });
        }
    }
}
