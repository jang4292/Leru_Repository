package com.bpm202.SensorProject.Main.Schedules;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bpm202.SensorProject.Data.DayOfWeek;
import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

import java.util.ArrayList;
import java.util.List;

import static com.bpm202.SensorProject.Main.Schedules.MainPlanFragment.CURRENT_DAY_OF_WEEK;

public class PlanAddActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LinearLayout ll_input_add_plan_data;
    private LinearLayout ll_main;
    private TextView tv_exercise_title;
    private EditText et_count_input;
    private EditText et_kg_input;
    private EditText et_rest_input;
    private EditText et_set_input;
    private Button btn_exercise_plan_add;
    private TextView tv_count_input;

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

        ll_input_add_plan_data = findViewById(R.id.ll_input_add_plan_data);
        ll_input_add_plan_data.setVisibility(View.GONE);

        ll_main = findViewById(R.id.ll_main);
        ll_main.setVisibility(View.VISIBLE);

        tv_exercise_title = findViewById(R.id.tv_exercise_title);

        tv_count_input = findViewById(R.id.tv_count_input);
        et_count_input = findViewById(R.id.et_count_input);
        et_kg_input = findViewById(R.id.et_kg_input);
        et_rest_input = findViewById(R.id.et_rest_input);
        et_set_input = findViewById(R.id.et_set_input);

        btn_exercise_plan_add = findViewById(R.id.btn_exercise_plan_add);

        TypeValueObject[] types = TypeValueObject.values();
        List<TypeValueObject> ListType = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            if (!types[i].isEquipments()) {
                ListType.add(types[i]);
            } else {
                Log.e(MainActivity.TAG, "it's not types in classes");
            }
        }

        for (int i = 0; i < types.length; i++) {
            if (types[i].isEquipments()) {
                ListType.add(types[i]);
            } else {
                Log.e(MainActivity.TAG, "it's not types in classes");
            }
        }


        ExerciseAddAdapter adpater = new ExerciseAddAdapter(this, ListType);
        recyclerView.setAdapter(adpater);
    }

    @Override
    public void onBackPressed() {
        if (ll_input_add_plan_data.getVisibility() == View.VISIBLE) {
            ll_input_add_plan_data.setVisibility(View.GONE);
            ll_main.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();

        }


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

                ll_main.setVisibility(View.GONE);
                ll_input_add_plan_data.setVisibility(View.VISIBLE);
                tv_exercise_title.setText(exerciseType.getName());

                if (MappingUtil.isCountType(PlanAddActivity.this, exerciseType.getName())) {
                    tv_count_input.setText(R.string.schedules_count);
                } else if (MappingUtil.isWaitingExercise(exerciseType)) {
                    tv_count_input.setText(R.string.schedules_duration);
                } else {
                    tv_count_input.setText(R.string.schedules_count);
                }

                et_count_input.setHint(String.valueOf(20));
                et_kg_input.setHint(String.valueOf(15));
                et_rest_input.setHint(String.valueOf(5));
                et_set_input.setHint(String.valueOf(2));

                btn_exercise_plan_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String count = et_count_input.getText().toString().trim();
                        String kg = et_kg_input.getText().toString().trim();
                        String rest = et_rest_input.getText().toString().trim();
                        String set = et_set_input.getText().toString().trim();


                        postAddSchedule(newSchedules(exerciseType, count, kg, rest, set));
                    }
                });

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
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onDataNotAvailable() {
                    Log.d("Test", "onDataNotAvailable on PostAddSchedule");
                }
            });
        }

        private ScheduleValueObject newSchedules(TypeValueObject type, String count, String kg, String rest, String set) {
            ScheduleValueObject.Builder sb = new ScheduleValueObject.Builder();
            sb.setType(type);

            sb.setDay(DayOfWeek.findByCode(CURRENT_DAY_OF_WEEK + 1));
            sb.setWeight(Integer.parseInt(kg.isEmpty() ? "15" : kg));
            sb.setCount(Integer.parseInt(count.isEmpty() ? "20" : count));
            sb.setSetCnt(Integer.parseInt(set.isEmpty() ? "2" : set));
            sb.setRest(Integer.parseInt(rest.isEmpty() ? "5" : rest));
            sb.setPos(1000);
            return sb.create();
        }
    }
}
