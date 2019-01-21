package com.bpm202.bpmv5.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bpm202.bpmv5.API.SignInRepository;

public class QToast {

    public static final String TAG = SignInRepository.class.getSimpleName();

    public static void showToast(Context context, int msg) {
        Log.d(TAG, "msg : " + msg);
        Toast.makeText(context.getApplicationContext(), context.getString(msg), Toast.LENGTH_SHORT).show();
    }
}
