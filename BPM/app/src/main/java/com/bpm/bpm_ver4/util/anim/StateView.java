package com.bpm.bpm_ver4.util.anim;

import android.content.Context;
import android.view.View;

public class StateView extends CircleView {

    public int index = -1;
    public int backgroundRes;


    public StateView(Context context, int index) {
        super(context);
        this.index = index;
    }


    @Override
    public void setX(float x) {
        super.setX(x);
    }


    @Override
    public void setY(float y) {
        super.setY(y);
    }


}
