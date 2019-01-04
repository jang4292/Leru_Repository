package com.bpm.bpm_ver4.exercise.schedule;

import com.bpm.bpm_ver4.BasePresenter;
import com.bpm.bpm_ver4.BaseView;
import com.bpm.bpm_ver4.data.entity.ScheduleVo;

import java.util.List;

public interface SchedulesContract {

    interface Presenter extends BasePresenter {

        // 스케줄 로드
        void loadSchedule();

    }

    interface View extends BaseView<Presenter> {

        void updateSchedule(List<ScheduleVo> scheduleVos);
        void showMessage(int message);
        void showMessage(String message);

    }
}
