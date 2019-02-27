package com.bpm202.SensorProject.Data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bpm202.SensorProject.API.Api;
import com.bpm202.SensorProject.BuildConfig;
import com.bpm202.SensorProject.RetrofitAPI.ExerciseRetrofit;
import com.bpm202.SensorProject.App;
import com.bpm202.SensorProject.ValueObject.ApiObj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExRemoteDataSource implements ExDataSrouce {

    public static final String TAG = ExRemoteDataSource.class.getSimpleName();

    @Override
    public void addExercise(@NonNull ExVo exVo, @NonNull UploadCallback callback) {
        if (BuildConfig.DEBUG)
            Log.d(TAG + " addExercise:전송~", exVo.toJson());

        ExerciseRetrofit.getInstance().exercise(App.getToken(), exVo)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        if (BuildConfig.DEBUG)
                            Log.e(TAG + " addExercise", response.body().toJson());

                        if (response == null || response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " addExercise", "null Data");
                            return;
                        }

                        callback.onUploaded();
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + " addExercise", "onFailure");
                        t.printStackTrace();
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getExerciseMonth(@NonNull String y, @NonNull String m, @NonNull GetCallback callback) {
        ExerciseRetrofit.getInstance().exerciseMonth(App.getToken(), y, m)
                .enqueue(new Callback<ApiObj<ArrayList<Integer>>>() {
                    @Override
                    public void onResponse(Call<ApiObj<ArrayList<Integer>>> call, Response<ApiObj<ArrayList<Integer>>> response) {
                        /*if (BuildConfig.DEBUG)
                            Log.e(TAG + " getExerciseMonth", response.body().toJson());*/

                        if (response == null || response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " getExerciseMonth", "null Data");
                            return;
                        }

                        ApiObj<ArrayList<Integer>> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            Log.e(TAG + " getExerciseMonth", "STATUS_FAIL");
                            callback.onDataNotAvailable();
                            return;
                        }

                        List<Integer> i_list = apiObj.obj;
                        callback.onLoaded(i_list);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<ArrayList<Integer>>> call, Throwable t) {
                        Log.e(TAG + " getExerciseMonth", "onFailure");
                        t.printStackTrace();
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getExerciseDay(@NonNull String token, @NonNull String d, @NonNull GetDayCallback callback) {
        ExerciseRetrofit.getInstance().exerciseDay(token, d)
                .enqueue(new Callback<ApiObj<List<ExVo>>>() {
                    @Override
                    public void onResponse(Call<ApiObj<List<ExVo>>> call, Response<ApiObj<List<ExVo>>> response) {
                        /*if (BuildConfig.DEBUG)
                            Log.e(TAG + " getExerciseDay", response.body().toJson());*/

                        if (response == null || response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " getExerciseDay", "null Data");
                            return;
                        }

                        ApiObj<List<ExVo>> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            Log.e(TAG + " getExerciseDay", "STATUS_FAIL");
                            callback.onDataNotAvailable();
                            return;
                        }

                        List<ExVo> exVoList = apiObj.obj;
                        callback.onLoaded(exVoList);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<List<ExVo>>> call, Throwable t) {
                        Log.e(TAG + " getExerciseDay", "onFailure");
                        t.printStackTrace();
                        callback.onDataNotAvailable();
                    }
                });
    }
}
