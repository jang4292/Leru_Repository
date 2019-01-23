package com.bpm202.bpmv5.Data;

import android.support.annotation.NonNull;
import android.util.Log;


import com.bpm202.bpmv5.RetrofitAPI.SignInInterface;
import com.bpm202.bpmv5.ValueObject.EmailInfoObj;


public class SignInRepository implements SignInDataSource {

    public static final String TAG = SignInRepository.class.getSimpleName();

    private SignInInterface mSignInRetrofit;




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

    public void signInWithEmail(@NonNull EmailInfoObj emailInfoObj, @NonNull final SignInCallback callback) {

        Log.d(TAG, "signInWithEmail");

        mRemoteDataSource.signInWithEmail(emailInfoObj, callback);

    }

/*    @Override
    public void signInWithSns(@NonNull SnsInfoObj snsInfoObj, SignInCallback callback) {

    }

    @Override
    public void signInToken(@NonNull String token, SignInCallback callback) {

    }*/

}
