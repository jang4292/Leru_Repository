package com.bpm202.SensorProject.Data;

import android.support.annotation.NonNull;

import com.bpm202.SensorProject.ValueObject.DayOfWeek;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.List;

public interface ScheduleDataSource {

    interface LoadCallback {

        void onLoaded(List<ScheduleValueObject> scheduleVos);

        void onDataNotAvailable();
    }

    interface GetCallback {

        void onLoaded(ScheduleValueObject scheduleVos);

        void onDataNotAvailable();
    }


    interface CompleteCallback {
        void onComplete();

        void onDataNotAvailable();
    }

    void getSchedules(@NonNull LoadCallback callback);

    void getDayOfWeekSchedules(@NonNull DayOfWeek dayOfWeek, @NonNull LoadCallback callback);

    void getTodaySchedules(@NonNull LoadCallback callback);

    void addSchedule(@NonNull ScheduleValueObject scheduleVos, CompleteCallback callback);

    void updateSchedule(@NonNull ScheduleValueObject scheduleVos);

    void deleteSchedule(@NonNull ScheduleValueObject scheduleVos);

    void deleteSchedule(@NonNull ScheduleValueObject scheduleVos, CompleteCallback callback);

    void sequenceSchedules(@NonNull List<ScheduleValueObject> scheduleVos);


}
