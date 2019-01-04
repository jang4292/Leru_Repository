package com.bpm.bpm_ver4.util.anim;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.bpm.bpm_ver4.R;

public class LayoutExercise extends ConstraintLayout {

    private CircleView circleView;
    private TextView textView;
    private int index = -1;

    public LayoutExercise(Context context) {
        super(context);

        init(context);
    }

    public LayoutExercise(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_exercise, this, true);

        circleView = findViewById(R.id.iv_exercise_icon);
        textView = findViewById(R.id.text_view);
    }


    public CircleView getCircleView() {
        return circleView;
    }


    public void setIndex(int index) {
        this.index = index;
    }


    public int getIndex() {
        return index;
    }
}
