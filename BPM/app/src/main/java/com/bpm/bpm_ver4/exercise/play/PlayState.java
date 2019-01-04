package com.bpm.bpm_ver4.exercise.play;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bpm.bpm_ver4.data.entity.ScheduleVo;

public class PlayState {

    private static final String TAG = PlayState.class.getSimpleName();

    private int count;
    private int set;
    private int rest;
    private Handler mTimeHandler;
    private TimeThread mTimeThread;
    private TimeCallback mTimeCallback;
    private TimeEndCallback mTimeEndCallback;
    private int msgWhat = 0;
    private boolean threadRunning = false;


    interface TimeCallback {
        void callback(int time);
    }

    @SuppressLint("HandlerLeak")
    public PlayState() {
        mTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "msgWhat: " + msg.what);
                if (msgWhat == msg.what) {
                    if (mTimeCallback != null) {
                        rest--;
                        mTimeCallback.callback(rest);
                        if (rest <= 1) {
                            mTimeThread.stop();
                        }
                    }
                }
            }
        };
    }

    public boolean isThreadRunning() {
        return threadRunning;
    }

    public void subCount() {
        if (count > 0) count--;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void subSet() {
        if (set > 0) set--;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public boolean hasNext() {
        if (set <= 0) {
            return false;
        }

        return true;
    }

    public boolean isRest() {
        if (rest > 0) {
            return true;
        }
        return false;
    }


    private static final long REST_ANIMATION_DELAY = 1000;
    private float totalAngle = 360f;
    public void animation(PlayContract.View view, float angle, ScheduleVo scheduleVo) {
        if (isRest()) {
            totalAngle -= angle;
            Log.d(TAG, totalAngle+"");
            view.updateRestAnim(rest, totalAngle, REST_ANIMATION_DELAY);
        }else {
            totalAngle = 360f;
            setCount(scheduleVo.getCount());
            setRest(scheduleVo.getRest());
            view.updateCount(getCount());
            view.updateSet(getSet());
            view.updateRestAnim(0, 0f, REST_ANIMATION_DELAY);
        }
    }


    interface TimeEndCallback {
        void callback();
    }
    /*----------------------------------------------------------------------------*/
    public void start(TimeCallback callback) {
        mTimeCallback = callback;
//        mTimeEndCallback = endCallback;
        msgWhat++;
        mTimeThread = new TimeThread(msgWhat);
        mTimeThread.start();
    }

    public void stop() {
        mTimeCallback = null;
        if (mTimeThread != null) {
            mTimeThread.stop();
            mTimeThread = null;
        }
    }


    private class TimeThread {
        private boolean isRunning;
        private Runnable mRunnable;
        private Thread mThread;
        private int msgWhat;

        public TimeThread(int msgWhat) {
            this.msgWhat = msgWhat;
        }


        public void start() {
            threadRunning = true;
            isRunning = true;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning) {
                        Log.d(TAG, "thread" + msgWhat);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        mTimeHandler.sendEmptyMessage(msgWhat);
                    }
                }
            });

            thread.start();
        }


        public void stop() {
            threadRunning = false;
            isRunning = false;
        }
    }
}
