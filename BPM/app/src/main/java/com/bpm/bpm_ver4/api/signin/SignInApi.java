package com.bpm.bpm_ver4.api.signin;

import android.util.Log;

import com.bpm.bpm_ver4.BuildConfig;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.ApiCallback;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.EmailInfoObj;
import com.bpm.bpm_ver4.vo.MemberObj;
import com.bpm.bpm_ver4.vo.PersonalInfoObj;
import com.bpm.bpm_ver4.vo.RegionObj;
import com.bpm.bpm_ver4.vo.SnsInfoObj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInApi extends Api {

    private static final String TAG = SignInApi.class.getSimpleName();

    // 회원 정보 수정
    public static void modifyAccount(String token, PersonalInfoObj obj, ApiCallback callback) {
        if (BuildConfig.DEBUG) Log.d(TAG + " modifyAccount post", obj.toJson());

        SignInRetrofit.getInstance().modifyAccount(token, obj)
                .enqueue(new Callback<ApiObj<PersonalInfoObj>>() {
                    @Override
                    public void onResponse(Call<ApiObj<PersonalInfoObj>> call, Response<ApiObj<PersonalInfoObj>> response) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG + "modifyAccount", response.body().toJson());
                        }

                        if (response != null && response.body() != null) {
                            callback.callBack(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiObj<PersonalInfoObj>> call, Throwable t) {
                        Log.e(TAG + "modifyAccount", "onFailure");
                        t.printStackTrace();
                    }
                });
    }


    // 비밀 번호 찾기
    public static void findPassword(String email, ApiCallback callback) {
        SignInRetrofit.getInstance().findPassword(email)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG + "findPassword", response.body().toJson());
                        }

                        if (response != null && response.body() != null) {
                            callback.callBack(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + "findPassword", "onFailure");
                        t.printStackTrace();
                    }
                });
    }


    // 비밀번호 변경
    public static void changePassword(String token, EmailInfoObj obj, ApiCallback callback) {
        if (BuildConfig.DEBUG) Log.d(TAG + "changePassword", obj.toJson());


        SignInRetrofit.getInstance().changePassword(token, obj)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG + "changePassword", response.body().toJson());
                        }

                        if (response != null && response.body() != null) {
                            callback.callBack(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + "changePassword", "onFailure");
                        t.printStackTrace();
                    }
                });
    }


    // 회원 탈퇴
    public static void secession(String token, ApiCallback callback) {
        SignInRetrofit.getInstance().secession(token)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG + "secession", response.body().toJson());
                        }

                        if (response != null && response.body() != null) {
                            callback.callBack(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + "secession", "onFailure");
                        t.printStackTrace();
                    }
                });
    }

}
