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
import android.view.View;
import android.view.ViewGroup;

import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.R;

import java.util.ArrayList;

public class SchedulesFrgment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager view_pager;

    public static SchedulesFrgment newInstance() {
        return new SchedulesFrgment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedules, container, false); // 여기서 UI를 생성해서 View를 return
        initView(v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initTapControl();
        initViewPager();
        super.onActivityCreated(savedInstanceState);
    }

    private void initViewPager() {
        ArrayList<Fragment> fl = new ArrayList<>();

        for (int i = 0; i < CommonData.WEEK_DATA_LIST.length; i++) {
            SchedulesViewPagerFragment viewPagerFragment = SchedulesViewPagerFragment.Instance();
            fl.add(viewPagerFragment);
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
            SchedulesViewPagerFragment fragment = (SchedulesViewPagerFragment) fragmentList.get(i);
            fragment.setTv_Text(CommonData.WEEK_DATA_LIST[i]);
            return fragment;
        }

        @Override
        public int getCount() {
            return CommonData.WEEK_DATA_LIST.length;
        }
    }

    private void initTapControl() {
        for (String dayName : CommonData.WEEK_DATA_LIST) {
            tabLayout.addTab(tabLayout.newTab().setText(dayName));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });
    }

    private void initView(View v) {
        tabLayout = v.findViewById(R.id.tab_layout);
        view_pager = v.findViewById(R.id.view_pager);
    }
}
