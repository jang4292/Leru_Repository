package com.bpm202.SensorProject.Main.Exercise;

import android.util.Log;

import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.Main.Schedules.SchdulesManager;

public class ExerciseManager {

    private static ExerciseManager instance = null;

    public static ExerciseManager Instance() {
        return (instance == null) ? new ExerciseManager() : instance;
    }

    public static StateClass STATE_CLASS = new StateClass();

    public enum STATE {IDLE, READY, RUN, REST, FINISH, NONE}

    static class StateClass {
        private STATE currentState = STATE.NONE;
        private STATE previousState = STATE.NONE;

        private void setState(STATE state) {
            Log.i(MainActivity.TAG, "[SchdulesManager] pre : " + previousState + " to " + currentState);
            Log.i(MainActivity.TAG, "[SchdulesManager] cur : " + currentState + " to " + state);
            previousState = currentState;
            currentState = state;
        }

        public STATE getCurrentState() {
            return currentState;
        }

        public STATE getPreviousState() {
            return previousState;
        }
    }

    public void setSTATE(STATE state) {
        STATE_CLASS.setState(state);
    }
}
