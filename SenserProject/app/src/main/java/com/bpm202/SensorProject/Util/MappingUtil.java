package com.bpm202.SensorProject.Util;

import android.content.Context;

import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

public class MappingUtil {

    public static final int[] exerciseIconResource = {
            R.drawable.today_push_up_img_small,
            R.drawable.today_pull_up_img_small,
            R.drawable.today_cycle_img_small,
            R.drawable.today_curl_barbell_small,
            R.drawable.today_curl_barbell_small,
            R.drawable.today_crunch_img_small,
            R.drawable.today_squat_img_small,
            R.drawable.today_deadlift_barbell_img_small,
            R.drawable.deadlift_dumbbell,
            R.drawable.today_row_barbell_img_small,
            R.drawable.today_row_dumbbell_img_small,
            R.drawable.today_bench_press_barbell_img_small,
            R.drawable.today_bench_press_dumbbell_img_small,
            R.drawable.today_overhead_press_barbell_img_small,
            R.drawable.today_over_head_press_dumbell_img_small,
            R.drawable.today_raise_front_img_small,
            R.drawable.today_later_raise_side_img_small,
            R.drawable.today_fly_img_small,
            R.drawable.today_lat_pull_down_img_small,
            R.drawable.today_abwheel_img_small

    };

    public static final int[] exerciseIconResourceBig = {
            R.drawable.today_push_up_img,
            R.drawable.today_pull_up_img,
            R.drawable.today_cycle_img,
            R.drawable.today_curl_barbell,
            R.drawable.today_curl_barbell,
            R.drawable.today_crunch_img,
            R.drawable.today_squat_img,
            R.drawable.today_deadlift_barbell_img,
            R.drawable.deadlift_dumbbell,
            R.drawable.today_row_barbell_img,
            R.drawable.today_row_dumbbell_img,
            R.drawable.today_bench_press_barbell_img,
            R.drawable.today_bench_press_dumbbell_img,
            R.drawable.today_overhead_press_barbell_img,
            R.drawable.today_over_head_press_dumbell_img,
            R.drawable.today_raise_front_img,
            R.drawable.today_later_raise_side_img,
            R.drawable.today_fly_img,
            R.drawable.today_lat_pull_down_img,
            R.drawable.today_abwheel_img

    };

    public static String name(Context context, String name) {
        String convertName = "?";
        final String[] names = {
                TypeValueObject.PUSH_UP.getName(),
                TypeValueObject.PULL_UP.getName(),
                TypeValueObject.CYCLE.getName(),
                TypeValueObject.BARBELL_CURL.getName(),
                TypeValueObject.DUMBBELL_CURL.getName(),
                TypeValueObject.SIT_UP.getName(),
                TypeValueObject.SQUAT.getName(),
                TypeValueObject.BARBELL_DEADLIFT.getName(),
                TypeValueObject.DUMBBELL_DEADLIFT.getName(),
                TypeValueObject.BARBELL_ROW.getName(),
                TypeValueObject.DUMBBELL_ROW.getName(),
                TypeValueObject.BARBELL_BENCH_PRESS.getName(),
                TypeValueObject.DUMBBELL_BENCH_PRESS.getName(),
                TypeValueObject.BARBELL_SHOULDER_PRESS.getName(),
                TypeValueObject.DUMBBELL_SHOULDER_PRESS.getName(),
                TypeValueObject.FRONT_LATERAL_RAISE.getName(),
                TypeValueObject.SIDE_LATERAL_RAISE.getName(),
                TypeValueObject.OVER_LATERAL_RAISE.getName(),
                TypeValueObject.LAT_PULL_DOWN.getName(),
                TypeValueObject.AB_SLIDE.getName()
        };

        final String[] convertNames = context.getResources().getStringArray(R.array.ex_name);

        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(name)) {
                convertName = convertNames[i];
            }
        }

        return convertName;
    }


    public static boolean isUsingRangeType(Context context, String name) {
        String currentName = MappingUtil.name(context, name);

        final String[] convertNames = context.getResources().getStringArray(R.array.ex_using_range_type);

        for (int i = 0; i < convertNames.length; i++) {
            if (convertNames[i].equals(currentName)) {
                return true;
            }
        }
        return false;

    }

    public static boolean isCountType(Context context, String name) {

        String currentName = MappingUtil.name(context, name);

        final String[] convertNames = context.getResources().getStringArray(R.array.ex_count_type);

        for (int i = 0; i < convertNames.length; i++) {
            if (convertNames[i].equals(currentName)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isWaitingExercise(TypeValueObject type) {

        if (type.getId() == 1 ||
                type.getId() == 2 ||
                type.getId() == 3 ||
                type.getId() == 4 ||
                type.getId() == 5 ||
                type.getId() == 6 ||
                type.getId() == 7 ||
                type.getId() == 8 ||
                type.getId() == 9 ||
                type.getId() == 10
        ) {
            return true;
        } else {
            return false;
        }
    }
}
