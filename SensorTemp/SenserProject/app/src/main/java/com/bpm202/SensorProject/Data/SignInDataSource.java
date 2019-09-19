package com.bpm202.SensorProject.Data;

import android.support.annotation.NonNull;

import com.bpm202.SensorProject.ValueObject.EmailInfoObj;
import com.bpm202.SensorProject.ValueObject.MemberObj;

public interface SignInDataSource {

    interface SignInCallback {

        void onResponse(String token, MemberObj memberObj);

        void onDataNotAvailable();
    }


    void signInWithEmail(@NonNull EmailInfoObj emailInfoObj, SignInCallback callback);

    //void signInWithSns(@NonNull SnsInfoObj snsInfoObj, SignInCallback callback);

    void signInToken(@NonNull String token, SignInCallback callback);
}
