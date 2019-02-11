package com.bpm202.SensorProject.ValueObject;

import static com.bpm202.SensorProject.Data.CommonData.BODY;
import static com.bpm202.SensorProject.Data.CommonData.COUNT;
import static com.bpm202.SensorProject.Data.CommonData.EQUIP;
import static com.bpm202.SensorProject.Data.CommonData.TIME;

public enum TypeValueObject {

    PUSH_UP(1, "push_up", true, false, false, BODY, COUNT), // distance
    PULL_UP(2, "pull_up", true, false, false, EQUIP, COUNT), // distance
    CYCLE(3, "cycle", false, false, false, EQUIP, TIME),
    BARBELL_CURL(4, "barbell_curl", false, true, true, EQUIP, COUNT), // angle, balance
    DUMBBELL_CURL(5, "dumbbell_curl", false, true, false, EQUIP, COUNT), // angle
    SIT_UP(6, "sit_up", false, true, false, BODY, COUNT), // angle
    SQUAT(7, "squat", false, true, false, BODY, COUNT), // angle
    BARBELL_DEADLIFT(8, "barbell_deadlift", true, false, true, EQUIP, COUNT), // distance, balance
    DUMBBELL_DEADLIFT(9, "dumbbell_deadlift", true, false, false, EQUIP, COUNT), // distance
    BARBELL_ROW(10, "barbell_row", true, false, true, EQUIP, COUNT), // distance, balance
    DUMBBELL_ROW(11, "dumbbell_row", true, false, false, EQUIP, COUNT), // distance
    BARBELL_BENCH_PRESS(12, "barbell_bench_press", true, false, true, EQUIP, COUNT), // distance, balance
    DUMBBELL_BENCH_PRESS(13, "dumbbell_bench_press", true, false, false, EQUIP, COUNT), // distance
    BARBELL_SHOULDER_PRESS(14, "barbell_shoulder_press", true, false, true, EQUIP, COUNT), // distance, balance
    DUMBBELL_SHOULDER_PRESS(15, "dumbbell_shoulder_press", true, false, false, EQUIP, COUNT), // distance
    FRONT_LATERAL_RAISE(16, "front_lateral_raise", false, true, false, EQUIP, COUNT), // angle
    SIDE_LATERAL_RAISE(17, "side_lateral_raise", false, true, false, EQUIP, COUNT), // angle
    OVER_LATERAL_RAISE(18, "over_lateral_raise", false, true, false, EQUIP, COUNT), // angle
    LAT_PULL_DOWN(19, "lat_pull_down", true, false, true, EQUIP, COUNT), // distance, balance
    AB_SLIDE(20, "ab_slide", true, false, false, EQUIP, COUNT) // distance
    ;

    private int id;
    private String name;

    private boolean distance;
    private boolean angle;
    private boolean balance;
    private boolean equipments;
    private boolean time;

    TypeValueObject(int id, String name, boolean distance, boolean angle, boolean balance, boolean equipments, boolean time) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.angle = angle;
        this.balance = balance;
        this.equipments = equipments;
        this.time = time;
    }

    public boolean isEquipments() {
        return equipments;
    }

    public boolean isTime() {
        return time;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean useDistance() {
        return this.distance;
    }

    public boolean useAngle() {
        return this.angle;
    }

    public boolean useBalance() {
        return this.balance;
    }
}
