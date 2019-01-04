package com.bpm.bpm_ver4.data.entity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface ExDao {

    @Query("SELECT * FROM ExVo")
    List<ExVo> getExs();

    @Query("SELECT * FROM ExVo WHERE date = :date")
    List<ExVo> getExsByDate(String date);

    @Query("SELECT * FROM ExVo WHERE id = :id")
    ExVo getExById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEx(ExVo exVo);

    @Update
    int update(ExVo exVo);

}
