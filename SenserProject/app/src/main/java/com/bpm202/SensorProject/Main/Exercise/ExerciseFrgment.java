package com.bpm202.SensorProject.Main.Exercise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bpm202.SensorProject.CustomView.CircleAngleAnimation;
import com.bpm202.SensorProject.CustomView.CircleView;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.Main.MainDataManager;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.Util.UtilForApp;
import com.bpm202.SensorProject.ValueObject.DayOfWeek;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.Calendar;
import java.util.List;

public class ExerciseFrgment extends Fragment {


    private static final int READY_TIME = 5;
    private static final long COUNT_ANIMATION_DELAY = 200;
    private ReadyTimeUtil mReadyTimeUtil;

    private static ExerciseFrgment instance = null;
    private RecyclerView recyclerView;
    private ConstraintLayout cl_has_exercise;
    private LinearLayout ll_no_exercise;
    private TextView tvDesc;
    private CircleView mCircleView;
    private ConstraintLayout statusLayout;
    private TextView tvWeight;
    private TextView tvCount;
    private TextView tvSet;
    private TextView tvRest;
    private TextView tvWeightLabel;

    private float posX = 0;
    private float posY = 0;


    private float totalAngle;
    private float angle;

    private long startTime;


    public static ExerciseFrgment Instance() {
        if (instance == null) {
            instance = new ExerciseFrgment();
        }
        return instance;
    }

    public static void DestroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_play, container, false); // 여기서 UI를 생성해서 View를 return
        initPreView(v);
        return v;
    }

    private void initPreView(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        statusLayout = v.findViewById(R.id.constraintLayout);
        tvWeight = v.findViewById(R.id.tv_weight_num);
        tvCount = v.findViewById(R.id.tv_count_num);
        tvSet = v.findViewById(R.id.tv_set_num);
        tvRest = v.findViewById(R.id.tv_rest_num);
        tvDesc = v.findViewById(R.id.tv_desc);
        mCircleView = v.findViewById(R.id.iv_exercise);
        tvWeightLabel = v.findViewById(R.id.tv_weight_label);
        cl_has_exercise = v.findViewById(R.id.cl_has_exercise);
        ll_no_exercise = v.findViewById(R.id.ll_no_exercise);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initRecyclerView();
        super.onActivityCreated(savedInstanceState);
    }

    private void initRecyclerView() {

        mReadyTimeUtil = new ReadyTimeUtil();

        int dayCode = Util.CalendarInfo.getCalendar().get(Calendar.DAY_OF_WEEK);
        DayOfWeek dayOfWeek = DayOfWeek.findByCode(dayCode);

        @NonNull List<ScheduleValueObject> todaySchedules = MainDataManager.Instance().getScheduleValueObjectForDay(dayOfWeek);

        if (todaySchedules.size() == 0) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("TODAY");
            cl_has_exercise.setVisibility(View.GONE);
            ll_no_exercise.setVisibility(View.VISIBLE);
        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("SELECT");
            cl_has_exercise.setVisibility(View.VISIBLE);
            ll_no_exercise.setVisibility(View.GONE);
            ExerciseAdapter adpater = new ExerciseAdapter(getContext(), todaySchedules);
            recyclerView.setAdapter(adpater);
            UtilForApp.setDividerItemDecoration(getContext(), recyclerView, R.drawable.divider_shape);
        }

    }

    public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

        private final Context context;
        private List<ScheduleValueObject> list;

        public ExerciseAdapter(Context context, List<ScheduleValueObject> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_play_select, viewGroup, false);
            return new ExerciseViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int position) {
            exerciseViewHolder.onBinding(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }

        class ExerciseViewHolder extends RecyclerView.ViewHolder {

            private CircleView ibtnIcon;
            private TextView tvName;

            public ExerciseViewHolder(@NonNull View itemView) {
                super(itemView);
                ibtnIcon = itemView.findViewById(R.id.ibtn_exercise);
            }

            private void onBinding(ScheduleValueObject scheduleVo) {

                itemView.setOnTouchListener((v, event) -> {
                    final int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    Log.d("x, y", location[0] + ":" + location[1]);
                    posX = location[0]; // x좌표
                    posY = location[1]; // y좌표
                    return false;
                });
                itemView.setOnClickListener(v -> {

                    if (ExerciseManager.Instance().STATE_CLASS.getCurrentState().equals(ExerciseManager.STATE.READY)) {
                        return;
                    }

                    Rect rect = new Rect();
                    getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    int statusbarHeight = rect.top;
                    int toolbarHeight = ((MainActivity) getActivity()).getSupportActionBar().getHeight();

                    ImageView viewSrc = new ImageView(context);
                    viewSrc.setImageDrawable(mCircleView.getResources().getDrawable(R.drawable.ic_example_run));
                    int size = mCircleView.getResources().getDimensionPixelSize(R.dimen.dimen_70);
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(size, size);

                    viewSrc.setLayoutParams(params);
                    viewSrc.setX(posX);
                    viewSrc.setY(posY - statusbarHeight - toolbarHeight);

                    ConstraintLayout layout = getView().findViewById(R.id.parent);
                    layout.addView(viewSrc);

                    int[] location = new int[2];
                    mCircleView.getLocationOnScreen(location);
                    float dimen_69dp = context.getResources().getDimension(R.dimen.dimen_69);

                    viewSrc.animate().translationX(location[0] + dimen_69dp).translationY(location[1] - statusbarHeight - toolbarHeight + dimen_69dp)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    viewSrc.animate().setListener(null);
                                    viewSrc.animate().alpha(0f);
                                    mCircleView.setImageDrawable(getResources().getDrawable(R.drawable.ic_example_run));
                                }
                            });

                    ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.IDLE);

                    float size1 = context.getResources().getDimension(R.dimen.play_circle_size);
                    float stroke = context.getResources().getDimension(R.dimen.play_circle_stroke);

                    mCircleView.setRectResize(size1, stroke);
                    mCircleView.setAngle(0);
                    mCircleView.clearAnimation();
                    mCircleView.invalidate();

                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(MappingUtil.name(context, scheduleVo.getType().getName()));

                    angle = (360f / scheduleVo.getCount());
                    totalAngle = 0;

                    float endPos = 0;
                    float startPos = statusLayout.getHeight();
                    tvDesc.setText(getString(R.string.play_start_by_pressed));
                    statusLayout.animate().translationY(startPos).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            tvCount.setText(String.format("%02d", scheduleVo.getCount()));
                            tvSet.setText(String.format("%02d", scheduleVo.getSetCnt()));
                            tvRest.setText(String.format("%02d", scheduleVo.getRest()));
                            tvWeight.setText(String.format("%02d", scheduleVo.getWeight()));
                            if (scheduleVo.getType().isTime()) {
                                tvWeightLabel.setText(getString(R.string.schedules_rpm));
                            } else {
                                tvWeightLabel.setText(getString(R.string.schedules_kg));
                            }
                            statusLayout.animate().setListener(null);
                            statusLayout.setVisibility(View.VISIBLE);
                            statusLayout.animate().translationY(endPos);
                        }
                    });
                    // 시작 버튼
                    mCircleView.setOnClickListener(btn -> {
                        if (ExerciseManager.Instance().STATE_CLASS.getCurrentState().equals(ExerciseManager.STATE.IDLE)) {
                            Log.d("TEST", "TEST, CircleView Start");
                            tvDesc.setVisibility(View.VISIBLE);
                            tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.font_size_48));
                            ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.READY);
                            tvDesc.setText(String.valueOf(READY_TIME));
                            mReadyTimeUtil.stop();
                            mReadyTimeUtil.setTime(READY_TIME);
                            mReadyTimeUtil.start(time -> {
                                if (time >= 0) {
                                    tvDesc.setText(String.valueOf(time));
                                } else {
                                    ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.RUN);
                                    tvDesc.setVisibility(View.INVISIBLE);
                                    tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.font_size_18));
                                    tvDesc.setText(getString(R.string.play_end_by_pressed));
                                    tvDesc.setVisibility(View.VISIBLE);
                                    startTime = System.currentTimeMillis();
                                }
                            });
                        } else if (ExerciseManager.Instance().STATE_CLASS.getCurrentState().equals(ExerciseManager.STATE.RUN)) {
                            Log.d("TEST", "TEST, CircleView Run");
                            ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.REST);
                            totalAngle = 360f;

                            mCircleView.startAnimation(() -> totalAngle);
                            int i = scheduleVo.getSetCnt();
                            scheduleVo.setSetCnt(--i);

                            //exercisePost(mScheduleVo, mScheduleVo.getSetCnt() - mPlayState.getSet());
                            if (scheduleVo.getSetCnt() <= 0) {
                                scheduleVo.setSuccess(true);
                                notifyDataSetChanged();
                                ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.FINISH);

                                /*if (isAllComplete())
                                    view.allFinishSchedules();
                                else*/

                                float startPos1 = statusLayout.getHeight();
                                tvDesc.setText(getString(R.string.play_good));
                                tvDesc.setVisibility(View.VISIBLE);
                                statusLayout.animate().translationY(startPos1);
                                mCircleView.setImageDrawable(null);


                                return;
                            }

                            ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.REST);

                            tvCount.setText(String.format("%02d", scheduleVo.getCount()));
                            tvSet.setText(String.format("%02d", scheduleVo.getSetCnt()));
                            tvRest.setText(String.format("%02d", scheduleVo.getRest()));
                            angle = 360f / (float) scheduleVo.getRest();
                            /*mPlayState.start(
                                    time -> mPlayState.animation(view, angle, mScheduleVo)
                            );*/
                            totalAngle = 0f;


                        } else {
                            Log.d("TEST", "TEST, CircleView nothing");
                        }
                    });
                });

                if (scheduleVo.isSuccess()) {
                    ibtnIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_example_run_black));
                } else {
                    ibtnIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_example_run));
                }
            }
        }
    }
}