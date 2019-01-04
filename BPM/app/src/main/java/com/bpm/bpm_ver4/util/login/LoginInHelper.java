package com.bpm.bpm_ver4.util.login;

import android.content.Intent;

public interface LoginInHelper {

    // 로그인 버튼 클릭시 이벤트
    public void loginPressImp();

    // 로그아웃 버튼 클릭시 이벤트
    public void logoutPressImp();

    // 회원탈퇴 이벤트
    public void secessionImp();

    // 액티비티 시작시 이벤트
    public void onStartImp();

    public void onActivityResultImp(int requestCode, int resultCode, Intent data);


    public interface Callback {
        int OK = 1;
        int NO = -1;

        void callback(int result, String token);
    }

}
