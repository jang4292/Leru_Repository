package com.bpm202.bpmv5.Data;

import android.support.annotation.NonNull;

import com.bpm202.bpmv5.ValueObject.EmailInfoObj;
import com.bpm202.bpmv5.ValueObject.MemberObj;
import com.bpm202.bpmv5.ValueObject.SnsInfoObj;

public interface SignInDataSource {

    interface SignInCallback {

        void onResponse(String token, MemberObj memberObj);

        void onDataNotAvailable();
    }


    void signInWithEmail(@NonNull EmailInfoObj emailInfoObj, SignInCallback callback);

    //void signInWithSns(@NonNull SnsInfoObj snsInfoObj, SignInCallback callback);

    //void signInToken(@NonNull String token, SignInCallback callback);
}
