package com.bpm.bpm_ver4.data.source;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.data.source.local.ScheduleLocalDataSource;
import com.bpm.bpm_ver4.data.source.remote.ScheduleRemoteDataSource;
import com.bpm.bpm_ver4.vo.DayOfWeek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScheduleRepository implements ScheduleDataSource {

    private static ScheduleRepository INSTANCE = null;

    private static final String KEY_SCHEDULE = "schedule";

    private final ScheduleDataSource mRemoteDataSource;

    private final ScheduleDataSource mLocalDataSource;

    Map<String, List<ScheduleVo>> mCached;


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
            List<ScheduleVo> list = mCached.get(key);
            for (ScheduleVo vo : list)
                Log.d("svo", vo.toJson());
            callback.onLoaded(list);
            return;
        }


        getScheduleFromRemoteDataSource(new LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleVo> scheduleVos) {
                String key = String.valueOf(dayOfWeek.getCode());
                List<ScheduleVo> list = mCached.get(key);

                for (ScheduleVo vo : list)
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
            public void onLoaded(List<ScheduleVo> scheduleVos) {
                DayOfWeek dayOfWeek = App.getDayOfWeek();
                List<ScheduleVo> todaySchedules = new ArrayList<>();
                for (ScheduleVo obj : scheduleVos) {
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
    public void addSchedule(@NonNull ScheduleVo scheduleVo, CompleteCallback callback) {
        mRemoteDataSource.addSchedule(scheduleVo, callback);
    }

    @Override
    public void updateSchedule(@NonNull ScheduleVo scheduleVo) {
        mRemoteDataSource.updateSchedule(scheduleVo);
    }

    @Override
    public void deleteSchedule(@NonNull ScheduleVo scheduleVo) {
        mRemoteDataSource.deleteSchedule(scheduleVo);
    }

    @Override
    public void sequenceSchedules(@NonNull List<ScheduleVo> scheduleVos) {
        mRemoteDataSource.sequenceSchedules(scheduleVos);
    }

    private void getScheduleFromRemoteDataSource(@NonNull final LoadCallback callback) {
        mRemoteDataSource.getSchedules(new LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleVo> scheduleVos) {
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


    private void putCache(List<ScheduleVo> scheduleVos) {
        if (mCached == null)
            mCached = new HashMap<>();

        mCached.clear();
        DayOfWeek[] dayOfWeek = DayOfWeek.values();

        for (int i = 0;  i < dayOfWeek.length;  i++) {
            List<ScheduleVo> list = new ArrayList<>();

            for (ScheduleVo vo : scheduleVos) {
                if (vo.getDay() == dayOfWeek[i])
                    list.add(vo);
            }

            mCached.put(String.valueOf(dayOfWeek[i].getCode()), list);
        }
    }


}
















