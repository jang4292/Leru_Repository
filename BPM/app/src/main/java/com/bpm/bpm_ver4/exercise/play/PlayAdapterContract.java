package com.bpm.bpm_ver4.exercise.play;

import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.util.OnItemClickListener;

import java.util.List;

public interface PlayAdapterContract {

    interface View {
        void setOnItemClickListener(OnItemClickListener onItemClickListener);
        void setOnTouchListener(android.view.View.OnTouchListener onTouchListener);
        void setTouchEnable(boolean enable);
        void notifyView();
    }

    interface Model {
        void setSchedule(List<ScheduleVo> scheduleVos);
    }
}
