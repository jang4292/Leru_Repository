package com.bpm.bpm_ver4.exercise.play;

import android.view.View;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.data.State;
import com.bpm.bpm_ver4.data.entity.ExVo;
import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.data.source.ExDataSrouce;
import com.bpm.bpm_ver4.data.source.ExRepository;
import com.bpm.bpm_ver4.data.source.ScheduleDataSource;
import com.bpm.bpm_ver4.data.source.ScheduleRepository;
import com.bpm.bpm_ver4.util.OnItemClickListener;

import java.util.List;

public class PlayPresenter implements PlayContract.Presenter, OnItemClickListener {

    private static final String TAG = PlayPresenter.class.getSimpleName();

    private PlayContract.View view;
    private PlayAdapterContract.Model mAdapterModel;
    private PlayAdapterContract.View  mAdapterView;
    private List<ScheduleVo> scheduleList;
    private ScheduleVo mScheduleVo;
    private ExRepository mExRepository;

    private ReadyTimeUtil mReadyTimeUtil;
    private PlayState mPlayState;
    private float angle;
    private float totalAngle;

    private long startTime;

    private boolean isItemPress = true;
    private boolean isCount = false;

    private static final long COUNT_ANIMATION_DELAY = 200;
    private static final long REST_ANIMATION_DELAY = 1000;
    private static final int READY_TIME = 5;

    private State mState = State.IDLE;


    public PlayPresenter(PlayContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.mPlayState = new PlayState();
        this.mReadyTimeUtil = new ReadyTimeUtil();
        this.mExRepository = ExRepository.getInstance();
    }


    @Override
    public void start() {

    }

    @Override
    public State getState() {
        return mState;
    }

    @Override
    public void setAdapter(PlayAdapterContract.View adapterView, PlayAdapterContract.Model adapterModel) {
        mAdapterModel = adapterModel;
        mAdapterView  = adapterView;
        mAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void loadSchedule() {
        ScheduleRepository repository = ScheduleRepository.getInstance();
        repository.getTodaySchedules(new ScheduleDataSource.LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleVo> scheduleVos) {
                scheduleList = scheduleVos;
                mAdapterModel.setSchedule(scheduleList);
                mAdapterView.notifyView();

                if (scheduleList.size() >= 1) {
                    view.updateIconImage(R.drawable.ic_example_run);
                    view.updateExerciseName(R.string.play_select);
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void setAdapterTouchListener(View.OnTouchListener onTouchListener) {
        mAdapterView.setOnTouchListener(onTouchListener);
    }

    @Override
    public void destroy() {
        mPlayState.stop();
        mReadyTimeUtil.stop();
    }

    @Override
    public void onItemClick(int position) {
        if (isItemPress) {
            view.outAnimation();
            initView(position);
        }
    }


    private void initView(int index) {
        if (scheduleList.size() <= index) {
            return;
        }
        updateState(State.IDLE);
        mReadyTimeUtil.stop();
        ScheduleVo obj = scheduleList.get(index);
        mScheduleVo = obj;
        // update view
        view.updateIconImage(R.drawable.ic_example_run);
        view.updateExerciseName(obj.getType().getName());

        // update animationAngle
        angle = (360f / obj.getCount());
        totalAngle = 0;

        // update PlayState
        mPlayState.setCount(obj.getCount());
        mPlayState.setSet(obj.getSetCnt());
        mPlayState.setRest(obj.getRest());

        view.selectSchedules();
        isCount = true;
    }

    @Override
    public void initData() {
        updateView(mPlayState.getCount(), mPlayState.getSet(), mPlayState.getRest());

        view.updateWeight(mScheduleVo.getWeight());
        if (mScheduleVo.getType().isTime()) view.updateWeightLabel(R.string.schedules_rpm);
        else view.updateWeightLabel(R.string.schedules_kg);
    }

    @Override
    public void endAnimation() {
        if (mPlayState.isThreadRunning()) {
            return;
        }
        isCount = true;

        if (mState == State.FINISH) {
            return;
        }
        updateState(State.RUN);
    }

    @Override
    public void startExercise() {

        updateState(State.READY);
        view.updateReadyTime(READY_TIME);
        mReadyTimeUtil.stop();
        mReadyTimeUtil.setTime(READY_TIME);
        mReadyTimeUtil.start(time -> {
            if (time >= 0) {
                view.updateReadyTime(time);
            } else {
                updateState(State.RUN);
            }
        });
    }

    @Override
    public void endExerciseClick() {
        totalAngle = 360f;
        view.updateCountAnim(totalAngle, COUNT_ANIMATION_DELAY);
        mPlayState.subSet();
        isCount = false;

        exercisePost(mScheduleVo, mScheduleVo.getSetCnt() - mPlayState.getSet());
        if (!mPlayState.hasNext()) {
            mScheduleVo.setSuccess(true);
            mAdapterView.notifyView();
            updateState(State.FINISH);
            return;
        }

        updateState(State.REST);
        updateView(0, mPlayState.getSet(), mPlayState.getRest());
        angle = 360f / (float)mPlayState.getRest();
        mPlayState.start(
                time -> mPlayState.animation(view, angle, mScheduleVo)
        );
        totalAngle = 0f;
    }


    private void exercisePost(ScheduleVo scheduleVo, int set) {
        long duration = (System.currentTimeMillis() - startTime)/1000;
        ExVo.Builder builder = new ExVo.Builder();
        builder.setCount(scheduleVo.getCount());
        builder.setCountMax(scheduleVo.getCount());
        builder.setSetCnt(set);
        builder.setSetMax(scheduleVo.getSetCnt());
        builder.setRest(scheduleVo.getRest());
        builder.setType(scheduleVo.getType());
        builder.setDuration((int)duration);
        ExVo vo = builder.create();

        mExRepository.addExercise(vo, new ExDataSrouce.UploadCallback() {
            @Override
            public void onUploaded() {

            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }



    private void updateView(int count, int set, int rest) {
        view.updateCount(count);
        view.updateSet(set);
        view.updateRest(rest);
    }



    private void updateState(State state) {
        mState = state;

        switch (state) {
            case IDLE:
                mAdapterView.setTouchEnable(true);
                break;

            case READY:
                mAdapterView.setTouchEnable(false);
                break;

            case RUN:
                view.running(true);
                startTime = System.currentTimeMillis();
                break;

            case REST:
                break;

            case FINISH:
                if (isAllComplete())
                    view.allFinishSchedules();
                else
                    view.finishSchedules();

                mAdapterView.setTouchEnable(true);
                break;
        }
    }


    private boolean isAllComplete() {
        for (ScheduleVo vo : scheduleList) {
            if (!vo.isSuccess()) {
                return false;
            }
        }
        return true;
    }


    public void testLoad() {
//        ApiObj<ArrayList<ScheduleObj>> apiObj = FakeData.getScheduleData();
//        ArrayList<ScheduleObj> scheduleObjs = apiObj.obj;
//        App.setScheduleObjs(scheduleObjs);
//        mScheduleObjs = App.getTodaySchedule();
//        mAdapterModel.setScheduleObjs(mScheduleObjs);
//        mAdapterView.notifyView();
//        if (mScheduleObjs.size() >= 1) {
//            view.updateIconImage(R.drawable.ic_example_run);
//            view.initSchedules();
//            view.updateExerciseName(R.string.play_select);
//        } else {
//            view.emptySchedules();
//        }
    }

}
