package com.bpm202.SensorProject.Main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.CustomView.CustomCalendar;
import com.bpm202.SensorProject.Main.History.HistoryCalendarActivity;
import com.bpm202.SensorProject.Main.History.HistoryDateActivity;
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
//            Log.d("Test", "intent");
//            Intent intent = new Intent(getActivity(), HistoryDateActivity.class);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//            Util.FragmentUtil.addFragmentToActivity(getFragmentManager(), HistoryDateFragment.newInstance(), R.id.child_fragment_container);
        });
        customCalendar.setOnTotalDaysTextViewListener(() -> tvTotalDays);

    }

}
