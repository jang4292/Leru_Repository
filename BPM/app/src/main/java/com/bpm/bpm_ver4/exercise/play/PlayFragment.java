package com.bpm.bpm_ver4.exercise.play;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm.bpm_ver4.BaseFragment;
import com.bpm.bpm_ver4.DeviceScanActivity;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.data.State;
import com.bpm.bpm_ver4.exercise.MainActivity;
import com.bpm.bpm_ver4.testpack.FakeData;
import com.bpm.bpm_ver4.util.MappingUtil;
import com.bpm.bpm_ver4.util.anim.CircleAngleAnimation;
import com.bpm.bpm_ver4.util.anim.CircleView;

import java.util.Objects;

public class PlayFragment extends BaseFragment implements PlayContract.View, View.OnTouchListener {

    // tag
    public static final String TAG = PlayFragment.class.getSimpleName();

    private Context mContext;

    // Present
    private PlayPresenter mPresent;

    // View
    private RecyclerView recyclerView;
    private CircleView mCircleView;
    private TextView tvWeight;
    private TextView tvCount;
    private TextView tvSet;
    private TextView tvRest;
    private TextView tvWeightLabel, tvRestLabel, tvSetLabel, tvCountLabel;
    private View statusLayout;
    private TextView tvDesc;

    private float posX = 0;
    private float posY = 0;


    public static PlayFragment newInstance() {
        PlayFragment fragment = new PlayFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        setHasOptionsMenu(true);
        mPresent = new PlayPresenter(this);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(Objects.requireNonNull(getView()));
        PlayAdapter adapter = new PlayAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        mPresent.setAdapter(adapter, adapter);
        mPresent.setAdapterTouchListener(this);
        getView().setOnTouchListener(this);

        if (FakeData.TEST) mPresent.testLoad();
        else mPresent.loadSchedule();
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        mPresent.destroy();
        super.onDestroy();
    }

    private void initView(View view) {
        statusLayout = view.findViewById(R.id.constraintLayout);
        tvDesc = view.findViewById(R.id.tv_desc);
        recyclerView = view.findViewById(R.id.recycler_view);
        mCircleView = view.findViewById(R.id.iv_exercise);
        tvWeight = view.findViewById(R.id.tv_weight_num);
        tvCount = view.findViewById(R.id.tv_count_num);
        tvSet = view.findViewById(R.id.tv_set_num);
        tvRest = view.findViewById(R.id.tv_rest_num);
        tvWeightLabel = view.findViewById(R.id.tv_weight_label);
        tvRestLabel = view.findViewById(R.id.tv_rest_label);
        tvSetLabel = view.findViewById(R.id.tv_set_label);
        tvCountLabel = view.findViewById(R.id.tv_count_label);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.play_bluetooth_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
//            mPresent.count();
            Intent intent = new Intent(getActivity(), DeviceScanActivity.class);
            startActivityForResult(intent, 1000);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setPresenter(PlayContract.Presenter presenter) {

    }

    // 스케줄의 운동 선택 했을때
    @Override
    public void selectSchedules() {
        float endPos = 0;
        float startPos = statusLayout.getHeight();
        tvDesc.setText(getString(R.string.play_start_by_pressed));
        statusLayout.animate().translationY(startPos).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mPresent.initData();
                statusLayout.animate().setListener(null);
                statusLayout.setVisibility(View.VISIBLE);
                statusLayout.animate().translationY(endPos);
            }
        });


        // 시작 버튼
        mCircleView.setOnClickListener(btn -> {
            if (mPresent.getState() == State.IDLE) {
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.font_size_48));
                mPresent.startExercise();
            } else if (mPresent.getState() == State.RUN) {
                mPresent.endExerciseClick();
            }
        });
    }

    // 스케줄의 운동을 완료했을때
    @Override
    public void finishSchedules() {
        float startPos = statusLayout.getHeight();
        tvDesc.setText(getString(R.string.play_good));
        tvDesc.setVisibility(View.VISIBLE);
        statusLayout.animate().translationY(startPos);
        mCircleView.setImageDrawable(null);
    }

    // 스케줄의 모든 운동을 완료했을때
    @Override
    public void allFinishSchedules() {
        float startPos = statusLayout.getHeight();
        tvDesc.setText(getString(R.string.play_finish));
        tvDesc.setVisibility(View.VISIBLE);
        statusLayout.animate().translationY(startPos);
        mCircleView.setImageDrawable(null);
        updateExerciseName(R.string.play_complete);
    }


    @Override
    public void updateIconImage(int imageRes) {
        float size = mContext.getResources().getDimension(R.dimen.play_circle_size);
        float stroke = mContext.getResources().getDimension(R.dimen.play_circle_stroke);

        mCircleView.setRectResize(size, stroke);
        mCircleView.setAngle(0);
        mCircleView.clearAnimation();
        mCircleView.invalidate();
    }

    @Override
    public void updateExerciseName(String name) {
        ((MainActivity) getActivity()).setupTitle(MappingUtil.name(mContext, name));
    }

    @Override
    public void updateExerciseName(int nameRes) {
        ((MainActivity) getActivity()).setupTitle(getString(nameRes));
    }

    // weight 값 업데이트
    @Override
    public void updateWeight(int weight) {
        tvWeight.setText(String.format("%02d", weight));
    }

    // count 값 업데이트
    @Override
    public void updateCount(int count) {
        tvCount.setText(String.format("%02d", count));
    }

    @Override
    public void updateDesc(int textRest) {
    }

    // weight/rpm 등 변경
    @Override
    public void updateWeightLabel(int textRes) {
        tvWeightLabel.setText(getString(textRes));
    }

    @Override
    public void updateRestLabel(int textRes) {
        tvRestLabel.setText(getString(textRes));
    }

    @Override
    public void updateSetLabel(int textRes) {
        tvSetLabel.setText(getString(textRes));
    }

    // count/time 등 변경
    @Override
    public void updateCountLabel(int textRes) {
        tvCountLabel.setText(getString(textRes));
    }

    // 준비시간 값 업데이트
    @Override
    public void updateReadyTime(int time) {
        tvDesc.setText(String.valueOf(time));
    }

    @Override
    public void running(boolean isRunning) {
        tvDesc.setVisibility(View.INVISIBLE);
        tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.font_size_18));
        tvDesc.setText(getString(R.string.play_end_by_pressed));
        tvDesc.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateSet(int set) {
        tvSet.setText(String.format("%02d", set));
    }

    @Override
    public void updateRest(int rest) {
        tvRest.setText(String.format("%02d", rest));
    }

    @Override
    public void updateCountAnim(float angle, long durationMillis) {
        updateAnim(false, 0, angle, durationMillis);
    }

    @Override
    public void updateRestAnim(int restTime, float angle, long durationMillis) {
        updateAnim(true, restTime, angle, durationMillis);
    }

    private void updateAnim(final boolean restTimeEnable, final int restTime, float angle, long durationMillis) {
        CircleAngleAnimation animation = new CircleAngleAnimation(mCircleView, angle);
        animation.setOldAngle(mCircleView.getAngle());
        animation.setDuration(durationMillis - 50);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPresent.endAnimation();
                if (restTimeEnable) {
                    updateRest(restTime);
                }
                animation.setAnimationListener(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mCircleView.startAnimation(animation);
    }

    @Override
    public void outAnimation() {
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusbarHeight = rect.top;
        int toolbarHeight = ((MainActivity) getActivity()).getToolbarHeight();

        ImageView viewSrc = new ImageView(mContext);
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
        float dimen_69dp = mContext.getResources().getDimension(R.dimen.dimen_69);

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

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int[] location = new int[2];
        v.getLocationOnScreen(location);
        Log.d("x, y", location[0] + ":" + location[1]);
        posX = location[0]; // x좌표
        posY = location[1]; // y좌표
        return false;
    }



}
