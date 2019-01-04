package com.bpm.bpm_ver4.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwipeViewPager extends ViewPager {

    private final boolean ENABLE = true;

    public SwipeViewPager(@NonNull Context context) {
        super(context);
    }

    public SwipeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ENABLE) {
            return super.onInterceptTouchEvent(ev);
        } else {
            if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
                //ignore move action
            } else {
                if (super.onInterceptTouchEvent(ev)) {
                    super.onTouchEvent(ev);
                }
            }
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ENABLE) {
            return super.onTouchEvent(ev);
        } else {
            return ev.getActionMasked() != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);
        }
    }
}
