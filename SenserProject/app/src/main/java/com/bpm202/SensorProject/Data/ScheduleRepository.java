package com.bpm202.SensorProject.Data;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bpm202.SensorProject.DataBase.ScheduleLocalDataSource;
import com.bpm202.SensorProject.Util.App;
import com.bpm202.SensorProject.ValueObject.DayOfWeek;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleRepository implements ScheduleDataSource {

    private static ScheduleRepository INSTANCE = null;

    private static final String KEY_SCHEDULE = "schedule";

    private final ScheduleDataSource mRemoteDataSource;

    private final ScheduleDataSource mLocalDataSource;

    Map<String, List<ScheduleValueObject>> mCached;


    boolean mCacheIsDirty = false;


    private ScheduleRepository(@NonNull ScheduleDataSource remoteDataSource,
                               @NonNull ScheduleDataSource localDataSource) {
        this.mRemoteDataSource = remoteDataSource;
        this.mLocalDataSource = localDataSource;
    }


    public static ScheduleRepository getInstance() {
        if (INSTANCE == null) {
            ScheduleDataSource remoteDataSource = new ScheduleRemoteDataSource();
            ScheduleDataSource localDataSource = new ScheduleLocalDataSource();
            INSTANCE = new ScheduleRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }


    public static void destoryInstance() {
        INSTANCE = null;
    }

    @Override
    public void getSchedules(@NonNull LoadCallback callback) {
        getScheduleFromRemoteDataSource(callback);
    }

    @Override
    public void getDayOfWeekSchedules(@NonNull DayOfWeek dayOfWeek, @NonNull LoadCallback callback) {
        if (mCached != null) {
            String key = String.valueOf(dayOfWeek.getCode());
            List<ScheduleValueObject> list = mCached.get(key);
            for (ScheduleValueObject vo : list)
                Log.d("svo", vo.toJson());
            callback.onLoaded(list);
            return;
        }


        getScheduleFromRemoteDataSource(new LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
                String key = String.valueOf(dayOfWeek.getCode());
                List<ScheduleValueObject> list = mCached.get(key);

                for (ScheduleValueObject vo : list)
                    callback.onLoaded(list);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }


    @Override
    public void getTodaySchedules(@NonNull LoadCallback callback) {
        mRemoteDataSource.getSchedules(new LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
                DayOfWeek dayOfWeek = App.getDayOfWeek();
                List<ScheduleValueObject> todaySchedules = new ArrayList<>();
                for (ScheduleValueObject obj : scheduleVos) {
                    if (obj.getDay() == dayOfWeek) {
                        todaySchedules.add(obj);
                    }
                }

                callback.onLoaded(todaySchedules);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    @Override
    public void addSchedule(@NonNull ScheduleValueObject scheduleVo, CompleteCallback callback) {
        mRemoteDataSource.addSchedule(scheduleVo, callback);
    }

    @Override
    public void updateSchedule(@NonNull ScheduleValueObject scheduleVo) {
        mRemoteDataSource.updateSchedule(scheduleVo);
    }

    @Override
    public void deleteSchedule(@NonNull ScheduleValueObject scheduleVo) {
        mRemoteDataSource.deleteSchedule(scheduleVo);
    }

    @Override
    public void deleteSchedule(@NonNull ScheduleValueObject scheduleVo, CompleteCallback callback) {
        mRemoteDataSource.deleteSchedule(scheduleVo, callback);
    }

    @Override
    public void sequenceSchedules(@NonNull List<ScheduleValueObject> scheduleVos) {
        mRemoteDataSource.sequenceSchedules(scheduleVos);
    }

    private void getScheduleFromRemoteDataSource(@NonNull final LoadCallback callback) {
        mRemoteDataSource.getSchedules(new LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                callback.onLoaded(scheduleVos);
                            }
                        }, 500
                );
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }


    private void putCache(List<ScheduleValueObject> scheduleVos) {
        if (mCached == null)
            mCached = new HashMap<>();

        mCached.clear();
        DayOfWeek[] dayOfWeek = DayOfWeek.values();

        for (int i = 0; i < dayOfWeek.length; i++) {
            List<ScheduleValueObject> list = new ArrayList<>();

            for (ScheduleValueObject vo : scheduleVos) {
                if (vo.getDay() == dayOfWeek[i])
                    list.add(vo);
            }

            mCached.put(String.valueOf(dayOfWeek[i].getCode()), list);
        }
    }


}
















