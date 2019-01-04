package com.bpm.bpm_ver4.util.anim;

import java.util.ArrayList;

public class StateViewArrayList {

    public int activeIndex;

    public ArrayList<StateView> stateViews;

    public StateViewArrayList() {
        activeIndex = 0;
        stateViews = new ArrayList<>();
    }
}
