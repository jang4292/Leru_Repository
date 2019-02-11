package com.bpm202.SensorProject.Util;

import android.app.Application;

import com.bpm202.SensorProject.ValueObject.DayOfWeek;

import java.util.Calendar;

public class App extends Application {
    public static DayOfWeek getDayOfWeek() {
        int dayCode = getCalendar().get(Calendar.DAY_OF_WEEK);
        return DayOfWeek.findByCode(dayCode);
    }

    public static Calendar calendar = getCalendar();

    public static Calendar getCalendar() {
        if (calendar == null)
            calendar = Calendar.getInstance();
        return calendar;
    }

    private static String token;
    public static String getToken() {
        return token;
    }
    public static void setToken(String tokenStr) {
        token = tokenStr;
    }
}
