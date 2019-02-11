package com.bpm202.SensorProject.ValueObject;

public class ScheduleObj extends JsonObj {

    private long id; // 아이디
    private String member; // 회원
    private TypeValueObject type; // 운동
    private DayOfWeek day; // 요일
    private int count; // 횟수
    private int weight; // 무게
    private int setCnt; // 세트
    private int pos; // 순서
    private int rest; // 휴식
    private boolean success;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public TypeValueObject getType() {
        return type;
    }

    public void setType(TypeValueObject type) {
        this.type = type;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSetCnt() {
        return setCnt;
    }

    public void setSetCnt(int setCnt) {
        this.setCnt = setCnt;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
