package com.bpm.bpm_ver4.exercise.schedule.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bpm.bpm_ver4.BaseFragment;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.util.ViewPagerAdapter;
import com.bpm.bpm_ver4.vo.DayOfWeek;
import com.bpm.bpm_ver4.vo.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulesAddFragment extends BaseFragment {


    public static final String TAG = SchedulesAddFragment.class.getSimpleName();

    public static final String BODY_WEIGHT = "Body weight";
    public static final String EQUIPMENTS = "Equipments";
    public static final String[] tabTitle = {
            BODY_WEIGHT, EQUIPMENTS
    };

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DayOfWeek dayOfWeek;
    private int pos;

    public static SchedulesAddFragment newInstance(DayOfWeek dayOfWeek, int pos) {
        SchedulesAddFragment fragment = new SchedulesAddFragment();
        fragment.dayOfWeek = dayOfWeek;
        fragment.pos = pos;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise_select, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = getView().findViewById(R.id.toolbar);

        SchedulesAddActivity activity = (SchedulesAddActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvToolbarTitle = activity.findViewById(R.id.tv_title_toolbar);
        tvToolbarTitle.setText(getString(R.string.title_schedules_add));

        viewPager = getView().findViewById(R.id.view_pager);
        tabLayout = getView().findViewById(R.id.tab_layout);

        Type[] types = Type.values();
        List<Type> bodyType = new ArrayList<>();
        List<Type> equipType = new ArrayList<>();
        for (int i = 0;  i < types.length; i++) {
            if (types[i].isEquipments()) {
                equipType.add(types[i]);
            } else {
                bodyType.add(types[i]);
            }
        }

        adapter = new ViewPagerAdapter(getFragmentManager());
        for (int i = 0;  i < tabTitle.length;  i++) {
            ScheuldesAddViewPager viewPager = new ScheuldesAddViewPager();
            if (tabTitle[i].equals(EQUIPMENTS)) {
                viewPager.initData(dayOfWeek, pos, equipType);
            } else {
                viewPager.initData(dayOfWeek, pos, bodyType);
            }
            adapter.addFragment(viewPager, tabTitle[i]);
            tabLayout.addTab(tabLayout.newTab().setText(tabTitle[i]));
        }

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
