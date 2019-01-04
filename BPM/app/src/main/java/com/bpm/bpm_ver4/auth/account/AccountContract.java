package com.bpm.bpm_ver4.auth.account;

import android.content.Context;
import android.graphics.Bitmap;

import com.bpm.bpm_ver4.BasePresenter;
import com.bpm.bpm_ver4.BaseView;
import com.bpm.bpm_ver4.vo.PersonalInfoObj;

public interface AccountContract {

    interface Presenter extends BasePresenter {

        void initData();
        void setImagePath(String path);
        void setRegionId(int regionId);
        void accountModify(Context context, String nick, String height, String weight, String age, String gender);

    }


    interface View extends BaseView<Presenter> {

        void initUpdateView(PersonalInfoObj personalInfoObj);
        void initProfileView(String thumbnail);
        void selectPhotoDialog();
        void profileImageUpdateView(String currentImagePath); // 프로필 이미지 뷰 갱신
        void regionUpdateView(String region); // 지역 뷰 갱신
        void accountModify();
        void showMessage(String message);
        void showMessage(int message);
    }
}
