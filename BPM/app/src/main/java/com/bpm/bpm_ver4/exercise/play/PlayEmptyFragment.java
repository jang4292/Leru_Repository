package com.bpm.bpm_ver4.exercise.play;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bpm.bpm_ver4.BaseFragment;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.exercise.MainActivity;
import com.bpm.bpm_ver4.exercise.schedule.SchedulesFragment;

public class PlayEmptyFragment extends BaseFragment {

    private static final String TAG = PlayEmptyFragment.class.getSimpleName();

    public static PlayEmptyFragment newInstance() {
        PlayEmptyFragment fragment = new PlayEmptyFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_empty, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        getView().findViewById(R.id.button).setOnClickListener(v -> {
//            MainActivity activity = (MainActivity) getActivity();
//            activity.setupTitle(getString(R.string.title_schedules));
//            activity.moveFragment(SchedulesFragment.newInstance());
//            MenuItem menuItem = activity.getMenuItem(R.id.nav_schedules);
//            menuItem.setChecked(true);
//        });
    }

//    public void count() {
//        if (!isCount) {
//            return;
//        }
//        // state
//        mPlayState.subCount();
//        view.updateCount(mPlayState.getCount());
//
//        // animation
//        totalAngle += angle;
//        view.updateCountAnim(totalAngle, 200);
//
//
//        // 모든 세트가 끝났을 때
//        if (mPlayState.getSet() <= 1 && mPlayState.getCount() <= 0) {
//            view.updateCountAnim(360f, 200);
//            isCount = false;
//            isItemPress = true;
//
//            boolean isAllScuccess = true;
//            mScheduleVo.setSuccess(true);
//            for (ScheduleVo obj : scheduleList) {
//                if (!obj.isSuccess()) {
//                    isAllScuccess = false;
//                    break;
//                }
//            }
//
//            view.updateSet(0);
//            view.updateCount(0);
//            view.updateRest(0);
//            mAdapterView.notifyView();
//
//            if (isAllScuccess) {
//                view.allFinishSchedules();
//            } else {
//                view.finishSchedules();
//            }
//            return;
//        }
//
//
//        // 모든 카운트를 완료
//        if (mPlayState.getCount() <= 0) {
//            view.updateCountAnim(360f, COUNT_ANIMATION_DELAY);
//            view.updateRest(mPlayState.getRest());
//            isCount = false;
//
//            mPlayState.subSet();
//            view.updateSet(mPlayState.getSet());
//            angle = 360 / mPlayState.getRest();
//            mPlayState.stop();
//            mPlayState.start(callback -> {
//                totalAngle -= angle;
//
//                if (mPlayState.getRest() > 0) {
//                    view.updateRestAnim(mPlayState.getRest(), totalAngle, REST_ANIMATION_DELAY);
//                }
//                else {
//                    mPlayState.setCount(mScheduleVo.getCount());
//                    mPlayState.setRest(mScheduleVo.getRest());
//                    angle = (360f / mScheduleVo.getCount());
//                    totalAngle = 0;
//                    view.updateCount(mPlayState.getCount());
//                    view.updateSet(mPlayState.getSet());
//                    view.updateRestAnim(0, 0f, REST_ANIMATION_DELAY);
//                }
//            });
//        }
//
//    }


}
