package com.bpm202.SensorProject.Main.Temp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.Exercise.ExerciseActivity;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.List;

public class MainActivity1 extends Activity /*BaseActivity */ {
    /*@NonNull
    @Override
    protected String getTitleText() {
        return null;
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.activity_exercise;
    }


    @Override
    protected void init() {
        initView();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        ScheduleRepository repository = ScheduleRepository.getInstance();
        Util.LoadingProgress.show(MainActivity1.this);
        repository.getSchedules(new ScheduleDataSource.LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
                Util.LoadingProgress.hide();
                MainDataManager.Instance().setListScheduleValueObject(scheduleVos);

                Intent intent = new Intent(MainActivity1.this, ExerciseActivity.class);
                startActivity(intent);
                /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, ExerciseFrgment.Instance()).commit();*/
            }

            @Override
            public void onDataNotAvailable() {
                Util.LoadingProgress.hide();
                Log.e(MainActivity_sub.TAG, "[SchedulesFragment] getSchedules onDataNotAvailable");
            }
        });

    }

//    private void initView() {
//        navivationView = findViewById(R.id.navivation_view);
//        navivationView.setNavigationItemSelectedListener(onNavigationItemSelected);
//        drawerLayout = findViewById(R.id.drawer_layout);

        /*new MainActivity_sub.CustomActionBarDrawerToggle(
                MainActivity_sub.this,
                drawerLayout, getToolbar(),
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );*/

//        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
//        });
//    }
}
