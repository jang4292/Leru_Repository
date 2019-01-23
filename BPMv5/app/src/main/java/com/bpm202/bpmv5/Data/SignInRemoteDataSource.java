package com.bpm202.bpmv5.Data;

import android.support.annotation.NonNull;
import android.util.Log;


import com.bpm202.bpmv5.API.Api;
import com.bpm202.bpmv5.RetrofitAPI.SignInInterface;
import com.bpm202.bpmv5.RetrofitAPI.SignInRetrofit;
import com.bpm202.bpmv5.ValueObject.ApiObj;
import com.bpm202.bpmv5.ValueObject.EmailInfoObj;
import com.bpm202.bpmv5.ValueObject.MemberObj;

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
        Log.d(TAG, "Test, signInWithEmail222");
        mSignInRetrofit.signInWithEmail(emailInfoObj).enqueue(new Callback<ApiObj<MemberObj>>() {
            @Override
            public void onResponse(Call<ApiObj<MemberObj>> call, Response<ApiObj<MemberObj>> response) {


                SignInRemoteDataSource.Info info = new SignInRemoteDataSource.Info();
                Log.d(TAG + " Test signInWithEmail", "info");
                if (!isErrorChecked(response, info)) {
                    //MemberObj memberObj = apiObj.obj;
                    //callback.onResponse(token, memberObj);
                    Log.d(TAG + " Test signInWithEmail", info.memberObj.toString());
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

                Log.d(TAG, "Test, isChecked");
                if (response == null || response.headers() == null) {
                    callback.onDataNotAvailable();
                    Log.e(TAG + " signInWithEmail", "header null");
                    Log.e(TAG + " signInWithEmail", "it's not connected database servers");
                    return true;
                }

                //String token = response.headers().get("Authorization");
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

/*    @Override
    public void signInWithSns(@NonNull SnsInfoObj snsInfoObj, SignInCallback callback) {

    }

    @Override
    public void signInToken(@NonNull String token, SignInCallback callback) {

    }*/
}

