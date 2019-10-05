package com.bpm202.SensorProject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initActionBar();
        init();
    }

    private void initActionBar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }


//        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
//        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black, null));
//        toolbar.setTitle();
//                setTitleText(R.string.menu_exercise);
        getSupportActionBar().setTitle(R.string.menu_exercise);


//        setSupportActionBar(toolbar);

//        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
//        getSupportActionBar().setTitle(R.string.title_plan_exericse_add);
//        getSupportActionBar().setTitle(getTitleText());

//        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor, null));
//        toolbar.setTitle(getTitleText());
//        toolbar.setNavigationIcon(R.drawable.common_back_btn_n);

//        update();

    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Deprecated
    public void setTitle(int resId) {
        if (toolbar != null) {
            toolbar.setTitle(resId);
        }

//        getSupportActionBar().setTitle(resId);
    }

    public void setTitleText(int resId) {
        if (toolbar != null) {
            toolbar.setTitle(resId);
        }

//        getSupportActionBar().setTitle(resId);
    }

//    public void update() {
//        if (toolbar != null) {
//            toolbar.setTitle(getTitleText());
//        }
//    }
//
//    @NonNull
//    protected abstract String getTitleText();

    @NonNull
    protected abstract int getLayoutId();

    protected abstract void init();
}
