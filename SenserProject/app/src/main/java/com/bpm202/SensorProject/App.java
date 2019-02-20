package com.bpm202.SensorProject;

import android.app.Application;

public class App extends Application {

    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String tokenStr) {
        token = tokenStr;
    }
}
