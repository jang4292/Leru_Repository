package com.bpm.bpm_ver4.auth.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.auth.account.FindPasswordActivity;
import com.bpm.bpm_ver4.auth.term.TermActivity;
import com.bpm.bpm_ver4.databinding.ActivityLoginBinding;
import com.bpm.bpm_ver4.exercise.MainActivity;
import com.bpm.bpm_ver4.testpack.FakeData;
import com.bpm.bpm_ver4.testpack.TestDefined;
import com.bpm.bpm_ver4.util.LoadingUtil;

public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {

    private ActivityLoginBinding binding;

    private LoginContract.Presenter mPresenter;
    private LoginPresenter mLoginPresenter;
    private long mLastClick = 0;
    private final long CLICK_DELAY = 200;
    private LoadingUtil mLoadingUtil;

    private void test() {
        if (TestDefined.loginActivity) {
            binding.etEmail.setText("a@a.a");
            binding.etPw.setText("1111");
//            binding.signBtn.callOnClick();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        // presenter
        mLoginPresenter = new LoginPresenter(this);

        // setClickListener
        binding.googleSignBtn.setOnClickListener(this);
        binding.naverSignBtn.setOnClickListener(this);
        binding.kakaoSignBtn.setOnClickListener(this);
        binding.signBtn.setOnClickListener(this);
        binding.tvFindPw.setOnClickListener(this);
        binding.loginBtn.setOnClickListener(this);

        // progressBar
        mLoadingUtil = new LoadingUtil(this);

        test();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.googleLoginActivityResultValue(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void loginSuccessView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMessage(String errorMessage) {
        showToast(errorMessage);
    }

    @Override
    public void showMessage(int errorMessage) {
        showToast(errorMessage);
    }

    @Override
    public void showLoading(boolean visible) {
        if (visible)
            mLoadingUtil.show();
        else
            mLoadingUtil.dismiss();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        /* 클릭 딜레이 200ms 연속 클릭시 여기까지 실행되고 RETURN */
        if (mLastClick > System.currentTimeMillis() - CLICK_DELAY) {
            mLastClick = System.currentTimeMillis();
            return; // RETURN;
        }

        switch (v.getId()) {
            case R.id.sign_btn :
                startActivity(new Intent(this, TermActivity.class));
                break;

            case R.id.tv_find_pw :
                startActivity(new Intent(this, FindPasswordActivity.class));
                break;

            case R.id.login_btn :
                if (FakeData.TEST) {
                    mLoginPresenter.testLogin();
                    return;
                }
                mPresenter.emailLogin(
                        binding.etEmail.getText().toString().trim(),
                        binding.etPw.getText().toString().trim()
                );

                break;

            case R.id.google_sign_btn :
                mPresenter.googleLogin(this);
                break;

            case R.id.naver_sign_btn :
                mPresenter.naverLogin(this);
                break;

            case R.id.kakao_sign_btn :
                mPresenter.kakaoLogin(this);
                break;
        }
    }

}
