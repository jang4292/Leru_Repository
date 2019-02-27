package com.bpm202.SensorProject.Main.History;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.CustomView.CustomCalendar;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.Util;

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
        CustomCalendar customCalendar = v.findViewById(R.id.gridview_days);
        TextView tvTotalDays = v.findViewById(R.id.tv_total_days);

        customCalendar.setOnClickListener(v1 -> {
            Util.FragmentUtil.addFragmentToActivity(getFragmentManager(), HistoryDateFragment.newInstance(), R.id.child_fragment_container);
        });
        customCalendar.setOnTotalDaysTextViewListener(() -> tvTotalDays);
    }
}
