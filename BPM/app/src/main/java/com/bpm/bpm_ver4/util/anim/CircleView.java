package com.bpm.bpm_ver4.util.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bpm.bpm_ver4.R;

public class CircleView extends AppCompatImageView {

    private static final int START_ANGLE_POINT = -90;

    private final Paint whiltePaint;
    private final Paint paint;
    private final RectF rect;

    private float angle;
    private float strokeWidth = 40;

    public CircleView(Context context) {
        super(context);
        paint = createPaint(context.getResources().getColor(R.color.lineColor));
        whiltePaint = createPaint(Color.LTGRAY);
        rect = new RectF();
        angle = 0;
    }

    public CircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
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
}
