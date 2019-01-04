package com.bpm.bpm_ver4.data.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bpm.bpm_ver4.vo.JsonObj;
import com.bpm.bpm_ver4.vo.Type;

@Entity(tableName = "ExVo")
public final class ExVo extends JsonObj {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final int id;

    @Nullable
    @ColumnInfo(name = "count")
    private int count;

    @Nullable
    @ColumnInfo(name = "countMax")
    private int countMax;

    @Nullable
    @ColumnInfo(name = "weight")
    private int weight;

    @Nullable
    @ColumnInfo(name = "duration")
    private int duration;

    @Nullable
    @ColumnInfo(name = "setCnt")
    private int setCnt;

    @Nullable
    @ColumnInfo(name = "setMax")
    private int setMax;

    @Nullable
    @ColumnInfo(name = "rest")
    private int rest;

    @Nullable
    @ColumnInfo(name = "created")
    private int created;

    @Nullable
    @ColumnInfo(name = "distance")
    private int distance;

    @Nullable
    @ColumnInfo(name = "angle")
    private int angle;

    @Nullable
    @ColumnInfo(name = "balance")
    private int balance;

    @Nullable
    @ColumnInfo(name = "type")
    private Type type;

    @Nullable
    @ColumnInfo(name = "date")
    private String date;


    public ExVo(@NonNull int id, @Nullable int count,
                @Nullable int countMax, @Nullable int weight,
                @Nullable int duration,
                @Nullable int setCnt, @Nullable int setMax,
                @Nullable int rest, @Nullable int distance,
                @Nullable int angle, @Nullable int balance,
                @Nullable Type type, @Nullable String date) {
        this.id = id;
        this.count = count;
        this.countMax = countMax;
        this.weight = weight;
        this.duration = duration;
        this.setCnt = setCnt;
        this.setMax = setMax;
        this.rest = rest;
        this.distance = distance;
        this.angle = angle;
        this.balance = balance;
        this.type = type;
        this.date = date;
    }


    @NonNull
    public int getId() {
        return id;
    }

    @Nullable
    public int getCount() {
        return count;
    }

    public void setCount(@Nullable int count) {
        this.count = count;
    }

    @Nullable
    public int getCountMax() {
        return countMax;
    }

    public void setCountMax(@Nullable int countMax) {
        this.countMax = countMax;
    }

    @Nullable
    public int getWeight() {
        return weight;
    }

    public void setWeight(@Nullable int weight) {
        this.weight = weight;
    }

    @Nullable
    public int getDuration() {
        return duration;
    }

    public void setDuration(@Nullable int duration) {
        this.duration = duration;
    }

    @Nullable
    public int getSetCnt() {
        return setCnt;
    }

    public void setSetCnt(@Nullable int setCnt) {
        this.setCnt = setCnt;
    }

    @Nullable
    public int getSetMax() {
        return setMax;
    }

    public void setSetMax(@Nullable int setMax) {
        this.setMax = setMax;
    }

    @Nullable
    public int getRest() {
        return rest;
    }

    public void setRest(@Nullable int rest) {
        this.rest = rest;
    }

    @Nullable
    public int getCreated() {
        return created;
    }

    public void setCreated(@Nullable int created) {
        this.created = created;
    }

    @Nullable
    public int getDistance() {
        return distance;
    }

    public void setDistance(@Nullable int distance) {
        this.distance = distance;
    }

    @Nullable
    public int getAngle() {
        return angle;
    }

    public void setAngle(@Nullable int angle) {
        this.angle = angle;
    }

    @Nullable
    public int getBalance() {
        return balance;
    }

    public void setBalance(@Nullable int balance) {
        this.balance = balance;
    }

    @Nullable
    public Type getType() {
        return type;
    }

    public void setType(@Nullable Type type) {
        this.type = type;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    public void setDate(@Nullable String date) {
        this.date = date;
    }

    public static class Builder {
        int id;
        int count;
        int countMax;
        int weight;
        int duration;
        int setCnt;
        int setMax;
        int rest;
        int distance;
        int angle;
        int balance;
        Type type;
        String date;

        public void setId(int id) {
            this.id = id;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setCountMax(int countMax) {
            this.countMax = countMax;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void setSetCnt(int setCnt) {
            this.setCnt = setCnt;
        }

        public void setSetMax(int setMax) {
            this.setMax = setMax;
        }

        public void setRest(int rest) {
            this.rest = rest;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public void setAngle(int angle) {
            this.angle = angle;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ExVo create() {
            return new ExVo(
                    id, count,
                    countMax, weight,
                    duration,
                    setCnt, setMax,
                    rest, distance,
                    angle, balance,
                    type, date
            );
        }
    }

}
