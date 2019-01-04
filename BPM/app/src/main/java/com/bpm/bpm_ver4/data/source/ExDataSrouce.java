package com.bpm.bpm_ver4.data.source;

import android.support.annotation.NonNull;

import com.bpm.bpm_ver4.data.entity.ExVo;

import java.util.List;

public interface ExDataSrouce {

    interface UploadCallback {

        void onUploaded();

        void onDataNotAvailable();

    }

    interface GetCallback {

        void onLoaded(List<Integer> i_list);

        void onDataNotAvailable();
    }


    interface GetDayCallback {

        void onLoaded(List<ExVo> exVoList);

        void onDataNotAvailable();
    }


    void addExercise(@NonNull ExVo exVo,
                     @NonNull UploadCallback callback);

    void getExerciseMonth(@NonNull String y, /*년 (ex. 2018)*/
                          @NonNull String m, /*월 (ex. 08)*/
                          @NonNull GetCallback callback);

    void getExerciseDay(@NonNull String token,
                        @NonNull String d /*년월일 (ex. 2018-08-16)*/,
                        @NonNull GetDayCallback callback);

}
