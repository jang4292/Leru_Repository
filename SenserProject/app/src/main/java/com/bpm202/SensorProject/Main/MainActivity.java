package com.bpm202.SensorProject.Main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.bpm202.SensorProject.BaseActivity;
import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.Exercise.ExerciseFrgment;
import com.bpm202.SensorProject.Main.History.HistoryFragment;
import com.bpm202.SensorProject.Main.Schedules.SchedulesFrgment;
import com.bpm202.SensorProject.Main.Settings.SettingFragment;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.List;

public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private long mLastClick = 0;
    private final long CLICK_DELAY = 2000;
    private DrawerLayout drawerLayout;
    private NavigationView navivationView;


    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.activity_exercise;
    }

    @Override
    protected void init() {
        initView();
        initData();
    }

    @NonNull
    @Override
    protected String getTitleText() {
        return getString(R.string.menu_exercise);
    }


    private void initData() {
        ScheduleRepository repository = ScheduleRepository.getInstance();
        Util.LoadingProgress.show(MainActivity.this);
        repository.getSchedules(new ScheduleDataSource.LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
                Util.LoadingProgress.hide();
                MainDataManager.Instance().setListScheduleValueObject(scheduleVos);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, ExerciseFrgment.Instance()).commit();
            }

            @Override
            public void onDataNotAvailable() {
                Util.LoadingProgress.hide();
                Log.e(MainActivity.TAG, "[SchedulesFragment] getSchedules onDataNotAvailable");
            }
        });

    }


    private void initView() {
        navivationView = findViewById(R.id.navivation_view);
        navivationView.setNavigationItemSelectedListener(onNavigationItemSelected);
        drawerLayout = findViewById(R.id.drawer_layout);

        new CustomActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout, getToolbar(),
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SchedulesFrgment.DestroyInstance();
        HistoryFragment.DestroyInstance();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                Fragment fm = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fm instanceof ExerciseFrgment) {
                    if (mLastClick < System.currentTimeMillis() - CLICK_DELAY) {
                        mLastClick = System.currentTimeMillis();
                        QToast.showToast(getApplicationContext(), R.string.exit_button);
                        return;
                    }
                }
            }
            super.onBackPressed();
        }
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            menuItem.setChecked(true);

            Fragment fm = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            switch (menuItem.getItemId()) {
                case R.id.navigation_menu_item_exerciese:

                    setTitle(R.string.menu_exercise);
                    if (fm instanceof ExerciseFrgment) {
                        break;
                    }
                    Util.FragmentUtil.replaceFragment(getSupportFragmentManager(), ExerciseFrgment.Instance(), R.id.fragment_container);
                    break;

                case R.id.navigation_menu_item_schedules:

                    setTitle(R.string.menu_schedules);
                    if (fm instanceof SchedulesFrgment) {
                        break;
                    }
                    Util.FragmentUtil.replaceFragment(getSupportFragmentManager(), SchedulesFrgment.Instance(), R.id.fragment_container);

                    break;

                case R.id.navigation_menu_item_history:

                    setTitle(R.string.menu_history);
                    if (fm instanceof HistoryFragment) {
                        break;
                    }
                    Util.FragmentUtil.replaceFragment(getSupportFragmentManager(), HistoryFragment.newInstance(), R.id.fragment_container);
                    break;


                case R.id.navigation_menu_item_setting:
                    setTitle(R.string.menu_setting);
                    if (fm instanceof SettingFragment) {
                        break;
                    }
                    Util.FragmentUtil.replaceFragment(getSupportFragmentManager(), SettingFragment.newInstance(), R.id.fragment_container);
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
    };

    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

        public CustomActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
            syncState();
        }
    }
}
