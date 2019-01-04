package com.bpm.bpm_ver4.exercise.schedule.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.util.ActivityUtils;
import com.bpm.bpm_ver4.vo.DayOfWeek;

public class SchedulesAddActivity extends BaseActivity {

    private String menuStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        setMenuStr(getString(R.string.button_add_text));
        int code = getIntent().getIntExtra(Common.DAY_OF_WEEK_CODE, 1);
        int pos = getIntent().getIntExtra(Common.SCHEDULES_SEQEUNCE, 0);
        DayOfWeek dayOfWeek = DayOfWeek.findByCode(code);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                SchedulesAddFragment.newInstance(dayOfWeek, pos),
                R.id.container_layout);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getMenuStr() {
        return menuStr;
    }

    public void setMenuStr(String menuStr) {
        this.menuStr = menuStr;
    }

    public void nextFragment(Fragment fragment) {
        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.container_layout
        );
    }
}
