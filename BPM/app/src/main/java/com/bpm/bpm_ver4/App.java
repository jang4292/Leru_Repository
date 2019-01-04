package com.bpm.bpm_ver4;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.vo.DayOfWeek;
import com.bpm.bpm_ver4.vo.MemberObj;
import com.bpm.bpm_ver4.vo.RegionObj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class App extends Application {

    private static String email;
    private static MemberObj member;
    private static RegionObj region;
    public static Calendar calendar = getCalendar();

    public static MemberObj getMember() {
        return member;
    }

    public static void setMember(MemberObj memberObj) {
        member = memberObj;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        App.email = email;
    }

    public static DayOfWeek getDayOfWeek() {
        int dayCode = getCalendar().get(Calendar.DAY_OF_WEEK);
        return DayOfWeek.findByCode(dayCode);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    private static String token;
    public static String getToken() {
        return token;
    }
    public static void setToken(String tokenStr) {
        token = tokenStr;
    }
    public static Calendar getCalendar() {
        if (calendar == null)
            calendar = Calendar.getInstance();
        return calendar;
    }

}
