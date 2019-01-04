package com.bpm.bpm_ver4.exercise.play;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



public class ReadyTimeUtil {

    private static final String TAG = ReadyTimeUtil.class.getSimpleName();

    private int time; // 준비 시간
    private int msgWhat = 0;
    private TimeThread mTimeThread;
    private TimeCallback mTimeCallback;
    private boolean threadRunning = false;

    @SuppressLint("HandlerLeak")
    private Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "msgWhat: " + msg.what);
            if (msgWhat != msg.what) return;
            if (mTimeCallback == null) return;

            time--;
            mTimeCallback.callback(time);

            if (time <= 0)
                mTimeThread.stop();

        }
    };

    interface TimeCallback {
        void callback(int time);
    }


    public boolean isThreadRunning() {
        return threadRunning;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void start(TimeCallback callback) {
        mTimeCallback = callback;
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
                        Log.d(TAG, "readyThread: " + msgWhat);
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
