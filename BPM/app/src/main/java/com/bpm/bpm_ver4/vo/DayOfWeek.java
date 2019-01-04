package com.bpm.bpm_ver4.vo;

public enum DayOfWeek {
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7)
    ;

    private int code;

    DayOfWeek(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static DayOfWeek findByCode(int code) {
        for (DayOfWeek day : values()) {
            if (day.getCode() == code) {
                return day;
            }
        }
        return null;
    }
}
