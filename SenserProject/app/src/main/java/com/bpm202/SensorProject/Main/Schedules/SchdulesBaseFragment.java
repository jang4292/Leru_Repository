package com.bpm202.SensorProject.Main.Schedules;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bpm202.SensorProject.BaseFragment;

import java.util.Observable;
import java.util.Observer;

interface UpdateObserver {
    void UpdateState(SchdulesManager.STATE cur, SchdulesManager.STATE pre);
}

public abstract class SchdulesBaseFragment extends BaseFragment implements Observer, UpdateObserver {

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof SchdulesManager.StateClass) {
            SchdulesManager.STATE cur = ((SchdulesManager.StateClass) arg).getCurrentState();
            SchdulesManager.STATE pre = ((SchdulesManager.StateClass) arg).getPreviousState();
            UpdateState(cur, pre);
        }
    }

    @Override
    public void onAttach(Context context) {
        SchdulesManager.Instance().addObserver(this);
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        SchdulesManager.Instance().deleteObserver(this);
        super.onDestroyView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
