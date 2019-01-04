package com.bpm.bpm_ver4.data.source.local;

import android.support.annotation.NonNull;

import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.data.source.ScheduleDataSource;
import com.bpm.bpm_ver4.vo.DayOfWeek;

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
    public void addSchedule(@NonNull ScheduleVo scheduleVo, CompleteCallback callback) {

    }


    @Override
    public void updateSchedule(@NonNull ScheduleVo scheduleVo) {

    }

    @Override
    public void deleteSchedule(@NonNull ScheduleVo scheduleVo) {

    }

    @Override
    public void sequenceSchedules(@NonNull List<ScheduleVo> scheduleVos) {

    }
}
