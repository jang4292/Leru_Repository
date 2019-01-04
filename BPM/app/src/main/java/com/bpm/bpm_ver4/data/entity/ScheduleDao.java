package com.bpm.bpm_ver4.data.entity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bpm.bpm_ver4.vo.DayOfWeek;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Query("SELECT * FROM ScheduleVo")
    List<ScheduleVo> getSchedule();

    @Query("SELECT * FROM ScheduleVo WHERE day =:day")
    List<ScheduleVo> getTodaySchedule(DayOfWeek day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSchedule(ScheduleVo scheduleVo);

    @Update
    int update(ScheduleVo scheduleVo);
}
