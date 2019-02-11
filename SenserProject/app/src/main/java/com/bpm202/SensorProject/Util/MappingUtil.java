package com.bpm202.SensorProject.Util;

import android.content.Context;

import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

public class MappingUtil {

    public static String name(Context context, String name) {
        String convertName = "?";
        final String[] names = {
                TypeValueObject.PUSH_UP.getName(), TypeValueObject.PULL_UP.getName(),
                TypeValueObject.CYCLE.getName(), TypeValueObject.BARBELL_CURL.getName(),
                TypeValueObject.DUMBBELL_CURL.getName(), TypeValueObject.SIT_UP.getName(),
                TypeValueObject.SQUAT.getName(), TypeValueObject.BARBELL_DEADLIFT.getName(),
                TypeValueObject.DUMBBELL_DEADLIFT.getName(), TypeValueObject.BARBELL_ROW.getName(),
                TypeValueObject.DUMBBELL_ROW.getName(), TypeValueObject.BARBELL_BENCH_PRESS.getName(),
                TypeValueObject.DUMBBELL_BENCH_PRESS.getName(), TypeValueObject.BARBELL_SHOULDER_PRESS.getName(),
                TypeValueObject.DUMBBELL_SHOULDER_PRESS.getName(), TypeValueObject.FRONT_LATERAL_RAISE.getName(),
                TypeValueObject.SIDE_LATERAL_RAISE.getName(), TypeValueObject.OVER_LATERAL_RAISE.getName(),
                TypeValueObject.LAT_PULL_DOWN.getName(), TypeValueObject.AB_SLIDE.getName(),
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
