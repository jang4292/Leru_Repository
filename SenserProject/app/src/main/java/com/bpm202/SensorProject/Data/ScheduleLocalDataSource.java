package com.bpm202.SensorProject.Data;

import android.support.annotation.NonNull;

import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.List;

public class ScheduleLocalDataSource implements ScheduleDataSource {
    @Override
    public void getSchedules(@NonNull LoadCallback callback) {

    }

    @Override
    public void getDayOfWeekSchedules(@NonNull DayOfWeek dayOfWeek, @NonNull LoadCallback callback) {

    }

    @Override
    public void getTodaySchedules(@NonNull LoadCallback callback) {

    }

    @Override
    public void addSchedule(@NonNull ScheduleValueObject scheduleVo, CompleteCallback callback) {

    }


    @Override
    public void updateSchedule(@NonNull ScheduleValueObject scheduleVo) {

    }

    @Override
    public void deleteSchedule(@NonNull ScheduleValueObject scheduleVo) {

    }

    @Override
    public void deleteSchedule(@NonNull ScheduleValueObject scheduleVos, CompleteCallback callback) {

    }

    @Override
    public void sequenceSchedules(@NonNull List<ScheduleValueObject> scheduleVos) {

    }
}
