package com.bpm202.SensorProject.Util;

import android.content.Context;

import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

public class MappingUtil {

    public static final int[] exerciseIconResource = {
            R.drawable.push_up,
            R.drawable.pull_up,
            R.drawable.bicycle,
            R.drawable.curl_barbell,
            R.drawable.curl_dumbbell,
            R.drawable.crunch,
            R.drawable.squat,
            R.drawable.deadlift_barbell,
            R.drawable.deadlift_dumbbell,
            R.drawable.row_barbell,
            R.drawable.row_dumbbell,
            R.drawable.bench_press_barbell,
            R.drawable.bench_press_dumbbell,
            R.drawable.overhead_press_barbell,
            R.drawable.overhead_press_dumbbell,
            R.drawable.later_raise_front,
            R.drawable.later_raise_side,
            R.drawable.fly,
            R.drawable.lat_pull_down,
            R.drawable.wheel_slide
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

        for (int i = 0;  i < names.length;  i++) {
            if (names[i].equals(name)) {
                convertName = convertNames[i];
            }
        }

        return convertName;
    }
}
