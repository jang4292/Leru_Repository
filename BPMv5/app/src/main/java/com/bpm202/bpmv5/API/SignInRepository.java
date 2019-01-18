package com.bpm202.bpmv5.API;

import android.support.annotation.NonNull;
import android.util.Log;


import com.bpm202.bpmv5.Data.SignInDataSource;
import com.bpm202.bpmv5.Data.SignInRemoteDataSource;
import com.bpm202.bpmv5.RetrofitAPI.SignInInterface;
import com.bpm202.bpmv5.RetrofitAPI.SignInRetrofit;
import com.bpm202.bpmv5.ValueObject.ApiObj;
import com.bpm202.bpmv5.ValueObject.EmailInfoObj;
import com.bpm202.bpmv5.ValueObject.MemberObj;
import com.bpm202.bpmv5.ValueObject.SnsInfoObj;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
