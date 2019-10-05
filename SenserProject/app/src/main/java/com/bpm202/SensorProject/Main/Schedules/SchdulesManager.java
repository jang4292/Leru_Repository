package com.bpm202.SensorProject.Main.Schedules;

import android.support.v4.app.Fragment;

import com.bpm202.SensorProject.Data.CommonData;

import java.util.ArrayList;

public class SchdulesManager {

    private static SchdulesManager instance = null;

    private ArrayList<PlansViewPagerFragment> viewPagerFragmentList = null;

    private ArrayList<Fragment> fl = new ArrayList<>();

    public static SchdulesManager Instance() {
        if (instance == null) {
            instance = new SchdulesManager();
        }
        return instance;
    }

    private ArrayList<PlansViewPagerFragment> getViewPagerFragmentList() {
        if (viewPagerFragmentList == null) {
            viewPagerFragmentList = new ArrayList<>();
            for (int i = 0; i < CommonData.WEEK_DATA_LIST.length; i++) {
                if (viewPagerFragmentList.size() != CommonData.WEEK_DATA_LIST.length) {
                    viewPagerFragmentList.add(PlansViewPagerFragment.Instance());
                }
            }
        }
        return viewPagerFragmentList;
    }

    public ArrayList<Fragment> initViewPager() {
        if (fl.size() != CommonData.WEEK_DATA_LIST.length) {
            for (int i = 0; i < CommonData.WEEK_DATA_LIST.length; i++) {
                PlansViewPagerFragment viewPagerFragment = SchdulesManager.Instance().getViewPagerFragmentList().get(i);
                fl.add(viewPagerFragment);
            }
        }
        return fl;
    }
}
