package com.bpm202.SensorProject.db;

import com.bpm202.SensorProject.ValueObject.JsonObj;
import com.bpm202.SensorProject.ValueObject.ScheduleObj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SendScheduleData extends JsonObj {

    public String name;
    public String day;
    public int setCnt;
    public int count;
    public int weight;
    public int rest;

    public String sTime;
    public String eTime;

    public String getCurrentTime(long time) {
        Date date = new Date(time);

        SimpleDateFormat CurTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String strCurTime = CurTimeFormat.format(date);

        return strCurTime;
    }

}
