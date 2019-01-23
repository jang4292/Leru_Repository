package com.bpm202.bpmv5.API;

import android.util.Log;

import com.bpm202.bpmv5.RetrofitAPI.SignInRetrofit;
import com.bpm202.bpmv5.ValueObject.ApiObj;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPasswordAPI extends Api {
    private static final String TAG = FindPasswordAPI.class.getSimpleName();

    // 비밀 번호 찾기
    public static void findPassword(String email, ApiCallback callback) {
        SignInRetrofit.getInstance().findPassword(email)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        /*if (BuildConfig.DEBUG) {
                            Log.e(TAG + "findPassword", response.body().toJson());
                        }*/

                        if (response != null && response.body() != null) {
                            callback.callBack(response.body());
                            //Log.d(TAG, "TEST, " + response.body().toString());
                            //Log.d(TAG, "TEST, " + response.body().toJson());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + "findPassword", "onFailure");
                        t.printStackTrace();
                    }
                });
    }

}
