package com.bpm.bpm_ver4.util.anim;

import android.view.View;

public class AnimationUtil {


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
