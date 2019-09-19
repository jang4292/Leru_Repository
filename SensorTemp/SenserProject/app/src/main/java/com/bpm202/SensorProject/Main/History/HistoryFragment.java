package com.bpm202.SensorProject.Main.History;

import android.view.View;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.Util;

public class HistoryFragment extends BaseFragment {

    private static HistoryFragment instance;

    public static HistoryFragment newInstance() {

        if (instance == null) {
            instance = new HistoryFragment();
        }
        return instance;
    }

    public static void DestroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void initView(View v) {
        Util.FragmentUtil.replaceFragmentToActivity(getFragmentManager(), HistoryCalendarFragment.newInstance(), R.id.child_fragment_container);
    }
}
