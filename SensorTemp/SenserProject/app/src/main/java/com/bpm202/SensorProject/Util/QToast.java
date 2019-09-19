package com.bpm202.SensorProject.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import com.bpm202.SensorProject.Data.SignInRepository;

public class QToast {

    public static final String TAG = SignInRepository.class.getSimpleName();

    @NonNull
    public static void showToast(@NonNull Context context, @StringRes int msgResource) {
        showToast(context, context.getString(msgResource));
    }
    @NonNull
    public static void showToast(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
