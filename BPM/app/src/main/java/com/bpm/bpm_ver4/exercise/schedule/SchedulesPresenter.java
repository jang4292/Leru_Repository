package com.bpm.bpm_ver4.exercise.schedule;

import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.data.source.ScheduleDataSource;
import com.bpm.bpm_ver4.data.source.ScheduleRepository;

import java.util.List;

public class SchedulesPresenter implements SchedulesContract.Presenter {

    private SchedulesContract.View view;

    public SchedulesPresenter(SchedulesContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void loadSchedule() {
        ScheduleRepository repository = ScheduleRepository.getInstance();
        repository.getSchedules(new ScheduleDataSource.LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleVo> scheduleVos) {
                view.updateSchedule(scheduleVos);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void start() {

    }
}
