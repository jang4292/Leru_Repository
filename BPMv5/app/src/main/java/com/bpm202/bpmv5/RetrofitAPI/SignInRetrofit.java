package com.bpm202.bpmv5.RetrofitAPI;


import com.bpm202.bpmv5.Common.CommonUrl;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInRetrofit {

    private static SignInInterface instance;

    public static SignInInterface getInstance() {
        if (instance == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CommonUrl.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(SignInInterface.class);
        }

        return instance;
    }

    /*
     * 타임아웃 설정등 커스텀 하여 사용할때 사용함
     * */
//    public static SignInInterface getInstance() {
//        if (instance == null) {
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .connectTimeout(1, TimeUnit.MINUTES)
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .writeTimeout(15, TimeUnit.SECONDS)
//                    .build();
//
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(CommonUrl.baseUrl)
//                    .client(okHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            instance = retrofit.create(SignInInterface.class);
//        }
//
//        return instance;
//    }

}
