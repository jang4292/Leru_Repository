package com.bpm.bpm_ver4.auth.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.data.source.SignInDataSource;
import com.bpm.bpm_ver4.data.source.SignInRepository;
import com.bpm.bpm_ver4.testpack.FakeData;
import com.bpm.bpm_ver4.util.login.GoogleLoginHelper;
import com.bpm.bpm_ver4.util.login.KaKaoLoginHelper;
import com.bpm.bpm_ver4.util.login.LoginInHelper;
import com.bpm.bpm_ver4.util.login.NaverLoginHelper;
import com.bpm.bpm_ver4.util.AppPref;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.EmailInfoObj;
import com.bpm.bpm_ver4.vo.MemberObj;
import com.bpm.bpm_ver4.vo.SnsInfoObj;

public class LoginPresenter implements LoginContract.Presenter, SignInDataSource.SignInCallback {

    private LoginContract.View view;
    private EmailInfoObj mEmailInfoObj;
    private SnsInfoObj mSnsInfoObj;
    private SignInRepository mSignInRepository;

    private String mEmail;

    private GoogleLoginHelper mGoogleLoginHelper = null;
    private NaverLoginHelper mNaverLoginHelper = null;
    private KaKaoLoginHelper mKaKaoLoginHelper = null;


    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        mEmailInfoObj = new EmailInfoObj();
        mSnsInfoObj = new SnsInfoObj();
        mSignInRepository = SignInRepository.getInstance();
    }


    @Override
    public void emailLogin(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            view.showLoading(true);
            mEmail = email;
            mEmailInfoObj.setEmail(email);
            mEmailInfoObj.setPassword(password);
            mSignInRepository.signInWithEmail(mEmailInfoObj, this);
        } else {
            view.showMessage(R.string.email_input_hint);
        }
    }

    @Override
    public void googleLogin(Activity activity) {
        if (mGoogleLoginHelper == null) {
            mGoogleLoginHelper = new GoogleLoginHelper(activity, createLoginCallback(Api.SNS_GOOGLE));
        }
        mGoogleLoginHelper.loginPressImp();
    }

    @Override
    public void naverLogin(Activity activity) {
        // TODO: 2018-08-13 : 네이버는 개발자센터 아직 app등록 X
//        if (mNaverLoginHelper == null) {
//            mNaverLoginHelper = new NaverLoginHelper(activity, createLoginCallback(Api.SNS_NAVER));
//        }
//        mNaverLoginHelper.loginPressImp();
    }

    @Override
    public void kakaoLogin(Activity activity) {

    }

    @Override
    public void googleLoginActivityResultValue(int requestCode, int resultCode, Intent data) {
        if (mGoogleLoginHelper != null) {
            mGoogleLoginHelper.onActivityResultImp(requestCode, resultCode, data);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void onResponse(String token, MemberObj memberObj) {
        new AppPref(view.getContext().getApplicationContext()).setStringPref(AppPref.KEY_TOKEN, token);
        App.setToken(token);
        Log.d(getClass().getSimpleName(), "TOKEN: " + token);

        App.setMember(memberObj);
        App.setEmail(mEmail);
        view.showLoading(false);
        view.loginSuccessView();
        SignInRepository.destroyInstance();
    }

    @Override
    public void onDataNotAvailable() {
        view.showLoading(false);
        view.showMessage(R.string.login_callback_error);
    }

    private LoginInHelper.Callback createLoginCallback(final String snsApi) {
        return new LoginInHelper.Callback() {
            @Override
            public void callback(int result, String token) {
                if (result == OK) {
                    // 로그인 성공, 토큰 발급
                    // 발급된 토큰으로 서버에 로그인 요청함
                    mSnsInfoObj.setSns(snsApi);
                    mSnsInfoObj.setToken(token);
                    mSignInRepository.signInWithSns(mSnsInfoObj, LoginPresenter.this);
                } else {
                    view.showMessage(R.string.login_callback_error);
                }

            }
        };
    }


    public void testLogin() {
        ApiObj apiObj = FakeData.getEmialLoginData();
        MemberObj obj = (MemberObj) apiObj.obj;
        App.setMember(obj);
        App.setEmail(mEmail);
        view.loginSuccessView();
    }
}
