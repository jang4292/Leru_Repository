package com.bpm202.SensorProject.Main.Schedules;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.Util.UtilForApp;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

import java.util.List;

public class SchedulesViewPagerFragment extends SchdulesBaseFragment {

    private RecyclerView recyclerView;

    public static SchedulesViewPagerFragment Instance() {
        return new SchedulesViewPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedules_view_pager, container, false);
        initView(v);
        return v;
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_schedules_view_pager;
    }

    @NonNull
    @Override
    protected void initView(View v) {
        recyclerView = v.findViewById(R.id.recycler_view_exercise_list);
        ExerciseSchedulesAdapter adpater = new ExerciseSchedulesAdapter(getContext(), list);
        recyclerView.setAdapter(adpater);
        UtilForApp.setDividerItemDecoration(getContext(), recyclerView, R.drawable.divider_shape);
    }

    private List<ScheduleValueObject> list;

    public void setData(List<ScheduleValueObject> list) {
        this.list = list;
    }

    @Override
    public void UpdateState(SchdulesManager.STATE cur, SchdulesManager.STATE pre) {
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private class ExerciseSchedulesAdapter extends RecyclerView.Adapter<ExerciseSchedulesAdapter.ScheduleViewHolder> {

        private final Context context;
        private List<ScheduleValueObject> list;

        public ExerciseSchedulesAdapter(Context context, List<ScheduleValueObject> list) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public ExerciseSchedulesAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_schedule, viewGroup, false);
            return new ExerciseSchedulesAdapter.ScheduleViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseSchedulesAdapter.ScheduleViewHolder scheduleViewHolder, int position) {
            if (SchdulesManager.Instance().STATE_CLASS.getCurrentState().equals(SchdulesManager.STATE.MODIFY)) {
                scheduleViewHolder.imMove.setVisibility(View.VISIBLE);
                scheduleViewHolder.ibtnDelete.setVisibility(View.GONE);

                scheduleViewHolder.imMove.setOnTouchListener((v, event) -> {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    }
                    return false;
                });

            } else if (SchdulesManager.Instance().STATE_CLASS.getCurrentState().equals(SchdulesManager.STATE.DELETE)) {
                scheduleViewHolder.imMove.setVisibility(View.GONE);
                scheduleViewHolder.ibtnDelete.setVisibility(View.VISIBLE);

                scheduleViewHolder.ibtnDelete.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(MappingUtil.name(context, list.get(position).getType().getName()));
                    builder.setMessage("삭제하시겠습니까?");


                    builder.setPositiveButton("예",
                            (dialog, which) -> {
                                Util.LoadingProgress.show(getContext());
                                ScheduleRepository.getInstance().deleteSchedule(list.get(position), new ScheduleDataSource.CompleteCallback() {
                                    @Override
                                    public void onComplete() {
                                        SchdulesManager.Instance().setSTATE(SchdulesManager.STATE.RELOAD);
                                        Util.LoadingProgress.hide();
                                    }

                                    @Override
                                    public void onDataNotAvailable() {
                                        Util.LoadingProgress.hide();
                                        Log.e(MainActivity.TAG, "[SchedulesViewPagerFragment] deleteSchedule onDataNotAvailable");
                                    }
                                });
                            });
                    builder.setNegativeButton("아니오",
                            (dialog, which) -> QToast.showToast(context, "삭제를 취소했습니다."));
                    builder.show();
                });
            } else {
                scheduleViewHolder.onBinding(list.get(position));
                scheduleViewHolder.imMove.setVisibility(View.GONE);
                scheduleViewHolder.ibtnDelete.setVisibility(View.GONE);

                scheduleViewHolder.itemView.setTag(position);
                scheduleViewHolder.itemView.setOnClickListener(v -> {
                    QToast.showToast(context, "TEST, onItem position: " + v.getTag());
                });
            }
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
            private ImageButton ibtnDelete;
            private ImageView imMove;


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
                ibtnDelete = itemView.findViewById(R.id.ibtn_delete);
                imMove = itemView.findViewById(R.id.iv_move);
            }

            private void onBinding(ScheduleValueObject scheduleVo) {
                ivExerciseIcon.setImageDrawable(context.getResources().getDrawable(getIconResource(scheduleVo.getType())));
                tvExerciseName.setText(MappingUtil.name(context, scheduleVo.getType().getName()));

                if (scheduleVo.getType().isTime()) {
                    tvWeightLabel.setText(R.string.schedules_rpm);
                    tvCountLabel.setText(R.string.schedules_times);
                } else {
                    tvWeightLabel.setText(R.string.schedules_kg);
                    tvCountLabel.setText(R.string.schedules_count);
                }
                tvRestLabel.setText(R.string.schedules_rest);
                tvSetLabel.setText(R.string.schedules_set);

                tvCountNum.setText(String.format("%02d", scheduleVo.getCount()));
                tvSetNum.setText(String.format("%02d", scheduleVo.getSetCnt()));
                tvRestNum.setText(String.format("%02d", scheduleVo.getRest()));
                tvWeightNum.setText(String.format("%02d", scheduleVo.getWeight()));
            }

            private int getIconResource(TypeValueObject exerciseType) {
                return MappingUtil.exerciseIconResource[exerciseType.getId() - 1];
            }
        }
    }
}
