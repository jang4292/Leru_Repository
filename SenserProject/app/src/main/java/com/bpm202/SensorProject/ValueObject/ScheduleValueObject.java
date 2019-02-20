package com.bpm202.SensorProject.ValueObject;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bpm202.SensorProject.Data.DayOfWeek;

/*import com.bpm.bpm_ver4.vo.DayOfWeek;
import com.bpm.bpm_ver4.vo.ScheduleObj;
import com.bpm.bpm_ver4.vo.Type;*/

//@Entity(tableName = "scheduleVo")
@Entity(tableName = "scheduleValueObject")
public final class ScheduleValueObject extends JsonObj {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final int id;

    @Nullable
    @ColumnInfo(name = "member")
    private final String member;

    @Nullable
    @ColumnInfo(name = "type")
    private final TypeValueObject type;

    @Nullable
    @ColumnInfo(name = "day")
    private final DayOfWeek day;

    @Nullable
    @ColumnInfo(name = "count")
    private int count;

    @Nullable
    @ColumnInfo(name = "weight")
    private int weight;

    @Nullable
    @ColumnInfo(name = "setCnt")
    private int setCnt;

    @Nullable
    @ColumnInfo(name = "pos")
    private int pos;

    @Nullable
    @ColumnInfo(name = "rest")
    private int rest;

    @Nullable
    @ColumnInfo(name = "success")
    private boolean success;


    public ScheduleValueObject(@NonNull int id, @Nullable String member,
                               @Nullable TypeValueObject type,
                               @Nullable DayOfWeek day, @Nullable int count,
                               @Nullable int weight, @Nullable int setCnt,
                               @Nullable int pos,
                               @Nullable int rest, @Nullable boolean success) {
        this.id = id;
        this.member = member;
        this.type = type;
        this.day = day;
        this.count = count;
        this.weight = weight;
        this.setCnt = setCnt;
        this.pos = pos;
        this.rest = rest;
        this.success = success;
    }


    @NonNull
    public int getId() {
        return id;
    }

    @Nullable
    public DayOfWeek getDay() {
        return day;
    }

    public ScheduleObj getConvertObj() {
        ScheduleObj obj = new ScheduleObj();
        obj.setId(id);
        obj.setMember(member);
        obj.setType(type);
        obj.setDay(day);
        obj.setCount(count);
        obj.setWeight(weight);
        obj.setCount(setCnt);
        obj.setPos(pos);
        obj.setRest(rest);
        return obj;
    }

    @Nullable
    public String getMember() {
        return member;
    }

    @Nullable
    public TypeValueObject getType() {
        return type;
    }

    @Nullable
    public int getCount() {
        return count;
    }

    @Nullable
    public int getWeight() {
        return weight;
    }

    @Nullable
    public int getSetCnt() {
        return setCnt;
    }

    @Nullable
    public int getPos() {
        return pos;
    }

    @Nullable
    public int getRest() {
        return rest;
    }

    @Nullable
    public boolean isSuccess() {
        return success;
    }


    public void setCount(@Nullable int count) {
        this.count = count;
    }

    public void setWeight(@Nullable int weight) {
        this.weight = weight;
    }

    public void setSetCnt(@Nullable int setCnt) {
        this.setCnt = setCnt;
    }

    public void setPos(@Nullable int pos) {
        this.pos = pos;
    }

    public void setRest(@Nullable int rest) {
        this.rest = rest;
    }

    public void setSuccess(@Nullable boolean success) {
        this.success = success;
    }

    public static class Builder {
        int id;
        String member;
        TypeValueObject type;
        DayOfWeek day;
        int count, weight, setCnt, pos, rest;
        boolean success;

        public void setId(int id) {
            this.id = id;
        }

        public void setMember(String member) {
            this.member = member;
        }

        public void setType(TypeValueObject type) {
            this.type = type;
        }

        public void setDay(DayOfWeek day) {
            this.day = day;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void setSetCnt(int setCnt) {
            this.setCnt = setCnt;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public void setRest(int rest) {
            this.rest = rest;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public ScheduleValueObject create() {
            return new ScheduleValueObject(
                    id, member,
                    type, day,
                    count, weight,
                    setCnt, pos,
                    rest, success);
        }
    }

}
