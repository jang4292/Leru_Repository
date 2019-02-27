package com.bpm202.SensorProject.Main;

import android.support.annotation.NonNull;

import com.bpm202.SensorProject.Data.DayOfWeek;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.ArrayList;
import java.util.List;

public class MainDataManager {

    private static MainDataManager instance = null;

    public static MainDataManager Instance() {
        return (instance == null) ? new MainDataManager() : instance;
    }

    private static List<ScheduleValueObject> listScheduleValueObject = new ArrayList<>();

    @NonNull
    public List<ScheduleValueObject> getScheduleValueObjectForDay(@NonNull DayOfWeek day) {
        List<ScheduleValueObject> objs = new ArrayList<>();
        for (ScheduleValueObject obj : listScheduleValueObject) {
            if (obj.getDay() == day) {
                objs.add(obj);
            }
        }
        return objs;
    }

    public void setListScheduleValueObject(List<ScheduleValueObject> list) {
        listScheduleValueObject = list;
    }

    @NonNull
    public List<ScheduleValueObject> getListScheduleValueObject() {
        return listScheduleValueObject;
    }
}
