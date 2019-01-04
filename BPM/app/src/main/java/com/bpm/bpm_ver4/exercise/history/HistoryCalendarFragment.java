package com.bpm.bpm_ver4.exercise.history;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.BaseFragment;
import com.bpm.bpm_ver4.data.source.ExDataSrouce;
import com.bpm.bpm_ver4.data.source.ExRepository;
import com.bpm.bpm_ver4.util.ActivityUtils;
import com.bpm.bpm_ver4.util.anim.AnimationUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HistoryCalendarFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = HistoryCalendarFragment.class.getSimpleName();

    private GridView mGridView;
    private TextView mTvYear, mTvMonth;
    private int mMonth;
    private int mYear;
    private List<Integer> exObjList;
    private Calendar mCalendar;

    private static final int FIRST_DAY = 1;
    private final Date date = new Date(System.currentTimeMillis());

    public static HistoryCalendarFragment newInstance() {
        HistoryCalendarFragment fragment = new HistoryCalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_calendar, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // view setting
        View view = getView();
        mTvYear = view.findViewById(R.id.tv_year);
        mTvMonth = view.findViewById(R.id.tv_month);
        mGridView = view.findViewById(R.id.gridview_days);

        // Listener setting
        view.findViewById(R.id.ibtn_prev).setOnClickListener(this);
        view.findViewById(R.id.ibtn_next).setOnClickListener(this);

        // calendar setting
        mCalendar = today();
        yearAndMonth(mYear, mMonth);
        onLoaded();
    }


    // 현재 달의 캘린더 생성
    private Calendar today() {
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        Calendar calendar = (Calendar) App.getCalendar().clone();

        calendar.set(
                Integer.parseInt(curYearFormat.format(date)),
                Integer.parseInt(curMonthFormat.format(date)) - 1,
                FIRST_DAY);

        mYear =  Integer.parseInt(curYearFormat.format(date));
        mMonth = Integer.parseInt(curMonthFormat.format(date));

        return calendar;
    }


    // 서버에서 데이터 다운로드
    private void onLoaded() {
        AnimationUtil.fadeOut(mGridView);
        ExRepository repository = ExRepository.getInstance();
        repository.getExerciseMonth(String.valueOf(mYear), String.valueOf(mMonth),
                new ExDataSrouce.GetCallback() {
                    @Override
                    public void onLoaded(List<Integer> i_list) {
                        exObjList = i_list;
                        GridAdapter adapter = new GridAdapter(getContext(), updateDate(exObjList));
                        mGridView.setAdapter(adapter);
                        AnimationUtil.fadeIn(mGridView);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        AnimationUtil.fadeIn(mGridView);
                    }
                });
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    // 달력 내용 데이터 갱신
    private List<String> updateDate(List<Integer> exObjList) {
        String[] strs = Objects.requireNonNull(getContext()).getResources().getStringArray(R.array.day_of_week_list);
        List<String> list = new ArrayList<>(Arrays.asList(strs));

        mCalendar.set(mYear, mMonth -1, FIRST_DAY);
        int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 1;  i < dayNum;  i++) {
            list.add("");
        }

        List<String> days = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            days.add("X");
        }

        for (int i = 0;  i < exObjList.size();  i++) {
            Integer integer = exObjList.get(i);
            days.set(integer -1, "O");
        }
        list.addAll(days);

        return list;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibtn_prev :
                if (mMonth > 1) {
                    mMonth--;
                } else {
                    mMonth = 12;
                    mYear--;
                }
                break;

            case R.id.ibtn_next :
                if (mMonth < 12) {
                    mMonth++;
                } else {
                    mMonth = 1;
                    mYear++;
                }
                break;
        }

        yearAndMonth(mYear, mMonth);
        onLoaded();
    }


    // 년,월 텍스트 수정
    private void yearAndMonth(int i_year, int i_month) {
        String year =  i_year + getString(R.string.year);
        String month = i_month + getString(R.string.month);
        mTvYear.setText(year);
        mTvMonth.setText(month);
    }


    // 달력 데이터가 들어가는 그리드 어뎁터
    private class GridAdapter extends BaseAdapter {
        private List<String> list;
        private LayoutInflater inflater;
        private Context mContext;

        public GridAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar, parent, false);

                holder = new ViewHolder();
                holder.tvDay = convertView.findViewById(R.id.tv_day);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvDay.setText(list.get(position));
            holder.tvDay.setOnClickListener(v -> {
                ActivityUtils.addFragmentToActivity(
                        getFragmentManager(),
                        HistoryDateFragment.newInstance(),
                        R.id.fragment_container);
            });

            return convertView;
        }


        class ViewHolder {
            TextView tvDay;
        }
    }

}
