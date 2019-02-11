package com.bpm202.SensorProject.Exercise;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.ValueObject.DayOfWeek;

import java.util.ArrayList;

public class SchedulesFrgment extends Fragment {

    private static SchedulesFrgment instance = null;

    public static SchedulesFrgment Instance() {
        if (instance == null) {
            instance = new SchedulesFrgment();
        }
        return instance;
    }

    private DayOfWeek currentDay;

    private TabLayout tabLayout;
    private ViewPager view_pager;
    private STATE state = STATE.DEFAULT;

    public enum STATE {ADD, MODIFY, DEFAULT}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedules, container, false); // 여기서 UI를 생성해서 View를 return
        initView(v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        notifyDateSetChanged(STATE.DEFAULT);
        super.onActivityCreated(savedInstanceState);
    }

    void notifyDateSetChanged(STATE state) {
        this.state = state;
        initTapControl();
        initViewPager();
        getActivity().invalidateOptionsMenu();
    }

    private void initViewPager() {
        ArrayList<Fragment> fl = new ArrayList<>();

        if (state.equals(STATE.DEFAULT)) {
            for (int i = 0; i < CommonData.WEEK_DATA_LIST.length; i++) {
                SchedulesViewPagerFragment viewPagerFragment = SchedulesViewPagerFragment.Instance();
                fl.add(viewPagerFragment);
            }
        } else if (state.equals(STATE.ADD)) {
            for (int i = 0; i < CommonData.TRAIN_LIST.length; i++) {
                SchedulesAddViewPagerFragment viewPagerFragment = SchedulesAddViewPagerFragment.Instance();
                viewPagerFragment.setTabPositionFromParent(i);
                viewPagerFragment.setDayOfWeekFromParent(DayOfWeek.findByCode(tabLayout.getSelectedTabPosition() + 1));

                fl.add(viewPagerFragment);
            }
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), fl);
        view_pager.setAdapter(viewPagerAdapter);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        @NonNull
        private ArrayList<Fragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentManager fm, @NonNull ArrayList<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int i) {
            Log.i(MainActivity.TAG, "ViewPagerAdapter getItem position : " + i);
            return fragmentList != null ? fragmentList.get(i) : null;
        }

        @Override
        public int getCount() {
            if (state.equals(STATE.DEFAULT)) {
                return CommonData.WEEK_DATA_LIST.length;
            } else {
                return CommonData.TRAIN_LIST.length;
            }
        }
    }

    private void initTapControl() {

        if (tabLayout != null) {
            tabLayout.removeAllTabs();
            tabLayout.removeOnTabSelectedListener(onTabSelectedListener);
        }

        if (state.equals(STATE.DEFAULT)) {
            for (String dayName : CommonData.WEEK_DATA_LIST) {
                tabLayout.addTab(tabLayout.newTab().setText(dayName));
            }
        } else if (state.equals(STATE.ADD)) {
            for (String trainName : CommonData.TRAIN_LIST) {
                tabLayout.addTab(tabLayout.newTab().setText(trainName));
            }
        }
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.d(MainActivity.TAG, "TEST onTabSelected : tab :: " + tab.getPosition());
            int position = tab.getPosition();
            view_pager.setCurrentItem(position);
        }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(MainActivity.TAG, "TEST onTabUnselected : tab :: " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(MainActivity.TAG, "TEST onTabReselected : tab :: " + tab.getPosition());
            }
    };

    private void initView(View v) {
        tabLayout = v.findViewById(R.id.tab_layout);
        view_pager = v.findViewById(R.id.view_pager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.schedules_edit_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (state.equals(STATE.DEFAULT)) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
        } else if (state.equals(STATE.ADD)) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        } else {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_modify:
                Log.d(MainActivity.TAG, "TEST item : action modify");
                break;
            case R.id.menu_modify:
                Log.d(MainActivity.TAG, "TEST item : menu_modify");
                break;
            case R.id.menu_add:
                Log.d(MainActivity.TAG, "TEST item : menu_add");
                notifyDateSetChanged(STATE.ADD);
                break;

            case R.id.action_add:
                Log.d(MainActivity.TAG, "TEST item : menu_add");
                notifyDateSetChanged(STATE.DEFAULT);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
