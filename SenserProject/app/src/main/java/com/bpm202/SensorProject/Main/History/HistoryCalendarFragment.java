package com.bpm202.SensorProject.Main.History;

import android.support.annotation.NonNull;
import android.view.View;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.R;

public class HistoryCalendarFragment extends BaseFragment {

    private static HistoryCalendarFragment instance = null;

    public static HistoryCalendarFragment newInstance() {
        return instance == null ? new HistoryCalendarFragment() : instance;
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_calendar;
    }

    @NonNull
    @Override
    protected void initView(View v) {
    }
}
