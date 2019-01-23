package com.bpm202.bpmv5.Data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.bpm202.bpmv5.ValueObject.MemberObj;

public class SignUpRepository implements SignUpDataSource {

    private static SignUpRepository INSTANCE = null;

    private final SignUpDataSource mRemoteDataSource;

    private SignUpRepository(@NonNull SignUpDataSource remoteDataSource) {
        this.mRemoteDataSource = remoteDataSource;
    }


    public static SignUpRepository getInstance() {
        if (INSTANCE == null) {
            SignUpDataSource remoteDataSource = new SignUpRemoteDataSource();
            INSTANCE = new SignUpRepository(remoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void region(@NonNull RegionCallback callback) {
        mRemoteDataSource.region(callback);
    }

    @Override
    public void checkEmail(@NonNull String email, @NonNull CheckEmailCallback callback) {
        mRemoteDataSource.checkEmail(email, callback);
    }

    @Override
    public void checkNickname(@NonNull String nickname, @NonNull CheckNicknameCallback callback) {
        mRemoteDataSource.checkNickname(nickname, callback);
    }

    @Override
    public void photo(@Nullable String thumbnail, @NonNull String filePath, @NonNull PhotoCallback callback) {
        mRemoteDataSource.photo(thumbnail, filePath, callback);
    }

    @Override
    public void signUpWithEmail(@NonNull MemberObj memberObj, @NonNull SignUpCallback callback) {
        mRemoteDataSource.signUpWithEmail(memberObj, callback);
    }

    @Override
    public void signUpWithSns(@NonNull MemberObj memberObj, @NonNull SignUpCallback callback) {
        mRemoteDataSource.signUpWithSns(memberObj, callback);
    }
}
