package com.bpm202.SensorProject.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class UtilForApp {

    public static void setDividerItemDecoration(@NonNull Context context, @NonNull RecyclerView view, int resourceId) {
        DividerItemDecoration decoration = new DividerItemDecoration(context, new LinearLayoutManager(context).getOrientation());
        decoration.setDrawable(context.getResources().getDrawable(resourceId));
        view.addItemDecoration(decoration);
    }
}
