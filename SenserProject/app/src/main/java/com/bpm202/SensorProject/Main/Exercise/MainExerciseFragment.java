package com.bpm202.SensorProject.Main.Exercise;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.Data.DayOfWeek;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.Main.Temp.MainDataManager;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainExerciseFragment extends BaseFragment {

    private static MainExerciseFragment instance = null;
    private LinearLayout ll_work_rest;
    private LinearLayout ll_work_do;
    private RecyclerView recycler_view_exercise_list;

    private Button btn_exercise_title;
    private ImageView iv_exercise_item;


    public static MainExerciseFragment newInstance() {
        if (instance == null) {
            instance = new MainExerciseFragment();
        }
        return instance;
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exercise_main;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 3001) {
                ((MainActivity) getActivity()).onClickingBtnPlanButton();
            } else if (requestCode == 3002) {
                Log.d("Test", "request Code 3002");
            }
        }


    }

    @NonNull
    @Override
    protected void initView(View v) {
        ((MainActivity) getActivity()).setTitleText(R.string.title_exercise);

        ll_work_rest = v.findViewById(R.id.ll_work_rest);
        ll_work_do = v.findViewById(R.id.ll_work_do);

        int dayCode = Util.CalendarInfo.getCalendar().get(Calendar.DAY_OF_WEEK);
        DayOfWeek dayOfWeek = DayOfWeek.findByCode(dayCode);
        @NonNull List<ScheduleValueObject> todaySchedules = MainDataManager.Instance().getScheduleValueObjectForDay(dayOfWeek);


        if (todaySchedules == null) {
            Log.d("Test", "todaySchedules is null");
            return;
        }
        if (todaySchedules.size() == 0) {
            setNoExerciseLayout(true);

            if (MainDataManager.Instance().getListScheduleValueObject().size() == 0) {
                startActivityForResult(new Intent(getActivity(), NoSettingExerciseActivity.class), 3001);
            }
        } else {
            Log.d("Test", "todaySchedules : " + todaySchedules);
            setNoExerciseLayout(false);

            recycler_view_exercise_list = v.findViewById(R.id.recycler_view_exercise_list);
            ExerciseSchedulesAdapter adpater = new ExerciseSchedulesAdapter(getContext(), todaySchedules);
            recycler_view_exercise_list.setAdapter(adpater);


            iv_exercise_item = v.findViewById(R.id.iv_exercise_item);
            btn_exercise_title = v.findViewById(R.id.btn_exercise_title);

            setMainItem(todaySchedules.get(0));

            btn_exercise_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScheduleValueObject scheduleVo = (ScheduleValueObject) v.getTag();

                    Intent intent = new Intent(getActivity(), MainExerciseActivity.class);
                    intent.putExtra("ScheduleValueObject", scheduleVo);
                    startActivityForResult(intent, 3002);

                }
            });
        }
    }

    private void setMainItem(ScheduleValueObject scheduleVo) {
        iv_exercise_item.setImageDrawable(getResources().getDrawable(getIconResourceBig(scheduleVo.getType())));
        btn_exercise_title.setText(MappingUtil.name(getActivity(), scheduleVo.getType().getName()));
        btn_exercise_title.setTag(scheduleVo);
    }

    private int getIconResourceBig(TypeValueObject exerciseType) {
        return MappingUtil.exerciseIconResourceBig[exerciseType.getId() - 1];
    }

    private void setNoExerciseLayout(boolean isNoData) {
        if (isNoData) {
            ll_work_do.setVisibility(View.GONE);
            ll_work_rest.setVisibility(View.VISIBLE);
        } else {
            ll_work_do.setVisibility(View.VISIBLE);
            ll_work_rest.setVisibility(View.GONE);
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
        public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_work_list, viewGroup, false);
            return new ScheduleViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleViewHolder scheduleViewHolder, int position) {
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

            public ScheduleViewHolder(@NonNull View itemView) {
                super(itemView);

                ivExerciseIcon = itemView.findViewById(R.id.iv_exercise_icon);
                tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
            }

            private void onBinding(ScheduleValueObject scheduleVo) {
                ivExerciseIcon.setImageDrawable(context.getResources().getDrawable(getIconResource(scheduleVo.getType())));
                tvExerciseName.setText(MappingUtil.name(context, scheduleVo.getType().getName()));

                itemView.setOnClickListener(v -> {
                    setMainItem(scheduleVo);
                });
            }

            private int getIconResource(TypeValueObject exerciseType) {
                return MappingUtil.exerciseIconResource[exerciseType.getId() - 1];
            }
        }
    }

}
