package com.bpm.bpm_ver4.data.source;

import android.support.annotation.NonNull;

import com.bpm.bpm_ver4.vo.EmailInfoObj;
import com.bpm.bpm_ver4.vo.MemberObj;
import com.bpm.bpm_ver4.vo.SnsInfoObj;

public interface SignInDataSource {

    interface SignInCallback {

        void onResponse(String token, MemberObj memberObj);

        void onDataNotAvailable();
    }


    void signInWithEmail(@NonNull EmailInfoObj emailInfoObj, SignInCallback callback);

    void signInWithSns(@NonNull SnsInfoObj snsInfoObj, SignInCallback callback);

    void signInToken(@NonNull String token, SignInCallback callback);
}
