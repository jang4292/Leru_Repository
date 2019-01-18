package com.bpm202.bpmv5.Util;

import android.content.Context;
import android.widget.Toast;

public class QToast {


    public static void showToast(Context context, int msg) {
        Toast.makeText(context.getApplicationContext(), context.getString(msg), Toast.LENGTH_SHORT).show();
    }

    /*public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }*/
}
