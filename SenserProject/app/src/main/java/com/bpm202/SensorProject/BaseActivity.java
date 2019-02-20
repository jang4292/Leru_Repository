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
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor, null));
        toolbar.setTitle(getTitleText());
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    public void setTitle(int resId) {
        if (toolbar != null) {
            toolbar.setTitle(resId);
        }
    }

    @NonNull
    protected abstract String getTitleText();

    @NonNull
    protected abstract int getLayoutId();

    protected abstract void init();
}
