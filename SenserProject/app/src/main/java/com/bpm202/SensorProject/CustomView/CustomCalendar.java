package com.bpm202.SensorProject.CustomView;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.Data.ExDataSrouce;
import com.bpm202.SensorProject.Data.ExRepository;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CustomCalendar extends LinearLayout {

    private ImageButton ibtn_prev;
    private ImageButton ibtn_next;
    private TextView tv_month;
    private GridView gridview;
    private int mMonth;
    private int mYear;
    private GregorianCalendar calendar;

    public CustomCalendar(Context context) {
        super(context);
        initView();
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout_calendar, this, false);
        addView(v);

        ibtn_prev = findViewById(R.id.ibtn_prev);
        ibtn_next = findViewById(R.id.ibtn_next);
        tv_month = findViewById(R.id.tv_month);
        gridview = findViewById(R.id.gridview);
        mYear = Calendar.getInstance().get(Calendar.YEAR);
        mMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

        final Adapter adapter = new Adapter(getContext());
        gridview.setAdapter(adapter);
        calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1, 0, 0, 0);

        setTextData(tv_month, mYear, mMonth);
        onLoaded(adapter);

        ibtn_prev.setOnClickListener(v1 -> {
            if (mMonth > 1) {
                mMonth--;
            } else {
                mMonth = 12;
                mYear--;
            }
            setTextData(tv_month, mYear, mMonth);
            onLoaded(adapter);
        });

        ibtn_next.setOnClickListener(v12 -> {
            if (mMonth < 12) {
                mMonth++;
            } else {
                mMonth = 1;
                mYear++;
            }
            setTextData(tv_month, mYear, mMonth);
            onLoaded(adapter);
        });
    }

    private void setTextData(TextView tv, int yearData, int monthData) {
        StringBuffer sb = new StringBuffer();
        sb.append(yearData);
        sb.append(getContext().getString(R.string.year));
        sb.append(monthData);
        sb.append(getContext().getString(R.string.month));

        tv.setText(sb.toString());
    }

    private List<String> updateCalendar() {
        List<String> calendarList = new ArrayList<>();
        calendar.set(mYear, mMonth - 1, 1);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

        for (int j = 0; j < Calendar.DAY_OF_WEEK; j++) {
            calendarList.add(CommonData.WEEK_DATA_LIST[j]);  //비어있는 일자 타입
        }
        for (int j = 1; j < dayOfWeek; j++) {
            calendarList.add(String.valueOf(0));  //비어있는 일자 타입
        }
        for (int j = 1; j <= max; j++) {
            calendarList.add(String.valueOf(j));
        }
        return calendarList;
    }

    public interface OnTotalDaysTextViewListener {
        TextView getOnTotalDaysText();
    }

    private OnTotalDaysTextViewListener onTotalDaysTextViewListener;

    public void setOnTotalDaysTextViewListener(OnTotalDaysTextViewListener onTotalDaysTextViewListener) {
        this.onTotalDaysTextViewListener = onTotalDaysTextViewListener;
    }

    // 서버에서 데이터 다운로드
    private void onLoaded(final Adapter adapter) {
        Util.FadeAnimation.fadeOut(gridview);
        ExRepository.getInstance().getExerciseMonth(String.valueOf(mYear), String.valueOf(mMonth),
                new ExDataSrouce.GetCallback() {
                    @Override
                    public void onLoaded(List<Integer> i_list) {
                        adapter.notifyDataSetChanged(updateCalendar(), i_list);
                        if (onTotalDaysTextViewListener != null) {
                            TextView tv = onTotalDaysTextViewListener.getOnTotalDaysText();
                            if (tv != null) {
                                String str = i_list.size() + "DAYS";
                                tv.setText(str);
                            }
                        }
                        Util.FadeAnimation.fadeIn(gridview);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        Util.FadeAnimation.fadeIn(gridview);
                    }
                });
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private class Adapter extends BaseAdapter {

        private final Context context;
        private List<String> list;
        private List<Integer> dataList;

        private Adapter(Context context) {
            this.context = context;
        }

        public void notifyDataSetChanged(List<String> list, List<Integer> dataList) {
            this.list = list;
            this.dataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
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
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false);
                holder.tvDay = convertView.findViewById(R.id.tv_day);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String str = list.get(position);
            holder.tvDay.setClickable(false);
            if (str.equals("0")) {
                holder.tvDay.setVisibility(INVISIBLE);
            } else {
                holder.tvDay.setVisibility(VISIBLE);
                holder.tvDay.setClickable(false);
                holder.tvDay.setText(str);
                holder.tvDay.setTextColor(Color.BLACK);

                for (Integer i : dataList) {
                    if (str.equals(String.valueOf(i))) {
                        holder.tvDay.setTextColor(Color.MAGENTA);
                        if (onClickListener != null) {
                            holder.tvDay.setOnClickListener(onClickListener);
                        }
                    }
                }
            }
            return convertView;
        }

        class ViewHolder {
            TextView tvDay;
        }
    }


}
