package com.bpm202.SensorProject.Main.History;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.CustomView.CustomCalendar;
import com.bpm202.SensorProject.Main.Exercise.ExerciseActivity;
import com.bpm202.SensorProject.Main.Schedules.WeeksPlanActivity;
import com.bpm202.SensorProject.Main.Settings.SettingActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.Util;

public class HistoryCalendarActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_history_calendar);
        initView();
//        initTapControl();
//        loadSchedulesData();

        CustomCalendar customCalendar = findViewById(R.id.gridview_days);
        TextView tvTotalDays = findViewById(R.id.tv_total_days);

        customCalendar.setOnClickListener(v1 -> {
//            Log.d("Test", "intent");
            Intent intent = new Intent(HistoryCalendarActivity.this, HistoryDateActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
//            Util.FragmentUtil.addFragmentToActivity(getFragmentManager(), HistoryDateFragment.newInstance(), R.id.child_fragment_container);
        });
        customCalendar.setOnTotalDaysTextViewListener(() -> tvTotalDays);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        getSupportActionBar().setTitle(R.string.title_history);

        initBottomMenu();
    }


    private void initBottomMenu() {

        btnBottomToday = findViewById(R.id.btn_bottom_today);
        btnBottomToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(WeeksPlanActivity.this, "today", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(true);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(false);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(true);

                Intent intent = new Intent(HistoryCalendarActivity.this, ExerciseActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        btnBottomPlan = findViewById(R.id.btn_bottom_plan);
        btnBottomPlan.setSelected(true);

        btnBottomPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(WeeksPlanActivity.this, "plan", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(true);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(false);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(true);

                Intent intent = new Intent(HistoryCalendarActivity.this, WeeksPlanActivity.class);
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
//                Toast.makeText(WeeksPlanActivity.this, "record", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(true);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(false);
                btnBottomSetting.setClickable(true);

                Intent intent = new Intent(HistoryCalendarActivity.this, HistoryCalendarActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        btnBottomSetting = findViewById(R.id.btn_bottom_setting);
        btnBottomSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(WeeksPlanActivity.this, "setting", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(true);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(false);

                Intent intent = new Intent(HistoryCalendarActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

    }
    /*private static HistoryCalendarActivity instance = null;

    public static HistoryCalendarActivity newInstance() {
        return instance == null ? new HistoryCalendarActivity() : instance;
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_calendar2;
    }

    @NonNull
    @Override
    protected void initView(View v) {
        CustomCalendar customCalendar = v.findViewById(R.id.gridview_days);
        TextView tvTotalDays = v.findViewById(R.id.tv_total_days);

//        customCalendar.setOnClickListener(v1 -> {
//            Util.FragmentUtil.addFragmentToActivity(getFragmentManager(), HistoryDateFragment.newInstance(), R.id.child_fragment_container);
//        });
        customCalendar.setOnTotalDaysTextViewListener(() -> tvTotalDays);
    }*/
}
