package com.bpm.bpm_ver4.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.bpm.bpm_ver4.R;

public class LoadingUtil {


    private Dialog dialog;

    public LoadingUtil(Context context) {
        dialog = new ProgressDialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }


    public void show() {
        dialog.show();
        dialog.setContentView(R.layout.layout_progress);
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
