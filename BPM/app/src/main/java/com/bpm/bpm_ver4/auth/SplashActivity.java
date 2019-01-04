package com.bpm.bpm_ver4.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.auth.login.LoginActivity;
import com.bpm.bpm_ver4.exercise.MainActivity;
import com.bpm.bpm_ver4.util.AppPref;
import com.bpm.bpm_ver4.util.Permission;

public class SplashActivity extends Activity implements View.OnTouchListener, SplashContract.View {


    private SplashContract.Presenter mPresenter;
    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashPresenter = new SplashPresenter(this);
        findViewById(R.id.content_layer).setOnTouchListener(this);

    }

    private void autoLogin() {
        String token = new AppPref(this).getStringPref(AppPref.KEY_TOKEN);
        if (!token.isEmpty()) {
            mPresenter.autoLogin(token);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Permission.permissionAll(this)) {
            autoLogin();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Permission.PERMISSION_ALL_REQUEST_CODE) {

            for (int grant : grantResults) {
                if (grant == PackageManager.PERMISSION_DENIED) {
                    // TODO: 2018-08-20 : 거부 했을때 액션
                }
            }

            // 자동 로그인 시도
            autoLogin();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return false;
    }

    @Override
    public void loginSuccessView() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void loginFailureView() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }



}
