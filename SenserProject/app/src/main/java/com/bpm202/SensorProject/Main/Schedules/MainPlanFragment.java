package com.bpm202.SensorProject.Main.Schedules;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.CustomView.CustomViewPager;
import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.Data.DayOfWeek;
import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.Main.Temp.MainDataManager;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainPlanFragment extends BaseFragment {


    private static MainPlanFragment instance = null;
    public static int CURRENT_DAY_OF_WEEK = -1;
    public static boolean isReload = false;

    public static MainPlanFragment newInstance() {
        if (instance == null) {
            instance = new MainPlanFragment();
        }
        return instance;
    }

    private TabLayout tabLayout;
    private CustomViewPager view_pager;
    private ViewPagerAdapter viewPagerAdapter;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_plan_main;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.plan_icon_menu, menu); // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {// MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case 3000:
                    Log.d("Test", "3000, Result OK");
                    loadSchedulesData();
                    break;
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case R.id.action_plan_icon:
            case R.id.action_add_icon:
                startActivityForResult(new Intent(getActivity(), PlanAddActivity.class), 3000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    protected void initView(View v) {
        ((MainActivity) getActivity()).setTitleText(R.string.menu_schedules);

        tabLayout = v.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);


        view_pager = v.findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        view_pager.setAdapter(viewPagerAdapter);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (CURRENT_DAY_OF_WEEK == -1) {
                    int day_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    CURRENT_DAY_OF_WEEK = day_week - 1;
                } else if (isReload) {
                    isReload = false;
                } else {
                    CURRENT_DAY_OF_WEEK = i;
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        initTapControl();
        loadSchedulesData();
    }

    private void initTapControl() {
        if (tabLayout != null) {
            tabLayout.removeAllTabs();
        }

        for (String dayName : CommonData.WEEK_DATA_LIST) {
            tabLayout.addTab(tabLayout.newTab().setText(dayName));
        }
    }

    private void loadSchedulesData() {
        ScheduleRepository.getInstance().getSchedules(new ScheduleDataSource.LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
                Util.LoadingProgress.hide();
                if (scheduleVos == null) {
                    Log.e(MainActivity.TAG, "[TEST] , Data is null");
                } else {
                    MainDataManager.Instance().setListScheduleValueObject(scheduleVos);
                    initViewPager();
                }
            }

            @Override
            public void onDataNotAvailable() {
                Util.LoadingProgress.hide();
                Log.e(MainActivity.TAG, "[SchedulesFragment] getSchedules onDataNotAvailable");
            }
        });
    }

    private void initViewPager() {
        ArrayList<Fragment> fl = SchdulesManager.Instance().initViewPager();

        for (int i = 0; i < CommonData.WEEK_DATA_LIST.length; i++) {
            DayOfWeek[] dayOfWeek = DayOfWeek.values();
            List<ScheduleValueObject> objs = MainDataManager.Instance().getScheduleValueObjectForDay(dayOfWeek[i]);

            Fragment fragment = fl.get(i);
            if (fragment instanceof PlansViewPagerFragment) {
                ((PlansViewPagerFragment) fragment).setData(objs);
            }
        }

        viewPagerAdapter.notifyDataSetChanged(fl);
        view_pager.setCurrentItem(CURRENT_DAY_OF_WEEK);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        @NonNull
        private ArrayList<Fragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        public void notifyDataSetChanged(@NonNull ArrayList<android.support.v4.app.Fragment> fragmentList) {
            this.fragmentList = fragmentList;
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }


        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            return fragmentList != null ? fragmentList.get(i) : null;
        }

        @Override
        public int getCount() {
            return fragmentList != null ? fragmentList.size() : 0;
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


}
