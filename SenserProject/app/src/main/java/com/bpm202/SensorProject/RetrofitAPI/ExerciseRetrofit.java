package com.bpm202.SensorProject.RetrofitAPI;

import com.bpm202.SensorProject.Common.CommonUrl;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExerciseRetrofit {

    public static ExerciseInterface instance;

    public static ExerciseInterface getInstance() {
        if (instance == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CommonUrl.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(ExerciseInterface.class);
        }

        return instance;
    }
}
