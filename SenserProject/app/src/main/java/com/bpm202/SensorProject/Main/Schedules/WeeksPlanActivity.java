package com.bpm202.SensorProject.Main.Schedules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bpm202.SensorProject.CustomView.CustomViewPager;
import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.Data.DayOfWeek;
import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.MainActivity_sub;
import com.bpm202.SensorProject.Main.MainDataManager;
import com.bpm202.SensorProject.Main.Settings.SettingActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.ArrayList;
import java.util.List;

;

public class WeeksPlanActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private CustomViewPager view_pager;
    private ViewPagerAdapter viewPagerAdapter;
    private Button btnBottomPlan;
    private Button btnBottomSetting;
    private Button btnBottomRecord;
    private Button btnBottomToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    protected void init() {
        setContentView(R.layout.plan_activity);
        initView();
        initTapControl();
        loadSchedulesData();
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
        view_pager.setCurrentItem(SchdulesManager.Instance().getCurrentPageOfDay());
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
        Util.LoadingProgress.show(this);
        ScheduleRepository.getInstance().getSchedules(new ScheduleDataSource.LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
                Util.LoadingProgress.hide();
                if (scheduleVos == null) {
                    Log.e(MainActivity_sub.TAG, "[TEST] , Data is null");
                } else {
                    MainDataManager.Instance().setListScheduleValueObject(scheduleVos);
//                    SchdulesManager.Instance().setSTATE(SchdulesManager.STATE.DEFAULT);
                    initViewPager();
                }
            }

            @Override
            public void onDataNotAvailable() {
                Util.LoadingProgress.hide();
                Log.e(MainActivity_sub.TAG, "[SchedulesFragment] getSchedules onDataNotAvailable");
            }
        });
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        getSupportActionBar().setTitle(R.string.title_plan_exericse);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

        view_pager = findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
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
        initBottomMenu();
    }

    private void initBottomMenu() {

        btnBottomToday = findViewById(R.id.btn_bottom_today);
        btnBottomToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeeksPlanActivity.this, "today", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(true);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(false);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(true);

            }
        });
        btnBottomPlan = findViewById(R.id.btn_bottom_plan);
        btnBottomPlan.setSelected(true);
//        btnBottomPlan.setClickable(false);

        btnBottomPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeeksPlanActivity.this, "plan", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(true);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(false);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(true);

                Intent intent = new Intent(WeeksPlanActivity.this, WeeksPlanActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        btnBottomPlan.setClickable(false);

        btnBottomRecord = findViewById(R.id.btn_bottom_record);
        btnBottomRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeeksPlanActivity.this, "record", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(true);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(false);
                btnBottomSetting.setClickable(true);
            }
        });
        btnBottomSetting = findViewById(R.id.btn_bottom_setting);
        btnBottomSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeeksPlanActivity.this, "setting", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(true);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(false);

                Intent intent = new Intent(WeeksPlanActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plan_icon_menu, menu); // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true;
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

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        @NonNull
        private ArrayList<android.support.v4.app.Fragment> fragmentList;

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


}
