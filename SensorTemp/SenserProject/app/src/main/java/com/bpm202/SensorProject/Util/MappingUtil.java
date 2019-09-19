package com.bpm202.SensorProject.Util;

import android.content.Context;

import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

public class MappingUtil {

    public static final int[] exerciseIconResource = {
//            R.drawable.push_up,
            R.drawable.today_push_up_img_small,
//            R.drawable.pull_up,
            R.drawable.today_pull_up_img_small,
//            R.drawable.bicycle,
            R.drawable.today_cycle_img_small,
//            R.drawable.curl_barbell,
            R.drawable.today_curl_barbell_small,
//            R.drawable.curl_dumbbell,
            R.drawable.today_curl_barbell_small,
//            R.drawable.crunch,
            R.drawable.today_crunch_img_small,
//            R.drawable.squat,
            R.drawable.today_squat_img_small,
//            R.drawable.deadlift_barbell,
            R.drawable.today_deadlift_barbell_img_small,
//            R.drawable.deadlift_dumbbell,
            R.drawable.deadlift_dumbbell,
//            R.drawable.row_barbell,
            R.drawable.today_row_barbell_img_small,
//            R.drawable.row_dumbbell,
            R.drawable.today_row_dumbbell_img_small,
//            R.drawable.bench_press_barbell,
            R.drawable.today_bench_press_barbell_img_small,
//            R.drawable.bench_press_dumbbell,
            R.drawable.today_bench_press_dumbbell_img_small,
//            R.drawable.overhead_press_barbell,
            R.drawable.today_overhead_press_barbell_img_small,
//            R.drawable.overhead_press_dumbbell,
            R.drawable.today_over_head_press_dumbell_img_small,
//            R.drawable.later_raise_front,
            R.drawable.today_raise_front_img_small,
//            R.drawable.later_raise_side,
            R.drawable.today_later_raise_side_img_small,
//            R.drawable.fly,
            R.drawable.today_fly_img_small,
//            R.drawable.lat_pull_down,
            R.drawable.today_lat_pull_down_img_small,
//            R.drawable.wheel_slide
            R.drawable.today_abwheel_img_small

    };

    public static final int[] exerciseIconResourceBig = {
//            R.drawable.push_up,
            R.drawable.today_push_up_img,
//            R.drawable.pull_up,
            R.drawable.today_pull_up_img,
//            R.drawable.bicycle,
            R.drawable.today_cycle_img,
//            R.drawable.curl_barbell,
            R.drawable.today_curl_barbell,
//            R.drawable.curl_dumbbell,
            R.drawable.today_curl_barbell,
//            R.drawable.crunch,
            R.drawable.today_crunch_img,
//            R.drawable.squat,
            R.drawable.today_squat_img,
//            R.drawable.deadlift_barbell,
            R.drawable.today_deadlift_barbell_img,
//            R.drawable.deadlift_dumbbell,
            R.drawable.deadlift_dumbbell,
//            R.drawable.row_barbell,
            R.drawable.today_row_barbell_img,
//            R.drarow_dumbbell,
            R.drawable.today_row_dumbbell_img,
//            R.drabench_press_barbell,
            R.drawable.today_bench_press_barbell_img,
//            R.drabench_press_dumbbell,
            R.drawable.today_bench_press_dumbbell_img,
//            R.draoverhead_press_barbell,
            R.drawable.today_overhead_press_barbell_img,
//            R.draoverhead_press_dumbbell,
            R.drawable.today_over_head_press_dumbell_img,
//            R.dralater_raise_front,
            R.drawable.today_raise_front_img,
//            R.dralater_raise_side,
            R.drawable.today_later_raise_side_img,
//            R.drafly,
            R.drawable.today_fly_img,
//            R.dralat_pull_down,
            R.drawable.today_lat_pull_down_img,
//            R.drawheel_slide
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
}
