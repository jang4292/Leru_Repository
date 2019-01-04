package com.bpm.bpm_ver4.data.source;

import android.support.annotation.NonNull;

import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.vo.DayOfWeek;

import java.util.List;

public interface ScheduleDataSource {

    interface LoadCallback {

        void onLoaded(List<ScheduleVo> scheduleVos);

        void onDataNotAvailable();
    }

    interface GetCallback {

        void onLoaded(ScheduleVo scheduleVos);

        void onDataNotAvailable();
    }


    interface CompleteCallback {
        void onComplete();
        void onDataNotAvailable();
    }

    void getSchedules(@NonNull LoadCallback callback);

    void getDayOfWeekSchedules(@NonNull DayOfWeek dayOfWeek, @NonNull LoadCallback callback);

    void getTodaySchedules(@NonNull LoadCallback callback);

    void addSchedule(@NonNull ScheduleVo scheduleVos, CompleteCallback callback);

    void updateSchedule(@NonNull ScheduleVo scheduleVos);

    void deleteSchedule(@NonNull ScheduleVo scheduleVos);

    void sequenceSchedules(@NonNull List<ScheduleVo> scheduleVos);



}
