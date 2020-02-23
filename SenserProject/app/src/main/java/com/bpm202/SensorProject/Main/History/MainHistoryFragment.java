package com.bpm202.SensorProject.Main.History;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.CustomView.CustomCalendar;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.R;

public class MainHistoryFragment extends BaseFragment {

    private static MainHistoryFragment instance = null;

    public static MainHistoryFragment newInstance() {
        if (instance == null) {
            instance = new MainHistoryFragment();
        }
        return instance;
    }


    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_main;
    }

    @NonNull
    @Override
    protected void initView(View v) {
        ((MainActivity) getActivity()).setTitleText(R.string.title_history);

        CustomCalendar customCalendar = v.findViewById(R.id.gridview_days);
        TextView tvTotalDays = v.findViewById(R.id.tv_total_days);

        customCalendar.setOnClickListener(v1 -> {


            String date = "";

            if (v1 instanceof TextView) {

                String day = ((TextView) v1).getText().toString();
                if (day.length() < 2) {
                    day = "0" + day;
                }
                date = customCalendar.getCurrentDate() + "-" + day;

            }

            Intent intent = new Intent(getActivity(), HistoryDayActivity.class);
            intent.putExtra(AppPreferences.KEY_DATE_INFO, date);
            startActivity(intent);
        });
        customCalendar.setOnTotalDaysTextViewListener(() -> tvTotalDays);

    }

}
