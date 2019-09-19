package com.bpm202.SensorProject.Main;

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
import android.widget.Toast;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.Data.DayOfWeek;
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
//        DayOfWeek dayOfWeek = DayOfWeek.findByCode(dayCode);
        DayOfWeek dayOfWeek = DayOfWeek.findByCode(3);
        Log.d("Test", "dayOfWeek : " + dayOfWeek);
        @NonNull List<ScheduleValueObject> todaySchedules = MainDataManager.Instance().getScheduleValueObjectForDay(dayOfWeek);


        if (todaySchedules == null) {
            Log.d("Test", "todaySchedules is null");
            return;
        }
        if (todaySchedules.size() == 0) {
            setNoExerciseLayout(true);

            if (MainDataManager.Instance().getListScheduleValueObject().size() == 0) {


                startActivityForResult(new Intent(getActivity(), NoSettingExerciseActivity.class), 3001);

//                tv_text.setText("나에게 맞는 일정을 잡고 시작하세요");
//                button.setVisibility(View.VISIBLE);
//                button.setOnClickListener(v1 -> {
//                    getActivity().setTitle(R.string.menu_schedules);
//                    Util.FragmentUtil.replaceFragment(getFragmentManager(), SchedulesFrgment.Instance(), R.id.fragment_container);
//                });
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
                    Toast.makeText(getActivity(), "scheduleVo.getType().getName() : " + scheduleVo.getType().getName(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), MainExerciseActivity.class);

                    startActivityForResult(new Intent(getActivity(), MainExerciseActivity.class), 3002);
                }
            });
//            iv_exercise_item.setImageDrawable(getResources().getDrawable(getIconResourceBig(todaySchedules.get(0).getType())));
//            btn_exercise_title.setText(MappingUtil.name(getActivity(), todaySchedules.get(0).getType().getName()));


//            ExerciseFrgment.ExerciseAdapter adpater = new ExerciseFrgment.ExerciseAdapter(getContext(), todaySchedules);
//            recyclerView.setAdapter(adpater);
//            UtilForApp.setDividerItemDecoration(getContext(), recyclerView, R.drawable.divider_shape);
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
//            private TextView tvWeightNum;
//            private TextView tvRestNum;
//            private TextView tvSetNum;
//            private TextView tvCountNum;
//            private TextView tvWeightLabel;
//            private TextView tvRestLabel;
//            private TextView tvSetLabel;
//            private TextView tvCountLabel;


            public ScheduleViewHolder(@NonNull View itemView) {
                super(itemView);
                /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        int pos = getAdapterPosition();
                        Log.d("Test", "setOnClickListener item : " + pos);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(MappingUtil.name(context, list.get(pos).getType().getName()));
                        builder.setMessage("삭제하시겠습니까?");


                        builder.setPositiveButton("예",
                                (dialog, which) -> {
                                    Util.LoadingProgress.show(getContext());
                                    ScheduleRepository.getInstance().deleteSchedule(list.get(pos), new ScheduleDataSource.CompleteCallback() {
                                        @Override
                                        public void onComplete() {
//                                            Intent intent = new Intent();
//                                            intent.setClass(context, ((Activity) context).getClass());
//                                            context.startActivity(intent);
//
//                                            WeeksPlanActivity.isReload = true;
//
//                                            ((Activity) context).finish();

                                            list.remove(pos);
                                            notifyDataSetChanged();
                                            Util.LoadingProgress.hide();
                                        }

                                        @Override
                                        public void onDataNotAvailable() {
                                            Util.LoadingProgress.hide();
                                            Log.e(MainActivity_sub.TAG, "[SchedulesViewPagerFragment] deleteSchedule onDataNotAvailable");
                                        }
                                    });
                                });
                        builder.setNegativeButton("아니오",
                                (dialog, which) -> QToast.showToast(context, "삭제를 취소했습니다."));
                        builder.show();

                        return true;
                    }
                });*/

                ivExerciseIcon = itemView.findViewById(R.id.iv_exercise_icon);
                tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
//                tvWeightNum = itemView.findViewById(R.id.tv_weight_num);
//                tvRestNum = itemView.findViewById(R.id.tv_rest_num);
//                tvSetNum = itemView.findViewById(R.id.tv_set_num);
//                tvCountNum = itemView.findViewById(R.id.tv_count_num);
//                tvWeightLabel = itemView.findViewById(R.id.tv_weight_label);
//                tvRestLabel = itemView.findViewById(R.id.tv_rest_label);
//                tvSetLabel = itemView.findViewById(R.id.tv_set_label);
//                tvCountLabel = itemView.findViewById(R.id.tv_count_label);
            }

            private void onBinding(ScheduleValueObject scheduleVo) {
                ivExerciseIcon.setImageDrawable(context.getResources().getDrawable(getIconResource(scheduleVo.getType())));
                tvExerciseName.setText(MappingUtil.name(context, scheduleVo.getType().getName()));

                itemView.setOnClickListener(v -> {

//                    Toast.makeText(getActivity(), "scheduleVo.getType().getName() : " + scheduleVo.getType().getName(), Toast.LENGTH_SHORT).show();

                    setMainItem(scheduleVo);
                });

//                if (scheduleVo.getType().isTime()) {
//                    tvWeightLabel.setText(R.string.schedules_rpm);
//                    tvCountLabel.setText(R.string.schedules_times);
//                } else {
//                    tvWeightLabel.setText(R.string.schedules_kg);
//                    tvCountLabel.setText(R.string.schedules_count);
//                }
//                tvRestLabel.setText(R.string.schedules_rest);
//                tvSetLabel.setText(R.string.schedules_set);
//
//                tvCountNum.setText(String.format("%02d", scheduleVo.getCount()));
//                tvSetNum.setText(String.format("%02d", scheduleVo.getSetCnt()));
//                tvRestNum.setText(String.format("%02d", scheduleVo.getRest()));
//                tvWeightNum.setText(String.format("%02d", scheduleVo.getWeight()));
            }

            private int getIconResource(TypeValueObject exerciseType) {
                return MappingUtil.exerciseIconResource[exerciseType.getId() - 1];
            }
        }
    }

}
