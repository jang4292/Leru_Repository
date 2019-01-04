package com.bpm.bpm_ver4.data.source;

import android.support.annotation.NonNull;

import com.bpm.bpm_ver4.data.entity.ExVo;
import com.bpm.bpm_ver4.data.source.remote.ExRemoteDataSource;

import java.util.Map;

public class ExRepository implements ExDataSrouce {

    private static ExRepository INSTANCE = null;

    private final ExDataSrouce mRemoteDataSource;


    Map<String, ExVo> mCached;

    boolean mCacheIsDirty = false;

    private ExRepository(@NonNull ExDataSrouce remoteDataSource) {
        this.mRemoteDataSource = remoteDataSource;
    }


    public static ExRepository getInstance() {
        if (INSTANCE == null) {
            ExDataSrouce remoteDataSource = new ExRemoteDataSource();
            INSTANCE = new ExRepository(remoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void addExercise(@NonNull ExVo exVo, @NonNull UploadCallback callback) {
        mRemoteDataSource.addExercise(exVo, new UploadCallback() {
            @Override
            public void onUploaded() {
                callback.onUploaded();
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getExerciseMonth(@NonNull String y, @NonNull String m, @NonNull GetCallback callback) {
        mRemoteDataSource.getExerciseMonth(y, m, callback);
    }


    @Override
    public void getExerciseDay(@NonNull String token, @NonNull String d, @NonNull GetDayCallback callback) {
        mRemoteDataSource.getExerciseDay(token, d, callback);
    }
}


























