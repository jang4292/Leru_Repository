package com.bpm.bpm_ver4.data.source;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.bpm.bpm_ver4.data.source.remote.SignInRemoteDataSource;
import com.bpm.bpm_ver4.vo.EmailInfoObj;
import com.bpm.bpm_ver4.vo.SnsInfoObj;

public class SignInRepository implements SignInDataSource {

    private static SignInRepository INSTANCE = null;

    private final SignInDataSource mRemoteDataSource;

    private SignInRepository(@NonNull SignInDataSource remoteDataSource) {
        this.mRemoteDataSource = remoteDataSource;
    }


    public static SignInRepository getInstance() {
        if (INSTANCE == null) {
            SignInDataSource remoteDataSource = new SignInRemoteDataSource();
            INSTANCE = new SignInRepository(remoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void signInWithEmail(@NonNull EmailInfoObj emailInfoObj, SignInCallback callback) {
        new Handler().postDelayed(() -> {
            mRemoteDataSource.signInWithEmail(emailInfoObj, callback);
        }, 500);

    }

    @Override
    public void signInWithSns(@NonNull SnsInfoObj snsInfoObj, SignInCallback callback) {
        mRemoteDataSource.signInWithSns(snsInfoObj, callback);
    }

    @Override
    public void signInToken(@NonNull String token, SignInCallback callback) {
        mRemoteDataSource.signInToken(token, callback);
    }
}
