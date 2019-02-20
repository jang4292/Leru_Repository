package com.bpm202.SensorProject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.bpm202.SensorProject.Account.LoginActivity;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.Data.SignInDataSource;
import com.bpm202.SensorProject.Data.SignInRepository;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.ValueObject.MemberObj;

public class SplashActivity extends AppCompatActivity {

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

    private static final boolean isTest = false;
    private static final int DelayTime = 3000;

    private void initView() {
        setContentView(R.layout.activity_splash);
        if (isTest) {
            content_layer = findViewById(R.id.content_layer);
        } else {
            new TimerHandler().sendEmptyMessageDelayed(0, DelayTime);
        }
    }

    private class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();

        }
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
