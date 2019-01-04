package com.bpm.bpm_ver4.api.exercise;

import com.bpm.bpm_ver4.api.CommonUrl;

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
