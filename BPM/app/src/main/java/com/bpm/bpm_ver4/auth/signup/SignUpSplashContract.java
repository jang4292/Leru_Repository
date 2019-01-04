package com.bpm.bpm_ver4.auth.signup;


import android.app.Activity;
import android.content.Intent;

import com.bpm.bpm_ver4.BasePresenter;
import com.bpm.bpm_ver4.BaseView;

public interface SignUpSplashContract {


    interface Presenter extends BasePresenter {
        void emailDupCheck(String email);
        void emailCodeCheck(String emailCode);
        void passwordConfirm(String pw, String pwConf);
        void googleSignUp(Activity activity);
        void naverSignUp(Activity activity);
        void kakaoSignUp(Activity activity);
        void googleLoginActivityResultValue(int requestCode, int resultCode, Intent data);
        String getEmail();
        String getPassword();
    }


    interface View extends BaseView<Presenter> {
        void emailCheckUpdateView(boolean ok); // 이메일 검사
        void emailCodeUpdateView(boolean ok);
        void passwordConfirmUpdateView(boolean ok);
        void emailSignUpActivity();
        void snsSignUpActivity(String sns, String token);
        void showMessage(int message);
        void showMessage(String message);
        void showLoading(boolean visible);
    }
}
