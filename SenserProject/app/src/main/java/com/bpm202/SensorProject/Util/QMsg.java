package com.bpm202.SensorProject.Util;

import android.content.Context;
import android.widget.Toast;

public class QMsg {

    public static void ShowMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ShowMessage(Context context, int msg) {
        Toast.makeText(context, context.getString(msg), Toast.LENGTH_SHORT).show();
    }
}
