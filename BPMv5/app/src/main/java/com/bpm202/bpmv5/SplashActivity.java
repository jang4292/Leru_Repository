package com.bpm202.bpmv5;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.bpm202.bpmv5.Account.LoginActivity;

public class SplashActivity extends BaseActivity {

    private LinearLayout content_layer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();

    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        content_layer = findViewById(R.id.content_layer);
        //.setOnTouchListener(this);
    }

    private void initListener() {
        if (content_layer != null) {
            content_layer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                            return true;
                    }

                    return false;
                }
            });
        }
    }
}
