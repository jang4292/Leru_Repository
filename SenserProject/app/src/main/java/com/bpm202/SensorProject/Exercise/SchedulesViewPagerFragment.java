package com.bpm202.SensorProject.Exercise;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bpm202.SensorProject.R;

public class SchedulesViewPagerFragment extends Fragment {
    private TextView tv_Text;

    public static SchedulesViewPagerFragment Instance() {
        return new SchedulesViewPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedules_view_pager, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
    }

    private void initView(View v) {
        tv_Text = v.findViewById(R.id.tv_text);
    }

    public void setTv_Text(@NonNull String str) {
        Log.d(MainActivity.TAG, "tv_Text : " + tv_Text);
        Log.d(MainActivity.TAG, "str : " + str);
        if (tv_Text != null) {
            tv_Text.setText(str);
        }
    }
}
