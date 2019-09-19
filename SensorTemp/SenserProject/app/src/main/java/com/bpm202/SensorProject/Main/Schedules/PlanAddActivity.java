package com.bpm202.SensorProject.Main.Schedules;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm202.SensorProject.Data.DayOfWeek;
import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.Temp.MainActivity_sub;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

import java.util.ArrayList;
import java.util.List;

public class PlanAddActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    protected void init() {
        setContentView(R.layout.activity_plan_add_recyclerview);
        initView();
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        getSupportActionBar().setTitle(R.string.title_plan_exericse_add);

        recyclerView = findViewById(R.id.recycler_view);

        TypeValueObject[] types = TypeValueObject.values();
        List<TypeValueObject> ListType = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            if (!types[i].isEquipments()) {
                ListType.add(types[i]);
            } else {
                Log.e(MainActivity_sub.TAG, "it's not types in classes");
            }
        }

        for (int i = 0; i < types.length; i++) {
            if (types[i].isEquipments()) {
                ListType.add(types[i]);
            } else {
                Log.e(MainActivity_sub.TAG, "it's not types in classes");
            }
        }


        ExerciseAddAdapter adpater = new ExerciseAddAdapter(this, ListType);
        recyclerView.setAdapter(adpater);
    }

    private class ExerciseAddAdapter extends RecyclerView.Adapter<ExerciseAddAdapter.ViewHolder> {
        private Context context;
        private List<TypeValueObject> exerciseTypes;

        public ExerciseAddAdapter(Context context, List<TypeValueObject> types) {
            this.context = context;
            this.exerciseTypes = types;
        }

        @NonNull
        @Override
        public ExerciseAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_plan_add_list, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseAddAdapter.ViewHolder viewHolder, int position) {
            final TypeValueObject exerciseType = exerciseTypes.get(position);
            viewHolder.tv_exercise.setText(MappingUtil.name(context, exerciseType.getName()));
            viewHolder.itemView.setOnClickListener(v -> {
                postAddSchedule(newSchedules(exerciseType));
            });
            viewHolder.ibtn_exercise.setImageResource(getIconResource(exerciseType));
        }

        private int getIconResource(TypeValueObject exerciseType) {
            return MappingUtil.exerciseIconResource[exerciseType.getId() - 1];
        }

        @Override
        public int getItemCount() {
            return exerciseTypes == null ? 0 : exerciseTypes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView ibtn_exercise;
            private TextView tv_exercise;

            public ViewHolder(View itemView) {
                super(itemView);
                ibtn_exercise = itemView.findViewById(R.id.ibtn_exercise);
                tv_exercise = itemView.findViewById(R.id.tv_exercise);
            }
        }

        public void postAddSchedule(final ScheduleValueObject scheduleValueObject) {
            ScheduleRepository repository = ScheduleRepository.getInstance();
            repository.addSchedule(scheduleValueObject, new ScheduleDataSource.CompleteCallback() {
                @Override
                public void onComplete() {
//                    startActivity(new Intent(PlanAddActivity.this, WeeksPlanActivity.class));
                    SchdulesManager.Instance().setCurrentPageOfDay(scheduleValueObject.getDay().getCode());
//                    WeeksPlanActivity.isReload = true;
                    setResult(RESULT_OK);


                    finish();
                }

                @Override
                public void onDataNotAvailable() {
                }
            });
        }

        private ScheduleValueObject newSchedules(TypeValueObject type) {
            final int INIT_COUNT = 1;
            final int INIT_SET = 1;
            final int INIT_REST = 5;
            ScheduleValueObject.Builder sb = new ScheduleValueObject.Builder();
            sb.setType(type);

            sb.setDay(DayOfWeek.findByCode(SchdulesManager.Instance().getCurrentPageOfDay() + 1));
            Log.d("Test", "sb.getDay() : " + sb.getDay());
            sb.setCount(INIT_COUNT);
            sb.setSetCnt(INIT_SET);
            sb.setRest(INIT_REST);
            //sb.setPos(pos);
            //Log.d(MainActivity_sub.TAG, "TEST, Add Position : " + SchdulesManager.Instance().getCurrentPageOfDay());
            //sb.setPos(SchdulesManager.Instance().getCurrentPageOfDay());
            return sb.create();
        }
    }
}
