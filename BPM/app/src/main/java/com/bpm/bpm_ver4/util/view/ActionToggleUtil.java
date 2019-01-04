package com.bpm.bpm_ver4.util.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.auth.setting.SettingActivity;
import com.bpm.bpm_ver4.exercise.MainActivity;
import com.bpm.bpm_ver4.exercise.history.HistoryActivity;
import com.bpm.bpm_ver4.exercise.schedule.SchedulesActivity;

public class ActionToggleUtil extends ActionBarDrawerToggle implements NavigationView.OnNavigationItemSelectedListener {

    private AppCompatActivity mActivity;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private boolean mToolBarNavigationListenerIsRegistered = false;

    public ActionToggleUtil(AppCompatActivity activity, NavigationView navigationView,
                            DrawerLayout drawerLayout, Toolbar toolbar,
                            int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        mActivity = activity;
        mNavigationView = navigationView;
        mDrawerLayout = drawerLayout;
        mDrawerLayout.addDrawerListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        syncState();
    }

    public void setCheck(int id) {
        mNavigationView.getMenu().findItem(id).setChecked(true);
    }

    public MenuItem getMenuItem(int id) {
        return mNavigationView.getMenu().findItem(id);
    }


    public void toggleLockMode(boolean enable) {
        if (!enable) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            setDrawerIndicatorEnabled(false);
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (!mToolBarNavigationListenerIsRegistered) {
                setToolbarNavigationClickListener(v -> mActivity.onBackPressed());
                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setDrawerIndicatorEnabled(true);
            setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_play :
                if (mActivity instanceof MainActivity)
                    break;
                NavUtils.navigateUpFromSameTask(mActivity);
                break;

            case R.id.nav_schedules :
                if (mActivity instanceof SchedulesActivity)
                    break;
                mActivity.startActivity(new Intent(mActivity, SchedulesActivity.class));
                break;

            case R.id.nav_history :
                if (mActivity instanceof HistoryActivity)
                    break;
                mActivity.startActivity(new Intent(mActivity, HistoryActivity.class));
                break;

            case R.id.nav_setting :
                mActivity.startActivity(new Intent(mActivity, SettingActivity.class));
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
