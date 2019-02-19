package com.bpm202.SensorProject.Main.History;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpm202.SensorProject.R;

public class HistoryFragment extends Fragment {

    private static HistoryFragment instance;

    public static HistoryFragment newInstance() {

        if (instance == null) {
            instance = new HistoryFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false); // 여기서 UI를 생성해서 View를 return
        initPreView(v);
        return v;
    }

    private void initPreView(View v) {
        replace(true);
    }

    void replace(boolean isCalendar) {
        if (isCalendar) {
            Fragment childFragment = HistoryCalendarFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.child_fragment_container, childFragment);
            transaction.commit();

        } else {
            Fragment childFragment = HistoryDateFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.child_fragment_container, childFragment);
            transaction.addToBackStack(childFragment.getTag());
            transaction.commit();
        }
    }
}
