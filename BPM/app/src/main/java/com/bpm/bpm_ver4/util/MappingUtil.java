package com.bpm.bpm_ver4.util;

import android.content.Context;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.vo.Type;

public class MappingUtil {

    public static String name(Context context, String name) {
        String convertName = "?";
        final String[] names = {
                Type.PUSH_UP.getName(), Type.PULL_UP.getName(),
                Type.CYCLE.getName(), Type.BARBELL_CURL.getName(),
                Type.DUMBBELL_CURL.getName(), Type.SIT_UP.getName(),
                Type.SQUAT.getName(), Type.BARBELL_DEADLIFT.getName(),
                Type.DUMBBELL_DEADLIFT.getName(), Type.BARBELL_ROW.getName(),
                Type.DUMBBELL_ROW.getName(), Type.BARBELL_BENCH_PRESS.getName(),
                Type.DUMBBELL_BENCH_PRESS.getName(), Type.BARBELL_SHOULDER_PRESS.getName(),
                Type.DUMBBELL_SHOULDER_PRESS.getName(), Type.FRONT_LATERAL_RAISE.getName(),
                Type.SIDE_LATERAL_RAISE.getName(), Type.OVER_LATERAL_RAISE.getName(),
                Type.LAT_PULL_DOWN.getName(), Type.AB_SLIDE.getName(),
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
