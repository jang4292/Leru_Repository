package com.bpm202.SensorProject.RetrofitAPI;

import com.bpm202.SensorProject.Common.CommonUrl;
import com.bpm202.SensorProject.Data.ExVo;
import com.bpm202.SensorProject.ValueObject.ApiObj;
import com.bpm202.SensorProject.ValueObject.ExObj;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ExerciseInterface {

    // 운동결과 등록
    @POST(CommonUrl.exerciseUrl)
    Call<ApiObj<Boolean>> exercise(
            @Header("Authorization") String token,
            @Body() ExVo exVo
    );

    // 운동기록 조회
    @GET(CommonUrl.exerciseUrl)
    Call<ApiObj<ArrayList<ExObj>>> exercise(
            @Header("Authorization") String token,
            @Query("index") int index
    );

    // 월별 운동 유무 조회
    @GET(CommonUrl.exerciseMonthUrl)
    Call<ApiObj<ArrayList<Integer>>> exerciseMonth(
            @Header("Authorization") String token,
            @Query("y") String year,
            @Query("m") String month
    );

    // 일별 운동 결과 조회
    @GET(CommonUrl.exerciseDayUrl)
    Call<ApiObj<List<ExVo>>> exerciseDay(
            @Header("Authorization") String token,
            @Query("d") String date
    );


    //  일정 추가
    @POST(CommonUrl.scheduleAddUrl)
    Call<ApiObj<Boolean>> addSchedules(
            @Header("Authorization") String token,
            @Body() ScheduleValueObject scheduleVo
    );

    // 일정 조회
    @GET(CommonUrl.scheduleUrl)
    Call<ApiObj<List<ScheduleValueObject>>> schedules(
            @Header("Authorization") String token
    );


    // 일정 삭제
    @POST(CommonUrl.scheduleDeleteUrl)
    Call<ApiObj<Boolean>> deleteSchedules(
            @Header("Authorization") String token,
            @Body() ScheduleValueObject scheduleVo
    );


    // 일정 수정
    @POST(CommonUrl.scheduleEditUrl)
    Call<ApiObj<Boolean>> scheduleEdit(
            @Header("Authorization") String token,
            @Body() ScheduleValueObject scheduleVo
    );


    // 일정순서 변경
    @POST(CommonUrl.scheduleSequenceUrl)
    Call<ApiObj<Boolean>> scheduleSequence(
            @Header("Authorization") String token,
            @Body() List<ScheduleValueObject> scheduleVos
    );


}
