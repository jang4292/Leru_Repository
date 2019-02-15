package com.bpm202.SensorProject.Data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bpm202.SensorProject.API.Api;
import com.bpm202.SensorProject.RetrofitAPI.ExerciseRetrofit;
import com.bpm202.SensorProject.Util.App;
import com.bpm202.SensorProject.ValueObject.ApiObj;
import com.bpm202.SensorProject.ValueObject.DayOfWeek;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleRemoteDataSource implements ScheduleDataSource {

    public static final String TAG = ScheduleRemoteDataSource.class.getSimpleName();

    private static ScheduleRemoteDataSource INSTANCE;

    public static ScheduleRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ScheduleRemoteDataSource();
        }
        return INSTANCE;
    }

    // 운동 일정 조회
    @Override
    public void getSchedules(@NonNull LoadCallback callback) {
        ExerciseRetrofit.getInstance().schedules(App.getToken())
                .enqueue(new Callback<ApiObj<List<ScheduleValueObject>>>() {
                    @Override
                    public void onResponse(Call<ApiObj<List<ScheduleValueObject>>> call, Response<ApiObj<List<ScheduleValueObject>>> response) {
                        /*if (BuildConfig.DEBUG && response != null)
                            Log.d(TAG + " getSchedules", response.body().toJson());*/

                        if (response == null || response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " getSchedules", "esponse == null || response.body() == null");
                        }

                        ApiObj<List<ScheduleValueObject>> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " getSchedules", "STATUS_FAIL");
                        }

                        List<ScheduleValueObject> obj = apiObj.obj;
                        callback.onLoaded(obj);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<List<ScheduleValueObject>>> call, Throwable t) {
                        Log.e(TAG + " getSchedule", "onFailure");
                        t.printStackTrace();
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getDayOfWeekSchedules(@NonNull DayOfWeek dayOfWeek, @NonNull LoadCallback callback) {
        // TODO: 2018-12-19 : 나중에 생각
    }

    @Override
    public void getTodaySchedules(@NonNull LoadCallback callback) {
//        ExerciseRetrofit.getInstance().schedules(App.getToken())
//                .enqueue(new Callback<ApiObj<List<ScheduleVo>>>() {
//                    @Override
//                    public void onResponse(Call<ApiObj<List<ScheduleVo>>> call, Response<ApiObj<List<ScheduleVo>>> response) {
//                        if (BuildConfig.DEBUG && response != null)
//                            Log.d(TAG + " getTodaySchedules", response.body().toJson());
//
//                        if (response == null || response.body() == null) {
//                            callback.onDataNotAvailable();
//                            Log.e(TAG + " getTodaySchedules", "esponse == null || response.body() == null");
//                        }
//
//                        ApiObj<List<ScheduleVo>> apiObj = response.body();
//                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
//                            callback.onDataNotAvailable();
//                            Log.e(TAG + " getTodaySchedules", "STATUS_FAIL");
//                        }
//
//                        List<ScheduleVo> obj = apiObj.obj;
//                        App.setSchedules(obj);
//                        List<ScheduleVo> todayObj = App.getTodaySchedules();
//                        callback.onLoaded(todayObj);
//                    }
//
//                    @Override
//                    public void onFailure(Call<ApiObj<List<ScheduleVo>>> call, Throwable t) {
//                        Log.e(TAG + " getTodaySchedules", "onFailure");
//                        t.printStackTrace();
//                        callback.onDataNotAvailable();
//                    }
//                });
    }


    // 운동 일정 추가
    @Override
    public void addSchedule(@NonNull ScheduleValueObject scheduleVo, @NonNull CompleteCallback callback) {
        ExerciseRetrofit.getInstance().addSchedules(App.getToken(), scheduleVo)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        /*if (BuildConfig.DEBUG && response != null)
                            Log.e(TAG + " addSchedule", response.body().toJson());*/

                        if (response == null || response.body() == null)
                            callback.onDataNotAvailable();

                        ApiObj<Boolean> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL))
                            callback.onDataNotAvailable();

                        Boolean cussess = apiObj.obj;
                        if (cussess) {
                            callback.onComplete();
                        }

                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + " addSchedule", "onFailure");
                        t.printStackTrace();
                        callback.onDataNotAvailable();
                    }
                });
    }


    // 운동 일정 수정
    @Override
    public void updateSchedule(@NonNull ScheduleValueObject scheduleVo) {
        ExerciseRetrofit.getInstance().scheduleEdit(App.getToken(), scheduleVo)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        /*if (BuildConfig.DEBUG)
                            Log.e(TAG + " updateSchedule", response.body().toJson());*/
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + " updateSchedule", "onFailure");
                        t.printStackTrace();
                    }
                });

    }

    // 운동 일정 삭제
    @Override
    public void deleteSchedule(@NonNull ScheduleValueObject scheduleVo) {
        ExerciseRetrofit.getInstance().deleteSchedules(App.getToken(), scheduleVo)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        /*if (BuildConfig.DEBUG)
                            Log.e(TAG + " deleteSchedule", response.body().toJson());*/
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + " deleteSchedule", "onFailure");
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void deleteSchedule(@NonNull ScheduleValueObject scheduleVo, CompleteCallback callback) {
        ExerciseRetrofit.getInstance().deleteSchedules(App.getToken(), scheduleVo)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        /*if (BuildConfig.DEBUG)
                            Log.e(TAG + " deleteSchedule", response.body().toJson());*/

                        if (response == null || response.body() == null)
                            callback.onDataNotAvailable();

                        ApiObj<Boolean> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL))
                            callback.onDataNotAvailable();

                        Boolean cussess = apiObj.obj;
                        if (cussess) {
                            callback.onComplete();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + " deleteSchedule", "onFailure");
                        t.printStackTrace();
                    }
                });
    }

    // 운동리스트 순서 변경
    @Override
    public void sequenceSchedules(@NonNull List<ScheduleValueObject> scheduleVos) {
        /*if (BuildConfig.DEBUG) {
            for (ScheduleValueObject s : scheduleVos)
                Log.i(TAG + "sequenceSchedules", s.toJson());
        }*/

        ExerciseRetrofit.getInstance().scheduleSequence(App.getToken(), scheduleVos)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        /*if (BuildConfig.DEBUG)
                            Log.e(TAG + "sequenceSchedules", response.body().toJson());*/
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        Log.e(TAG + " sequenceSchedules", "onFailure");
                        t.printStackTrace();
                    }
                });
    }
}
