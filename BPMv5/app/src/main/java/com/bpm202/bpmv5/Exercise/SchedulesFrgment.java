package com.bpm202.bpmv5.Exercise;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpm202.bpmv5.R;

public class SchedulesFrgment extends Fragment {

    public static SchedulesFrgment newInstance() {
        return new SchedulesFrgment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_schedules, container, false); // 여기서 UI를 생성해서 View를 return
    }
}
