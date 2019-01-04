package com.bpm.bpm_ver4.exercise.schedule.add;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm.bpm_ver4.BaseFragment;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.data.source.ScheduleDataSource;
import com.bpm.bpm_ver4.data.source.ScheduleRepository;
import com.bpm.bpm_ver4.util.MappingUtil;
import com.bpm.bpm_ver4.vo.DayOfWeek;
import com.bpm.bpm_ver4.vo.Type;

import java.util.List;
import java.util.Objects;

public class ScheuldesAddViewPager extends BaseFragment {

    private DayOfWeek dayOfWeek;
    private int pos;
    private RecyclerView recyclerView;
    private ExerciseAddAdapter addAdapter;
    private List<Type> types;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedules_add_pager, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recycler_view);

        addAdapter = new ExerciseAddAdapter(getContext(), types);
        recyclerView.setAdapter(addAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        recyclerView.addItemDecoration(decoration);
    }

    public void initData(DayOfWeek dayOfWeek, int pos, List<Type> types) {
        this.dayOfWeek = dayOfWeek;
        this.pos = pos;
        this.types = types;
    }


    public void postAddSchedule(ScheduleVo scheduleVo) {
        ScheduleRepository repository = ScheduleRepository.getInstance();
        repository.addSchedule(scheduleVo, new ScheduleDataSource.CompleteCallback() {
                    @Override
                    public void onComplete() {
                        Objects.requireNonNull(getActivity()).finish();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }


    public ScheduleVo newSchedules(Type type) {
        final int INIT_COUNT = 1;
        final int INIT_SET = 1;
        final int INIT_REST = 5;
        ScheduleVo.Builder sb = new ScheduleVo.Builder();
        sb.setType(type);
        sb.setDay(dayOfWeek);
        sb.setCount(INIT_COUNT);
        sb.setSetCnt(INIT_SET);
        sb.setRest(INIT_REST);
        sb.setPos(pos);
        return sb.create();
    }


    private class ExerciseAddAdapter extends RecyclerView.Adapter<ExerciseAddAdapter.ViewHolder> {

        private Context mContext;
        private List<Type> exerciseTypes;

        public ExerciseAddAdapter(Context context, List<Type> types) {
            this.mContext = context;
            this.exerciseTypes = types;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_exercise_select, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Type exerciseType = getItem(position);
            holder.tv_exercise.setText(MappingUtil.name(mContext, exerciseType.getName()));
            holder.itemView.setOnClickListener(v -> {
                postAddSchedule(newSchedules(exerciseType));
            });
        }

        @Override
        public int getItemCount() {
            return exerciseTypes.size();
        }

        public Type getItem(int position) {
            return exerciseTypes.get(position);
        }

        public void changeExercise(List<Type> types) {
            exerciseTypes = types;
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
    }
}
