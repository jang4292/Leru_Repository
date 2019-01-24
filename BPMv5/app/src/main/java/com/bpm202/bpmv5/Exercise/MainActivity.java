package com.bpm202.bpmv5.Exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


import com.bpm202.bpmv5.Data.SignInRepository;
import com.bpm202.bpmv5.BaseActivity;
import com.bpm202.bpmv5.R;
import com.bpm202.bpmv5.Util.QToast;

public class MainActivity extends BaseActivity {

    public static final String TAG = SignInRepository.class.getSimpleName();

    private long mLastClick = 0;
    private final long CLICK_DELAY = 2000;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navivationView;
    private CustomActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }


    private void initView() {
        setContentView(R.layout.activity_exercise);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor, null));
        toolbar.setTitle(R.string.menu_exercise);


        navivationView = findViewById(R.id.navivation_view);
        navivationView.setNavigationItemSelectedListener(onNavigationItemSelected);
        drawerLayout = findViewById(R.id.drawer_layout);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.add(R.id.fragment_container, ExerciseFrgment.newInstance()).commit();
        fragmentTransaction.add(R.id.fragment_container, ExerciseFrgment.newInstance()).commit();
    }

    private void initListener() {
        toggle = new CustomActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d(TAG, "onBackStackChanged: ");

                Log.d(TAG, "onBackStackChanged getFragmentManager().getBackStackEntryCount() : " + getSupportFragmentManager().getBackStackEntryCount());

                int i = getSupportFragmentManager().getBackStackEntryCount();

                Log.d(TAG, "TEST , I : " + i);
            }
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

                    toolbar.setTitle(R.string.menu_exercise);
                    if (fm instanceof ExerciseFrgment) {
                        break;
                    }
                    replaceFragment(ExerciseFrgment.newInstance());
                    break;

                case R.id.navigation_menu_item_schedules:

                    toolbar.setTitle(R.string.menu_schedules);
                    if (fm instanceof SchedulesFrgment) {
                        break;
                    }
                    replaceFragment(SchedulesFrgment.newInstance());

                    break;

                case R.id.navigation_menu_item_history:

                    toolbar.setTitle(R.string.menu_history);
                    if (fm instanceof HistoryFrgment) {
                        break;
                    }
                    replaceFragment(HistoryFrgment.newInstance());
                    break;


                case R.id.navigation_menu_item_setting:
                    //mActivity.startActivity(new Intent(mActivity, SettingActivity.class));
                    toolbar.setTitle(R.string.menu_setting);
                    if (fm instanceof SettingFragment) {
                        break;
                    }
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

    // Fragment 변환을 해주기 위한 부분, Fragment의 Instance를 받아서 변경
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
