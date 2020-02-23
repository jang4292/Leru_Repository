package com.bpm202.SensorProject.Main.History;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm202.SensorProject.App;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.Data.ExDataSrouce;
import com.bpm202.SensorProject.Data.ExRepository;
import com.bpm202.SensorProject.Data.ExVo;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

import java.util.List;

public class HistoryDayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String date;
    private HistoryDayAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getIntent().getStringExtra(AppPreferences.KEY_DATE_INFO);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_history_day);
        initView();
        onLoaded();
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        getSupportActionBar().setTitle(R.string.title_plan_exericse_add);

        recyclerView = findViewById(R.id.recycler_view);

        adapter = new HistoryDayAdapter(this/*, ListType*/);
        recyclerView.setAdapter(adapter);
    }

    private void onLoaded() {
        ExRepository.getInstance().getExerciseDay(App.getToken(), date,
                new ExDataSrouce.GetDayCallback() {
                    @Override
                    public void onLoaded(List<ExVo> exVoList) {
                        if (adapter != null) {
                            adapter.setDataNotification(exVoList);
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {
                        Log.d("TEST", "onDataNotAvailable");
                    }
                });
    }

    private class HistoryDayAdapter extends RecyclerView.Adapter<HistoryDayAdapter.ScheduleViewHolder> {

        private final Context context;
        private List<ExVo> list;

        public HistoryDayAdapter(Context context) {
            this.context = context;
        }

        public void setDataNotification(List<ExVo> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public HistoryDayAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_plan_list, viewGroup, false);
            return new HistoryDayAdapter.ScheduleViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryDayAdapter.ScheduleViewHolder scheduleViewHolder, int position) {
            scheduleViewHolder.onBinding(list.get(position));
            scheduleViewHolder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }

        class ScheduleViewHolder extends RecyclerView.ViewHolder {

            private ImageView ivExerciseIcon;
            private TextView tvExerciseName;
            private TextView tvWeightNum;
            private TextView tvRestNum;
            private TextView tvSetNum;
            private TextView tvCountNum;
            private TextView tvWeightLabel;
            private TextView tvRestLabel;
            private TextView tvSetLabel;
            private TextView tvCountLabel;


            public ScheduleViewHolder(@NonNull View itemView) {
                super(itemView);

                ivExerciseIcon = itemView.findViewById(R.id.iv_exercise_icon);
                tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
                tvWeightNum = itemView.findViewById(R.id.tv_weight_num);
                tvRestNum = itemView.findViewById(R.id.tv_rest_num);
                tvSetNum = itemView.findViewById(R.id.tv_set_num);
                tvCountNum = itemView.findViewById(R.id.tv_count_num);
                tvWeightLabel = itemView.findViewById(R.id.tv_weight_label);
                tvRestLabel = itemView.findViewById(R.id.tv_rest_label);
                tvSetLabel = itemView.findViewById(R.id.tv_set_label);
                tvCountLabel = itemView.findViewById(R.id.tv_count_label);
            }

            private void onBinding(ExVo value) {
                ivExerciseIcon.setImageDrawable(context.getResources().getDrawable(getIconResource(value.getType())));
                tvExerciseName.setText(MappingUtil.name(context, value.getType().getName()));

                if (value.getType().isTime()) {
                    tvWeightLabel.setText(R.string.schedules_rpm);
                    tvCountLabel.setText(R.string.schedules_times);
                } else {
                    tvWeightLabel.setText(R.string.schedules_kg);
                    tvCountLabel.setText(R.string.schedules_count);
                }
                tvRestLabel.setText(R.string.schedules_rest);
                tvSetLabel.setText(R.string.schedules_set);

                tvCountNum.setText(String.format("%02d", value.getCount()));
                tvSetNum.setText(String.format("%02d", value.getSetCnt()));
                tvRestNum.setText(String.format("%02d", value.getRest()));
                tvWeightNum.setText(String.format("%02d", value.getWeight()));
            }

            private int getIconResource(TypeValueObject exerciseType) {
                return MappingUtil.exerciseIconResource[exerciseType.getId() - 1];
            }
        }
    }
}















