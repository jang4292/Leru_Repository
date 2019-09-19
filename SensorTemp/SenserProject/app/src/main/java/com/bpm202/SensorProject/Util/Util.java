package com.bpm202.SensorProject.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Data.DayOfWeek;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    // 이메일 형식검사를 한다.

    /**
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

            if (dialog != null && dialog.getContext() != context) {
                dialog.hide();
                dialog.dismiss();
                dialog = null;
                isShown = false;
            }
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

    public static class Time {
        private static int dividedTime = 1000;

        public static long getDuration(long startTime) {
            return (System.currentTimeMillis() - startTime) / dividedTime;
        }
    }

    public static class CalendarInfo {

        private static Calendar calendar;

        public static Calendar getCalendar() {
            if (calendar == null)
                calendar = Calendar.getInstance();
            return calendar;
        }

        public static DayOfWeek getDayOfWeek() {
            int dayCode = getCalendar().get(Calendar.DAY_OF_WEEK);
            return DayOfWeek.findByCode(dayCode);
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

    /**
     * This provides methods to help Activities load their UI.
     */
    public static class FragmentUtil {
        /**
         * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
         * performed by the {@code fragmentManager}.
         */
        public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment);
            transaction.addToBackStack(fragment.getTag());
            transaction.commit();
        }

        public static void replaceFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                     @NonNull Fragment fragment, int frameId) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(frameId, fragment);
            transaction.commit();
        }

        /**
         * Fragment 변환을 해주기 위한 부분, Fragment의 Instance를 받아서 변경
         *
         * @param fragmentManager
         * @param fragment
         * @param frameId
         */
        public static void replaceFragment(@NonNull FragmentManager fragmentManager,
                                           @NonNull Fragment fragment, int frameId) {
            fragmentManager.popBackStack();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(frameId, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }
}
