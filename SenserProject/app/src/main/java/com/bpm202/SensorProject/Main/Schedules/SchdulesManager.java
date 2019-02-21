package com.bpm202.SensorProject.Main.Schedules;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.Main.MainActivity;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class SchdulesManager {

    private static SchdulesManager instance = null;
    private static StateChangedObservable observable = null;

    private ArrayList<SchedulesViewPagerFragment> viewPagerFragmentList = null;
    private ArrayList<SchedulesAddViewPagerFragment> addViewPagerFragment = null;

    public static SchdulesManager Instance() {
        return (instance == null) ? new SchdulesManager() : instance;
    }

    private ArrayList<SchedulesViewPagerFragment> getViewPagerFragmentList() {
        if (viewPagerFragmentList == null) {
            viewPagerFragmentList = new ArrayList<>();
            for (int i = 0; i < CommonData.WEEK_DATA_LIST.length; i++) {
                viewPagerFragmentList.add(SchedulesViewPagerFragment.Instance());
            }
        }
        return viewPagerFragmentList;
    }

    private ArrayList<SchedulesAddViewPagerFragment> getAddViewPagerFragment() {
        if (addViewPagerFragment == null) {
            addViewPagerFragment = new ArrayList<>();
            for (int i = 0; i < CommonData.TRAIN_LIST.length; i++) {
                addViewPagerFragment.add(SchedulesAddViewPagerFragment.Instance());
            }
        }
        return addViewPagerFragment;
    }

    ArrayList<Fragment> initViewPager() {
        ArrayList<Fragment> fl = new ArrayList<>();
        if (STATE_CLASS.getCurrentState().equals(SchdulesManager.STATE.DEFAULT)) {
            for (int i = 0; i < CommonData.WEEK_DATA_LIST.length; i++) {
                SchedulesViewPagerFragment viewPagerFragment = SchdulesManager.Instance().getViewPagerFragmentList().get(i);
                fl.add(viewPagerFragment);
            }
        } else if (STATE_CLASS.getCurrentState().equals(SchdulesManager.STATE.ADD)) {
            for (int i = 0; i < CommonData.TRAIN_LIST.length; i++) {
                SchedulesAddViewPagerFragment viewPagerFragment = SchdulesManager.Instance().getAddViewPagerFragment().get(i);
                fl.add(viewPagerFragment);
            }
        }
        return fl;
    }

    public void addObserver(Observer o) {
        if (observable == null) {
            observable = new StateChangedObservable();
        }
        observable.addObserver(o);
    }

    public void deleteObserver(Observer o) {
        if (observable != null) {
            observable.deleteObserver(o);
            if (observable.countObservers() < 1) {
                observable = null;
            }
        }
    }

    public static StateClass STATE_CLASS = new StateClass();

    public enum STATE {ADD, MODIFY, DEFAULT, RELOAD, DELETE, NONE}

    static class StateClass {
        private STATE currentState = STATE.NONE;
        private STATE previousState = STATE.NONE;

        private void setState(STATE state) {
            Log.i(MainActivity.TAG, "[SchdulesManager] pre : " + previousState + " to " + currentState);
            Log.i(MainActivity.TAG, "[SchdulesManager] cur : " + currentState + " to " + state);
            previousState = currentState;
            currentState = state;
        }

        public STATE getCurrentState() {
            return currentState;
        }

        public STATE getPreviousState() {
            return previousState;
        }
    }

    public void setSTATE(STATE state) {
        STATE_CLASS.setState(state);

        if (observable != null) {
            observable.notifyObservers();
        }
    }

    private static int currentPage = 0;

    public void setCurrentPageOfDay(int i) {
        currentPage = i;
    }

    public int getCurrentPageOfDay() {
        return currentPage;
    }

    private static class StateChangedObservable extends Observable {
        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers(STATE_CLASS);
        }
    }
}
