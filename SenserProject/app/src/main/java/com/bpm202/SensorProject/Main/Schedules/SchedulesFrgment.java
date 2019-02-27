package com.bpm202.SensorProject.Main.Schedules;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bpm202.SensorProject.CustomView.CustomViewPager;
import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.Main.MainDataManager;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.Data.DayOfWeek;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.ArrayList;
import java.util.List;

public class SchedulesFrgment extends SchdulesBaseFragment {

    private static SchedulesFrgment instance = null;
    private ViewPagerAdapter viewPagerAdapter;

    public static SchedulesFrgment Instance() {
        if (instance == null) {
            instance = new SchedulesFrgment();
        }
        return instance;
    }

    public static void DestroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    private TabLayout tabLayout;
    private CustomViewPager view_pager;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_schedules;
    }

    @NonNull
    @Override
    protected void initView(View v) {
        tabLayout = v.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

        view_pager = v.findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        view_pager.setAdapter(viewPagerAdapter);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (SchdulesManager.Instance().STATE_CLASS.getCurrentState().equals(SchdulesManager.STATE.DEFAULT)) {
                    SchdulesManager.Instance().setCurrentPageOfDay(i);
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        SchdulesManager.Instance().setSTATE(SchdulesManager.STATE.DEFAULT);
        super.onActivityCreated(savedInstanceState);
    }

    private void loadSchedulesData() {
        Util.LoadingProgress.show(getContext());
        ScheduleRepository.getInstance().getSchedules(new ScheduleDataSource.LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
                Util.LoadingProgress.hide();
                if (scheduleVos == null) {
                    Log.e(MainActivity.TAG, "[TEST] , Data is null");
                } else {
                    MainDataManager.Instance().setListScheduleValueObject(scheduleVos);
                    SchdulesManager.Instance().setSTATE(SchdulesManager.STATE.DEFAULT);
                }
            }

            @Override
            public void onDataNotAvailable() {
                Util.LoadingProgress.hide();
                Log.e(MainActivity.TAG, "[SchedulesFragment] getSchedules onDataNotAvailable");
            }
        });
    }

    @Override
    public void UpdateState(SchdulesManager.STATE cur, SchdulesManager.STATE pre) {
        if (cur.equals(SchdulesManager.STATE.RELOAD)) {
            loadSchedulesData();
        } else if (cur.equals(SchdulesManager.STATE.DEFAULT)) {
            if (pre.equals(SchdulesManager.STATE.MODIFY)
                    || pre.equals(SchdulesManager.STATE.DELETE)) {
                initViewPager(cur);
                view_pager.setPagingEnabled(true);
            } else if (pre.equals(SchdulesManager.STATE.RELOAD)) {
                initTapControl(cur);
                initViewPager(cur);
                view_pager.setPagingEnabled(true);
            } else {
                initTapControl(cur);
                initViewPager(cur);
                view_pager.setPagingEnabled(true);
            }
        } else if (cur.equals(SchdulesManager.STATE.ADD)) {
            initTapControl(cur);
            initViewPager(cur);
            view_pager.setPagingEnabled(true);
        } else if (cur.equals(SchdulesManager.STATE.MODIFY)
                || cur.equals(SchdulesManager.STATE.DELETE)) {
            view_pager.setPagingEnabled(false);
        }
        getActivity().invalidateOptionsMenu();
    }

    private void initViewPager(SchdulesManager.STATE curState) {
        ArrayList<Fragment> fl = SchdulesManager.Instance().initViewPager();
        if (curState.equals(SchdulesManager.STATE.DEFAULT)) {
            for (int i = 0; i < CommonData.WEEK_DATA_LIST.length; i++) {
                DayOfWeek[] dayOfWeek = DayOfWeek.values();
                List<ScheduleValueObject> objs = MainDataManager.Instance().getScheduleValueObjectForDay(dayOfWeek[i]);
                Fragment fragment = fl.get(i);
                if (fragment instanceof SchedulesViewPagerFragment) {
                    ((SchedulesViewPagerFragment) fragment).setData(objs);
                }
            }
        } else if (curState.equals(SchdulesManager.STATE.ADD)) {
            for (int i = 0; i < CommonData.TRAIN_LIST.length; i++) {
                Fragment fragment = fl.get(i);
                if (fragment instanceof SchedulesAddViewPagerFragment) {
                    ((SchedulesAddViewPagerFragment) fragment).setTabPositionFromParent(i);
                }
            }
        }

        viewPagerAdapter.notifyDataSetChanged(fl);
        if (curState.equals(SchdulesManager.STATE.DEFAULT)) {
            view_pager.setCurrentItem(SchdulesManager.Instance().getCurrentPageOfDay());
        }
    }


    private void initTapControl(SchdulesManager.STATE curState) {
        if (tabLayout != null) {
            tabLayout.removeAllTabs();
        }

        if (curState.equals(SchdulesManager.STATE.DEFAULT)) {
            for (String dayName : CommonData.WEEK_DATA_LIST) {
                tabLayout.addTab(tabLayout.newTab().setText(dayName));
            }
        } else if (curState.equals(SchdulesManager.STATE.ADD)) {
            for (String trainName : CommonData.TRAIN_LIST) {
                tabLayout.addTab(tabLayout.newTab().setText(trainName));
            }
        }

    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            view_pager.setCurrentItem(position);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.schedules_edit_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (SchdulesManager.Instance().STATE_CLASS.getCurrentState().equals(SchdulesManager.STATE.DEFAULT)) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
        } else if (SchdulesManager.Instance().STATE_CLASS.getCurrentState().equals(SchdulesManager.STATE.ADD)
                || SchdulesManager.Instance().STATE_CLASS.getCurrentState().equals(SchdulesManager.STATE.MODIFY)
                || SchdulesManager.Instance().STATE_CLASS.getCurrentState().equals(SchdulesManager.STATE.DELETE)) {
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
                break;
            case R.id.menu_modify:
                SchdulesManager.Instance().setSTATE(SchdulesManager.STATE.MODIFY);
                break;
            case R.id.menu_delete:
                SchdulesManager.Instance().setSTATE(SchdulesManager.STATE.DELETE);
                break;
            case R.id.menu_add:
                SchdulesManager.Instance().setSTATE(SchdulesManager.STATE.ADD);
                break;
            case R.id.action_add:
                SchdulesManager.Instance().setSTATE(SchdulesManager.STATE.DEFAULT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        @NonNull
        private ArrayList<Fragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void notifyDataSetChanged(@NonNull ArrayList<Fragment> fragmentList) {
            this.fragmentList = fragmentList;
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList != null ? fragmentList.get(i) : null;
        }

        @Override
        public int getCount() {
            return fragmentList != null ? fragmentList.size() : 0;
        }
    }
}
