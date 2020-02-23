package com.bpm202.SensorProject.db;


public class TABLE_USER {

    public static final String TABLE_NAME = "_user";

    public static final String COLUMN_IDX = "idx";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_REGION = "region";
    public static final String COLUMN_REGION_STR = "region_str";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT not null, "
            + COLUMN_EMAIL + " TEXT not null, "
            + COLUMN_HEIGHT + " INTEGER , "
            + COLUMN_WEIGHT + " INTEGER , "
            + COLUMN_AGE + " INTEGER , "
            + COLUMN_GENDER + " TEXT not null, "
            + COLUMN_REGION + " INTEGER, "
            + COLUMN_REGION_STR + " TEXT not null "
            + ")";
}
