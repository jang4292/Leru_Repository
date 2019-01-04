package com.bpm.bpm_ver4.auth.signup;

import android.content.Context;

import com.bpm.bpm_ver4.BasePresenter;
import com.bpm.bpm_ver4.BaseView;

public interface SignUpContract {

    interface Presenter extends BasePresenter {
        void setId(boolean isEmailSingUp, String value1, String value2);
        void setImagePath(String imagePath); // 이미지 url 저장
        void nickDuplCheck(String nickName); // 닉네임 중복 확인
        void setLocationId(int locationId); // 지역 id 저장
        void signUp(String nick, String height, String weight, String sex); // 회원가입 요청
    }

    interface View extends BaseView<Presenter> {
        Context getContext();
        void selectPhotoDialog();
        void profileImageUpdateView(String currentImageFilePath); // 프로필 이미지 뷰 갱신
        void nickDuplCehckUpdateView(boolean isDuplicate); // 닉네임 중복 체크후 뷰 갱신
        void locationUpdateView(String location); // 지역 뷰 갱신
        void signUpUpdateView(); // 회원가입 완료 뷰 갱신
        void showMessage(int message); // 화면에 메시지 출력
        void showMessage(String message); // 화면에 메시지 출력
        void showLoading(boolean visible);
    }

}
