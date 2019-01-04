package com.bpm.bpm_ver4.exercise.play;

import com.bpm.bpm_ver4.BasePresenter;
import com.bpm.bpm_ver4.BaseView;
import com.bpm.bpm_ver4.data.State;

public interface PlayContract {


    interface View extends BaseView<Presenter> {

        // 스케줄의 운동을 선택 했을 때
        void selectSchedules();
        // 스케줄의 운동을 완료 했을 때
        void finishSchedules();
        // 모든 스케줄의 운동을 완료 했을 때
        void allFinishSchedules();

        //
        void updateIconImage(int imageRes);
        // 타이틀을 운동이름으로 수정
        void updateExerciseName(String name);
        void updateExerciseName(int nameRes);
        // 무게 값 수정
        void updateWeight(int weight);
        // 카운트 값 수정
        void updateCount(int count);
        // 세트 값 수정
        void updateSet(int set);
        // 휴식 값 수정
        void updateRest(int rest);

        //
        void updateDesc(int textRest);
        // weight/rpm 명칭 수정
        void updateWeightLabel(int textRes);
        //
        void updateRestLabel(int textRes);
        //
        void updateSetLabel(int textRes);
        // count/time 등 명칭 수정
        void updateCountLabel(int textRes);

        // 준비시간 표시 업데이트
        void updateReadyTime(int time);
        //
        void running(boolean isRunning);

        void updateCountAnim(float angle, long durationMillis);
        void updateRestAnim(int restTime, float angle, long durationMillis);
        void outAnimation();
    }

    interface Presenter extends BasePresenter {

        State getState();

        // 어뎁터 저장
        void setAdapter(PlayAdapterContract.View adapterView, PlayAdapterContract.Model adapterModel);
        // 뷰의 리스너를 어뎁터에 넣음
        void setAdapterTouchListener(android.view.View.OnTouchListener onTouchListener);

        // 스케줄 로드
        void loadSchedule();

        // 데이터를 뷰에 업데이트 시킴
        void initData();
        // 애니메이션 종료됬을 때
        void endAnimation();
        // 시작
        void startExercise();
        // 버튼을 눌러 세트 종료
        void endExerciseClick();

        // 화면 종료시 처리
        void destroy();

    }
}
