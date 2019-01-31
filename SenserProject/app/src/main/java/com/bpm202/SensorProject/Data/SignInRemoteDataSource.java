package com.bpm202.SensorProject.Data;

import android.support.annotation.NonNull;
import android.util.Log;


import com.bpm202.SensorProject.API.Api;
import com.bpm202.SensorProject.RetrofitAPI.SignInInterface;
import com.bpm202.SensorProject.RetrofitAPI.SignInRetrofit;
import com.bpm202.SensorProject.ValueObject.ApiObj;
import com.bpm202.SensorProject.ValueObject.EmailInfoObj;
import com.bpm202.SensorProject.ValueObject.MemberObj;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInRemoteDataSource implements SignInDataSource {

    public static final String TAG = SignInRemoteDataSource.class.getSimpleName();

    private class Info {
        MemberObj memberObj;
        String token;
    }

    private SignInInterface mSignInRetrofit;

    public SignInRemoteDataSource() {
        mSignInRetrofit = SignInRetrofit.getInstance();
    }


    @Override
    public void signInWithEmail(@NonNull EmailInfoObj emailInfoObj, final SignInCallback callback) {

        mSignInRetrofit.signInWithEmail(emailInfoObj).enqueue(new Callback<ApiObj<MemberObj>>() {
            @Override
            public void onResponse(Call<ApiObj<MemberObj>> call, Response<ApiObj<MemberObj>> response) {
                SignInRemoteDataSource.Info info = new SignInRemoteDataSource.Info();
                if (!isErrorChecked(response, info)) {
                    callback.onResponse(info.token, info.memberObj);
                }

            }

            @Override
            public void onFailure(Call<ApiObj<MemberObj>> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG + " signInWithEmail", "onFailure");
                callback.onDataNotAvailable();
            }

            private boolean isErrorChecked(Response<ApiObj<MemberObj>> response, SignInRemoteDataSource.Info info) {

                if (response == null || response.headers() == null) {
                    callback.onDataNotAvailable();
                    Log.e(TAG + " signInWithEmail", "header null");
                    Log.e(TAG + " signInWithEmail", "it's not connected database servers");
                    return true;
                }

                info.token = response.headers().get("Authorization");
                if (info == null || info.token == null || info.token.isEmpty()) {
                    callback.onDataNotAvailable();
                    Log.e(TAG + " signInWithEmail", "header null");
                    return true;
                }

                if (response.body() == null) {
                    callback.onDataNotAvailable();
                    Log.e(TAG + " signInWithEmail", "null Data");
                    return true;
                }

                ApiObj<MemberObj> apiObj = response.body();

                if (apiObj == null || apiObj.obj == null || apiObj.status.equals(Api.STATUS_FAIL)) {
                    callback.onDataNotAvailable();
                    Log.e(TAG + " signInWithEmail", "STATUS_FAIL");
                    return true;
                }
                info.memberObj = apiObj.obj;
                return false;

            }
        });
    }

    @Override
    public void signInToken(@NonNull String token, SignInCallback callback) {
        mSignInRetrofit.signInToken(token)
                .enqueue(new Callback<ApiObj<MemberObj>>() {
                    @Override
                    public void onResponse(Call<ApiObj<MemberObj>> call, Response<ApiObj<MemberObj>> response) {

                        if (response == null || response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInToken", "null Data");
                            return;
                        }

                        ApiObj<MemberObj> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInToken", "STATUS_FAIL");
                            return;
                        }

                        MemberObj memberObj = apiObj.obj;
                        callback.onResponse(token, memberObj);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<MemberObj>> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG + " signInToken", "onFailure");
                        callback.onDataNotAvailable();
                    }
                });
    }
}

