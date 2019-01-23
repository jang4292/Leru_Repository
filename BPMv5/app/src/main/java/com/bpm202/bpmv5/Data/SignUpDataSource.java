package com.bpm202.bpmv5.Data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.bpm202.bpmv5.ValueObject.MemberObj;
import com.bpm202.bpmv5.ValueObject.RegionObj;

import java.util.List;

public interface SignUpDataSource {

    interface RegionCallback {

        void onResponse(List<RegionObj> regionObjList);

        void onDataNotAvailable();

    }

    interface CheckEmailCallback {

        void onResponse(String authCode, Boolean enable);

        void onDataNotAvailable();

    }

    interface CheckNicknameCallback {

        void onResponse(Boolean enable);

        void onDataNotAvailable();
    }

    interface PhotoCallback {

        void onResponse(String path);

        void onDataNotAvailable();
    }

    interface SignUpCallback {

        void onResponse(String token, MemberObj memberObj);

        void onDataNotAvailable();
    }


    void region(@NonNull RegionCallback callback);

    void checkEmail(@NonNull String email, @NonNull CheckEmailCallback callback);

    void checkNickname(@NonNull String nickname, @NonNull CheckNicknameCallback callback);

    void photo(@Nullable String thumbnail, @NonNull String filePath, @NonNull PhotoCallback callback);

    void signUpWithEmail(@NonNull MemberObj memberObj, @NonNull SignUpCallback callback);

    void signUpWithSns(@NonNull MemberObj memberObj, @NonNull SignUpCallback callback);



}







