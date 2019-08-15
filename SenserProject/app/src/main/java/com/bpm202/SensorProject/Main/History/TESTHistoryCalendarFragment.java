package com.bpm202.SensorProject.Main.History;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.CustomView.CustomCalendar;
import com.bpm202.SensorProject.R;

import java.util.ArrayList;
import java.util.List;

public class TESTHistoryCalendarFragment extends BaseFragment {

    private static TESTHistoryCalendarFragment instance = null;
    private TextView mTvYear;
    private TextView mTvMonth;
    private CustomCalendar mGridView;

    public static TESTHistoryCalendarFragment newInstance() {
        return instance == null ? new TESTHistoryCalendarFragment() : instance;
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_calendar_test;
    }

    @NonNull
    @Override
    protected void initView(View v) {
        //mTvYear = v.findViewById(R.id.tv_year);
        mTvMonth = v.findViewById(R.id.tv_month);
        mGridView = v.findViewById(R.id.gridview_days);


        //mGridView.setData(temp);
        //mGridView.setRefresh();

        /*for(int i=0; i<31; i++ ) {
            TextView v1 = new TextView(getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            v1.setLayoutParams(lp);
            v1.setText(String.valueOf(++i));


            GridLayout.LayoutParams lp2 = (GridLayout.LayoutParams) mGridView.getLayoutParams();
            lp2.setGravity(Gravity.FILL);
            mGridView.setLayoutParams(lp2);
            mGridView.addView(v1);

        }*/
    }


}
