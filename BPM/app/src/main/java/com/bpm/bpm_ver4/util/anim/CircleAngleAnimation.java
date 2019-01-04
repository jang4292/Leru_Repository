package com.bpm.bpm_ver4.util.anim;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CircleAngleAnimation extends Animation {

    private CircleView circleView;

    private float oldAngle;
    private float newAngle;

    public CircleAngleAnimation(CircleView circleView, float newAngle) {
        this.oldAngle = circleView.getAngle();
        this.newAngle = newAngle;
        this.circleView = circleView;
    }

    public void setOldAngle(float oldAngle) {
        this.oldAngle = oldAngle;
    }

    public void setNewAngle(float newAngle) {
        this.newAngle = newAngle;
    }

    @Override
    public void setDuration(long durationMillis) {
        super.setDuration(durationMillis);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);

        circleView.setAngle(angle);
        circleView.requestLayout();
    }
}
