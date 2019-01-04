package com.bpm.bpm_ver4.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bpm.bpm_ver4.BuildConfig;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.signin.SignInInterface;
import com.bpm.bpm_ver4.api.signin.SignInRetrofit;
import com.bpm.bpm_ver4.data.source.SignInDataSource;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.EmailInfoObj;
import com.bpm.bpm_ver4.vo.MemberObj;
import com.bpm.bpm_ver4.vo.SnsInfoObj;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInRemoteDataSource implements SignInDataSource {

    public static final String TAG = SignInRemoteDataSource.class.getSimpleName();

    private SignInInterface mSignInRetrofit;

    public SignInRemoteDataSource() {
        mSignInRetrofit = SignInRetrofit.getInstance();
    }


    @Override
    public void signInWithEmail(@NonNull EmailInfoObj emailInfoObj, SignInCallback callback) {
        mSignInRetrofit.signInWithEmail(emailInfoObj)
                .enqueue(new Callback<ApiObj<MemberObj>>() {
                    @Override
                    public void onResponse(Call<ApiObj<MemberObj>> call, Response<ApiObj<MemberObj>> response) {
                        if (BuildConfig.DEBUG) {
                            try {
                                Log.e(TAG + " signInWithEmail", response.headers().get("Authorization"));
                                Log.e(TAG + " signInWithEmail", response.body().toJson());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG + " signInWithEmail", "not Data");
                            }
                        }


                        if (response == null || response.headers() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInWithEmail", "header null");
                            return;
                        }

                        String token = response.headers().get("Authorization");
                        if (token == null || token.isEmpty()) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInWithEmail", "header null");
                            return;
                        }

                        if (response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInWithEmail", "null Data");
                            return;
                        }

                        ApiObj<MemberObj> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInWithEmail", "STATUS_FAIL");
                            return;
                        }

                        MemberObj memberObj = apiObj.obj;
                        callback.onResponse(token, memberObj);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<MemberObj>> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG + " signInWithEmail", "onFailure");
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void signInWithSns(@NonNull SnsInfoObj snsInfoObj, SignInCallback callback) {
        mSignInRetrofit.signInWithSns(snsInfoObj)
                .enqueue(new Callback<ApiObj<MemberObj>>() {
                    @Override
                    public void onResponse(Call<ApiObj<MemberObj>> call, Response<ApiObj<MemberObj>> response) {
                        if (BuildConfig.DEBUG) {
                            try {
                                Log.e(TAG + " signInWithSns", response.headers().get("Authorization"));
                                Log.e(TAG + " signInWithSns", response.body().toJson());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG + " signInWithSns", "not Data");
                            }
                        }


                        if (response == null || response.headers() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInWithSns", "header null");
                            return;
                        }

                        String token = response.headers().get("Authorization");
                        if (token == null || token.isEmpty()) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInWithSns", "header null");
                            return;
                        }

                        if (response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInWithSns", "null Data");
                            return;
                        }

                        ApiObj<MemberObj> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signInWithSns", "STATUS_FAIL");
                            return;
                        }

                        MemberObj memberObj = apiObj.obj;
                        callback.onResponse(token, memberObj);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<MemberObj>> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG + " signInWithSns", "onFailure");
                        callback.onDataNotAvailable();
                    }
                });

    }

    @Override
    public void signInToken(@NonNull String token, SignInCallback callback) {
        mSignInRetrofit.signInToken(token)
                .enqueue(new Callback<ApiObj<MemberObj>>() {
                    @Override
                    public void onResponse(Call<ApiObj<MemberObj>> call, Response<ApiObj<MemberObj>> response) {
                        if (BuildConfig.DEBUG) {
                            try {
                                Log.e(TAG + " signInToken", response.body().toJson());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG + " signInToken", "not Data");
                            }
                        }


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
