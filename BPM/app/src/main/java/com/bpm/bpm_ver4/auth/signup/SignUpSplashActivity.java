package com.bpm.bpm_ver4.auth.signup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;

import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.databinding.ActivitySignUpSplashBinding;
import com.bpm.bpm_ver4.testpack.TestDefined;
import com.bpm.bpm_ver4.util.LoadingUtil;

public class SignUpSplashActivity extends BaseActivity implements SignUpSplashContract.View {


    private ActivitySignUpSplashBinding binding;

    private SignUpSplashContract.Presenter mPresenter;
    private SignUpSplashPresenter mSignUpPresenter;

    private boolean isEmail = false;
    private boolean isEmailCode = false;
    private boolean isPassword = false;

    // view
    private Snackbar mSnackbar;
    private LoadingUtil mLoadingUtil;


    private void test() {
        if (TestDefined.signUpSplashActivity) {
            binding.etEmail.setText("202bpm.com@gmail.com");
            binding.etPw.setText("1111");
            binding.etPwComp.setText("1111");
//            binding.btnDuplicate.callOnClick();
//            binding.googleSignInBtn.callOnClick();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_splash);
        mSnackbar = Snackbar.make(binding.getRoot(), "", Snackbar.LENGTH_INDEFINITE);

        mSignUpPresenter = new SignUpSplashPresenter(this);

        binding.topLayer.tvTitleToolbar.setText(R.string.title_sign_up);
        setSupportActionBar(binding.topLayer.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingUtil = new LoadingUtil(this);

        // 이메일 중복검사 click
        binding.btnDuplicate.setOnClickListener(
                View -> {
                    mSnackbar.dismiss();
                    mSignUpPresenter.emailDupCheck(binding.etEmail.getText().toString().trim());
                });

        // 이메일 코드 검사 click
        binding.btnCodeConfirm.setOnClickListener(
                View -> mSignUpPresenter.emailCodeCheck(
                        binding.etEmailCode.getText().toString().trim()
                ));

        // 다음으로 click
        binding.btnNext.setOnClickListener(
                View -> mSignUpPresenter.passwordConfirm(
                        binding.etPw.getText().toString().trim(),
                        binding.etPwComp.getText().toString().trim()
                    ));

        binding.googleSignInBtn.setOnClickListener(View -> mPresenter.googleSignUp(this));
        binding.naverSignBtn.setOnClickListener(View -> mPresenter.naverSignUp(this));
        binding.kakaoSignBtn.setOnClickListener(View -> mPresenter.kakaoSignUp(this));

        test();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.googleLoginActivityResultValue(requestCode, resultCode, data);
    }

    // 이메일 중복 확인후 뷰 갱신
    @Override
    public void emailCheckUpdateView(boolean ok) {
        isEmail = ok;
    }

    // 이메일 코드 확인후 뷰 갱신
    @Override
    public void emailCodeUpdateView(boolean ok) {
        isEmailCode = ok;
    }

    // 패스워드 일치 확인후 뷰 갱신
    @Override
    public void passwordConfirmUpdateView(boolean ok) {
        isPassword = ok;
        emailSignUpActivity();
    }

    @Override
    public void emailSignUpActivity() {
        if (!isEmail) {
            showMessage(R.string.email_duplicate_check_msg);
            return;
        }
        if (!isEmailCode) {
            showMessage(R.string.email_code_failed_msg);
            return;
        }
        if (!isPassword) {
            showMessage(R.string.password_do_not_match);
            return;
        }

        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(Common.IS_EMAIL_SIGN_UP, true);
        intent.putExtra(Common.ID, mSignUpPresenter.getEmail());
        intent.putExtra(Common.PW, mSignUpPresenter.getPassword());
        startActivity(intent);
    }

    @Override
    public void snsSignUpActivity(String sns, String token) {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(Common.IS_EMAIL_SIGN_UP, false);
        intent.putExtra(Common.ID, sns);
        intent.putExtra(Common.PW, token);
        startActivity(intent);
    }

    @Override
    public void showMessage(int message) {
        showToast(message);
        mSnackbar = Snackbar.make(binding.getRoot(), getString(message), Snackbar.LENGTH_INDEFINITE);
        mSnackbar.show();
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
        mSnackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.show();
    }

    @Override
    public void showLoading(boolean visible) {
        if (visible)
            mLoadingUtil.show();
        else
            mLoadingUtil.dismiss();
    }

    @Override
    public void setPresenter(SignUpSplashContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
