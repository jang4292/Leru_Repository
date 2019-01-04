package com.bpm.bpm_ver4.auth.signup;

import android.app.Activity;
import android.content.Intent;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.ApiCallback;
import com.bpm.bpm_ver4.api.signin.SignInApi;
import com.bpm.bpm_ver4.data.source.SignUpDataSource;
import com.bpm.bpm_ver4.data.source.SignUpRepository;
import com.bpm.bpm_ver4.util.login.GoogleLoginHelper;
import com.bpm.bpm_ver4.util.login.KaKaoLoginHelper;
import com.bpm.bpm_ver4.util.login.LoginInHelper;
import com.bpm.bpm_ver4.util.login.NaverLoginHelper;
import com.bpm.bpm_ver4.vo.ApiObj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpSplashPresenter implements SignUpSplashContract.Presenter, SignUpDataSource.CheckEmailCallback{

    private SignUpSplashContract.View view;
    private String mEmailCode = "NPE";
    private String mEmailStr = "NPE";
    private String mPasswordStr = "NPE";

    private GoogleLoginHelper mGoogleLoginHelper = null;
    private NaverLoginHelper mNaverLoginHelper = null;
    private KaKaoLoginHelper mKaKaoLoginHelper = null;

    private SignUpRepository mRepository;


    public SignUpSplashPresenter(SignUpSplashContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.mRepository = SignUpRepository.getInstance();
    }


    @Override
    public void emailDupCheck(String email) {
        if (isValidEmail(email)) {
            view.showLoading(true);
            mEmailStr = email;
            mRepository.checkEmail(email, this);
        } else {
            view.emailCheckUpdateView(false);
            view.showMessage(R.string.email_is_not_valid_msg);
        }
    }

    @Override
    public void emailCodeCheck(String emailCode) {
        boolean isCode;
        if (emailCode.isEmpty()) {
            isCode = false;
        } else {
            isCode = mEmailCode.equals(emailCode);
        }

        view.emailCodeUpdateView(isCode);

        if (isCode) {
            view.showMessage(R.string.email_code_confirm_msg);
        } else {
            view.showMessage(R.string.email_code_failed_msg);
        }
    }

    @Override
    public void passwordConfirm(String pw, String pwConf) {
        mPasswordStr = pw;
        view.passwordConfirmUpdateView(pw.equals(pwConf));
    }

    @Override
    public void googleSignUp(Activity activity) {
        if (mGoogleLoginHelper == null) {
            mGoogleLoginHelper = new GoogleLoginHelper(activity, createLoginCallback(Api.SNS_GOOGLE));
        }
        mGoogleLoginHelper.loginPressImp();
    }

    @Override
    public void naverSignUp(Activity activity) {

    }

    @Override
    public void kakaoSignUp(Activity activity) {

    }

    @Override
    public void googleLoginActivityResultValue(int requestCode, int resultCode, Intent data) {
        if (mGoogleLoginHelper != null) {
            mGoogleLoginHelper.onActivityResultImp(requestCode, resultCode, data);
        }
    }

    @Override
    public String getEmail() {
        return mEmailStr;
    }

    @Override
    public String getPassword() {
        return mPasswordStr;
    }

    @Override
    public void start() {

    }

    @Override
    public void onResponse(String authCode, Boolean enable) {
        view.showLoading(false);
        view.emailCheckUpdateView(!enable);

        if (!enable) {
            mEmailCode = authCode;
            view.showMessage(R.string.email_confirm_msg);
        } else {
            view.showMessage(R.string.email_duplicate_msg);
        }
    }

    @Override
    public void onDataNotAvailable() {
        view.showLoading(false);
        view.emailCheckUpdateView(false);
        view.showMessage(R.string.error_msg);
    }

    private LoginInHelper.Callback createLoginCallback(final String snsApi) {
        return new LoginInHelper.Callback() {
            @Override
            public void callback(int result, String token) {
                if (result == OK) {
                    // 로그인 성공, 토큰 발급
                    // 발급된 토큰으로 회원가입 화면 업데이트
                    view.snsSignUpActivity(snsApi, token);
                } else {
                    view.showMessage(R.string.login_callback_error);
                }

            }
        };
    }

    // 이메일 형식검사를 한다.
    private boolean isValidEmail(String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }


}
