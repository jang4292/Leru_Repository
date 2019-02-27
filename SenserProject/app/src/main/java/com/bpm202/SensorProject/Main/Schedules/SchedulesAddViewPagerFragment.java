package com.bpm202.SensorProject.Main.Schedules;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.Data.DayOfWeek;
import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.Util.UtilForApp;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

import java.util.ArrayList;
import java.util.List;

public class SchedulesAddViewPagerFragment extends BaseFragment {

    private static SchedulesAddViewPagerFragment instance;

    public static SchedulesAddViewPagerFragment Instance() {
        return instance == null ? new SchedulesAddViewPagerFragment() : instance;
    }

    public void setTabPositionFromParent(int i) {
        tabPosition = i;
    }

    private int tabPosition;
    private RecyclerView recyclerView;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_schedules_add_pager;
    }

    @NonNull
    @Override
    protected void initView(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);

        TypeValueObject[] types = TypeValueObject.values();
        List<TypeValueObject> ListType = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            if (tabPosition == 0 && !types[i].isEquipments()) {
                ListType.add(types[i]);
            } else if (tabPosition == 1 && types[i].isEquipments()) {
                ListType.add(types[i]);
            } else {
                Log.e(MainActivity.TAG, "it's not types in classes");
            }
        }
        ExerciseAddAdapter adpater = new ExerciseAddAdapter(getContext(), ListType);
        recyclerView.setAdapter(adpater);
        UtilForApp.setDividerItemDecoration(getContext(), recyclerView, R.drawable.divider_shape);
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
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_exercise_select, viewGroup, false);
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
            return MappingUtil.exerciseIconResource[exerciseType.getId()-1];
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

        public void postAddSchedule(ScheduleValueObject scheduleValueObject) {
            ScheduleRepository repository = ScheduleRepository.getInstance();
            repository.addSchedule(scheduleValueObject, new ScheduleDataSource.CompleteCallback() {
                @Override
                public void onComplete() {
                    SchdulesManager.Instance().setSTATE(SchdulesManager.STATE.RELOAD);
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
            sb.setCount(INIT_COUNT);
            sb.setSetCnt(INIT_SET);
            sb.setRest(INIT_REST);
            //sb.setPos(pos);
            //Log.d(MainActivity.TAG, "TEST, Add Position : " + SchdulesManager.Instance().getCurrentPageOfDay());
            //sb.setPos(SchdulesManager.Instance().getCurrentPageOfDay());
            return sb.create();
        }
    }
}
