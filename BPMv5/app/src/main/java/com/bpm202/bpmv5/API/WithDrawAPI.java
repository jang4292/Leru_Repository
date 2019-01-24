package com.bpm202.bpmv5.API;

import android.util.Log;

import com.bpm202.bpmv5.RetrofitAPI.SignInRetrofit;
import com.bpm202.bpmv5.ValueObject.ApiObj;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithDrawAPI extends Api {
    private static final String TAG = FindPasswordAPI.class.getSimpleName();

    // 회원 탈퇴
    public static void secession(String token, ApiCallback callback) {
        SignInRetrofit.getInstance().secession(token)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        /*if (BuildConfig.DEBUG) {
                            Log.e(TAG + "secession", response.body().toJson());
                        }*/

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
