package com.bpm.bpm_ver4.exercise.schedule;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.BaseFragment;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.data.source.ScheduleDataSource;
import com.bpm.bpm_ver4.data.source.ScheduleRepository;
import com.bpm.bpm_ver4.exercise.schedule.add.SchedulesAddActivity;
import com.bpm.bpm_ver4.util.BackPressedItem;
import com.bpm.bpm_ver4.util.Observer;
import com.bpm.bpm_ver4.util.ViewPagerAdapter;
import com.bpm.bpm_ver4.util.view.ActionToggleUtil;
import com.bpm.bpm_ver4.vo.DayOfWeek;

import java.util.ArrayList;
import java.util.List;

public class SchedulesFragment extends BaseFragment implements SchedulesContract.View, BackPressedItem {

    public static final String TAG = SchedulesFragment.class.getSimpleName();

    // view
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fabAddSchedule;
    private ViewPagerAdapter adapter;
    private ActionToggleUtil mActionToggleUtil;
    private List<Observer> mObservable;

    private int pagerCursor;
    private boolean isFirst;
    private boolean isEditMode;

    // presenter
    private SchedulesContract.Presenter mPresenter;
    private SchedulesPresenter mSchedulesPresenter;

    public static SchedulesFragment newInstance(ActionToggleUtil actionToggleUtil) {
        SchedulesFragment fragment = new SchedulesFragment();
        fragment.mActionToggleUtil = actionToggleUtil;
        fragment.isFirst = true;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mObservable = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedules, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
        mSchedulesPresenter = new SchedulesPresenter(this);

        String[] dayOfWeekName = getResources().getStringArray(R.array.day_of_week_list);
        for (String dayName : dayOfWeekName) {
            tabLayout.addTab(tabLayout.newTab().setText(dayName));
        }

        adapter = new ViewPagerAdapter(getFragmentManager());
        for (int i = 0;  i < dayOfWeekName.length;  i++) {
            ExerciseViewPager exercisePager = ExerciseViewPager.newInstance();
            mObservable.add(exercisePager);
            exercisePager.setOnItemClickListener(onItemClickListener);
            adapter.addFragment(exercisePager, dayOfWeekName[i]);
        }

        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                pagerCursor = position;
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fabAddSchedule.setOnClickListener(v -> {
            ExerciseViewPager pager = (ExerciseViewPager) adapter.getItem(pagerCursor);
            Intent intent = new Intent(getContext(), SchedulesAddActivity.class);
            intent.putExtra(Common.DAY_OF_WEEK_CODE, pagerCursor + 1);
            intent.putExtra(Common.SCHEDULES_SEQEUNCE, pager.getScheduleCount() + 1);
            startActivity(intent);
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.schedules_edit_menu, menu);

        if (!isFirst) {
            isEditMode = false;
            for (Observer o : mObservable)
                o.update(isEditMode);
        }

        ((SchedulesActivity)getActivity()).setBackPressedItem(null);
        fabAddSchedule.setVisibility(View.VISIBLE);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        isEditMode = !isEditMode;
        for (Observer o : mObservable)
            o.update(isEditMode);

        if (isEditMode) {
            mActionToggleUtil.toggleLockMode(false);
            item.setTitle(R.string.button_complete);
            ((SchedulesActivity)getActivity()).setBackPressedItem(this);
            fabAddSchedule.setVisibility(View.INVISIBLE);
        } else {
            mActionToggleUtil.toggleLockMode(true);
            item.setTitle(R.string.button_edit);
            ((SchedulesActivity)getActivity()).setBackPressedItem(null);
            fabAddSchedule.setVisibility(View.VISIBLE);
        }

        Log.e("onOptionItemSelected", "실행");
        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            pagerCursor = App.getDayOfWeek().getCode();
            viewPager.setCurrentItem(--pagerCursor);
            isFirst = false;
        }

        mPresenter.loadSchedule();
    }

    // 뷰초기화
    private void initView(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        fabAddSchedule = view.findViewById(R.id.fab_add);

    }

    @Override
    public void updateSchedule(List<ScheduleVo> scheduleVos) {
        DayOfWeek[] dayOfWeek = DayOfWeek.values();
        for (int i = 0;  i < adapter.getCount(); i++) {
            ExerciseViewPager viewPager = (ExerciseViewPager) adapter.getItem(i);
            List<ScheduleVo> objs = new ArrayList<>();

            for (ScheduleVo obj : scheduleVos) {
                if (obj.getDay() == dayOfWeek[i]) {
                    objs.add(obj);
                }
            }

            viewPager.setSchedule(objs);
        }
    }

    @Override
    public void showMessage(int message) {
        showToast(message);
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void setPresenter(SchedulesContract.Presenter presenter) {
        this.mPresenter = presenter;
    }


    public void showNumberPicker(ScheduleVo obj, RecyclerView.Adapter adapter) {
        final int WEIGHT_MAX = 500;
        final int REST_MAX = 600;
        final int SET_MAX = 99;
        final int COUNT_MAX = 120;
        final int MIN_VALUE = 1;
        final int EMPTY_VALUE = 0;
        final Dialog d = new Dialog(getContext());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.layout_number_picker);
        final NumberPicker weightNumPicker, countNumPicker, setNumPicker, restNumPicker;
        weightNumPicker = d.findViewById(R.id.numberPicker1);
        restNumPicker  = d.findViewById(R.id.numberPicker2);
        setNumPicker    = d.findViewById(R.id.numberPicker3);
        countNumPicker   = d.findViewById(R.id.numberPicker4);

        weightNumPicker.setMaxValue(WEIGHT_MAX);
        weightNumPicker.setMinValue(EMPTY_VALUE);
        weightNumPicker.setValue(obj.getWeight());

        restNumPicker.setMaxValue(REST_MAX);
        restNumPicker.setMinValue(EMPTY_VALUE);
        restNumPicker.setValue(obj.getRest());

        setNumPicker.setMaxValue(SET_MAX);
        setNumPicker.setMinValue(MIN_VALUE);
        setNumPicker.setValue(obj.getSetCnt());

        countNumPicker.setMaxValue(COUNT_MAX);
        countNumPicker.setMinValue(MIN_VALUE);
        countNumPicker.setValue(obj.getCount());

        d.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            d.dismiss();
        });

        d.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            obj.setWeight(weightNumPicker.getValue());
            obj.setRest(restNumPicker.getValue());
            obj.setSetCnt(setNumPicker.getValue());
            obj.setCount(countNumPicker.getValue());
            adapter.notifyDataSetChanged();
            d.dismiss();

            ScheduleRepository repository = ScheduleRepository.getInstance();
            repository.updateSchedule(obj);
        });

        d.show();
    }



    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(ScheduleVo scheduleVo, RecyclerView.Adapter adapter) {
            showNumberPicker(scheduleVo, adapter);
        }
    };

    @Override
    public void onBack() {
        if (isEditMode) {
            getActivity().invalidateOptionsMenu();
            mActionToggleUtil.toggleLockMode(true);
        }
    }


    interface OnItemClickListener {
        void onItemClick(ScheduleVo scheduleVo, RecyclerView.Adapter adapter);
    }

}

