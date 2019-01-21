package com.bpm202.bpmv5.Exercise;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


import com.bpm202.bpmv5.API.SignInRepository;
import com.bpm202.bpmv5.BaseActivity;
import com.bpm202.bpmv5.R;
import com.bpm202.bpmv5.Util.QToast;

public class ExerciseActivity extends BaseActivity {

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
    }

    private void initView() {
        setContentView(R.layout.activity_exercise);

        toolbar = findViewById(R.id.toolbar);
        navivationView = findViewById(R.id.navivation_view);
        navivationView.setNavigationItemSelectedListener(onNavigationItemSelected);
        drawerLayout = findViewById(R.id.drawer_layout);


        toggle = new CustomActionBarDrawerToggle(
                ExerciseActivity.this,
                drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else if (mLastClick < System.currentTimeMillis() - CLICK_DELAY) {             /* 클릭 딜레이 2000ms 연속 클릭시 여기까지 실행되고 RETURN */
            mLastClick = System.currentTimeMillis();
            QToast.showToast(getApplicationContext(), R.string.exit_button);
            return; // RETURN;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Log.d(TAG, "onNavigationItemSelected : " + menuItem.toString());
            switch (menuItem.getItemId()) {
                case R.id.navigation_menu_item_exerciese:
                    //if (mActivity instanceof MainActivity)
                    //break;
                    //NavUtils.navigateUpFromSameTask(mActivity);
                    break;

                case R.id.navigation_menu_item_schedules:
                    //if (mActivity instanceof SchedulesActivity)
                    //break;
                    //startActivity(new Intent(mActivity, SchedulesActivity.class));
                    break;

                case R.id.navigation_menu_item_history:
                    //if (mActivity instanceof HistoryActivity)
                    break;
                //mActivity.startActivity(new Intent(mActivity, HistoryActivity.class));
                //break;

                case R.id.navigation_menu_item_setting:
                    //mActivity.startActivity(new Intent(mActivity, SettingActivity.class));
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
