package com.bpm202.SensorProject.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.transition.Fade;
import android.view.View;
import android.widget.ProgressBar;

import com.bpm202.SensorProject.R;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    // 이메일 형식검사를 한다.

    /**
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static class LoadingProgress {

        private static Dialog dialog;
        private static boolean isShown = false;

        public static void show(Context context) {
            if (dialog == null) {
                dialog = new ProgressDialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);

                dialog.setContentView(R.layout.layout_progress);
            }

            if (!isShown) {
                dialog.show();
                isShown = true;
            }
        }

        public static void hide() {
            if (isShown) {
                if (dialog != null) {
                    dialog.hide();
                    dialog.dismiss();
                    dialog = null;
                    isShown = false;
                }
            }
        }
    }

    public static class CalendarInfo {

        public static Calendar calendar = getCalendar();

        public static Calendar getCalendar() {
            if (calendar == null)
                calendar = Calendar.getInstance();
            return calendar;
        }
    }

    public static class FadeAnimation {
        public static void fadeIn(View... views) {
            for (View view : views) {
                view.setAlpha(0f);
            }
            for (View view : views) {
                view.animate().alpha(1f).start();
            }
        }

        public static void fadeOut(View... views) {
            for (View view : views) {
                view.animate().alpha(0f).start();
            }
        }
    }
}
