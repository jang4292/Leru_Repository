package com.bpm202.SensorProject.ValueObject;


import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Type;

public class JsonObj {

    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }


    @SuppressWarnings("unchecked")
    public static <J extends JsonObj> J fromJson(JsonReader jr, Class<?> classOfT) {
        return (J) new GsonBuilder().create().fromJson(jr, classOfT);
    }


    @SuppressWarnings("unchecked")
    public static <J extends JsonObj> J fromJson(String json, Class<?> classOfT) {
        return (J) new GsonBuilder().create().fromJson(json, classOfT);
    }


    @SuppressWarnings("hiding")
    public static <Object> Object fromJson(JsonReader jr, Type typeOfT) {
        return new GsonBuilder().create().fromJson(jr, typeOfT);
    }


    @SuppressWarnings("hiding")
    public static <Object> Object fromJson(String json, Type typeOfT) {
        return new GsonBuilder().create().fromJson(json, typeOfT);
    }



}
