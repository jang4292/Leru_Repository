package com.bpm202.SensorProject.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.bpm202.SensorProject.R;


public class CircleView extends AppCompatImageView {

    private static final int START_ANGLE_POINT = -90;

    private Paint whiltePaint;
    private Paint paint;
    private RectF rect;

    private float angle;
    private float strokeWidth = 40;

    public CircleView(Context context) {
        super(context);
        init(context);
    }

    public CircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        paint = createPaint(context.getResources().getColor(R.color.lineColor));
        whiltePaint = createPaint(Color.LTGRAY);
        rect = new RectF();
        angle = 0;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setRectResize(float viewSize, float strokeWidth) {
        float strokeHalf = strokeWidth/2;
        this.strokeWidth = strokeWidth;
        this.rect.left = strokeHalf;
        this.rect.top = strokeHalf;
        this.rect.right = viewSize - strokeHalf;
        this.rect.bottom = viewSize - strokeHalf;
        this.paint.setStrokeWidth(strokeWidth);
        this.whiltePaint.setStrokeWidth(strokeWidth);
    }

    private Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        return paint;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rect, 0, 360, true, whiltePaint);
        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
    }

    public void startAnimation(CircleCallback callback) {

        CircleAngleAnimation animation = new CircleAngleAnimation(callback);
        startAnimation(animation);
    }

    public interface CircleCallback {
        float getNewAngle();
    }

    private class CircleAngleAnimation extends Animation {

        private static final long COUNT_ANIMATION_DELAY = 200;

        private float oldAngle;
        private float newAngle;

        public CircleAngleAnimation(CircleCallback callback) {
            oldAngle = getAngle();
            newAngle = callback.getNewAngle();
            setDuration(COUNT_ANIMATION_DELAY - 50);


            setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO
                /*mPresent.endAnimation();
                if (restTimeEnable) {
                    updateRest(restTime);
                }*/
                    setAnimationListener(null);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);
            setAngle(angle);
            requestLayout();
        }
    }
}
