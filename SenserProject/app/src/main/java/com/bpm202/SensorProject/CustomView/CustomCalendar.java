package com.bpm202.SensorProject.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bpm202.SensorProject.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class CustomCalendar extends GridView {

    private Adapter adapter;
    private final Date date = new Date(System.currentTimeMillis());

    public CustomCalendar(Context context) {
        super(context);
    }

    public CustomCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomCalendar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setData(List<String> list) {

        if (adapter == null) {
            adapter = new Adapter(getContext());
            setAdapter(adapter);
        }

        today();
        adapter.notifyDataSetChanged(list);


    }

    public void setRefresh() {

        if (adapter == null) {
            adapter = new Adapter(getContext());
            setAdapter(adapter);
        }


        adapter.notifyDataSetChanged(today());
    }

    private List<String> today() {
        List<String> calendarList = new ArrayList<>();
        GregorianCalendar calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1, 0, 0, 0);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

        for (int j = 0; j < dayOfWeek; j++) {
            calendarList.add(String.valueOf(0));  //비어있는 일자 타입
        }
        for (int j = 1; j <= max; j++) {
            calendarList.add(String.valueOf(j));
        }

        return calendarList;
    }


    private class Adapter extends BaseAdapter {

        private final Context context;
        private List<String> list;

        private Adapter(Context context) {
            this.context = context;
        }


        public void notifyDataSetChanged(List<String> list) {
            this.list = list;
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
            if (str.equals("0")) {
                holder.tvDay.setVisibility(INVISIBLE);
            } else {
                holder.tvDay.setVisibility(VISIBLE);
                holder.tvDay.setText(list.get(position));
            }

            /*holder.tvDay.setOnClickListener(v -> {
                Util.FragmentUtil.addFragmentToActivity(getFragmentManager(), HistoryDateFragment.newInstance(), R.id.child_fragment_container);
            });*/

            return convertView;
        }

        class ViewHolder {
            TextView tvDay;
        }
    }


}
