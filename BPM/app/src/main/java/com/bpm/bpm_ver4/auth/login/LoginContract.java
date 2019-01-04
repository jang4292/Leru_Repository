package com.bpm.bpm_ver4.auth.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.BasePresenter;
import com.bpm.bpm_ver4.BaseView;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.MemberObj;

public interface LoginContract {

    interface Presenter extends BasePresenter {
        void emailLogin(String email, String password); // 이메일 로그인
        void googleLogin(Activity activity); // 구글 로그인
        void naverLogin(Activity activity); // 네이버 로그인
        void kakaoLogin(Activity activity); // 카카오 로그인
        void googleLoginActivityResultValue(int requestCode, int resultCode, Intent data);
    }

    interface View extends BaseView<Presenter> {
        Context getContext();
        void loginSuccessView(); // 로그인성공 했을때 뷰 업데이트
        void showMessage(String errorMessage); // 로그인 실패 메시지
        void showMessage(int errorMessage); // 로그인 실패 메시지
        void showLoading(boolean visible);
    }
}
