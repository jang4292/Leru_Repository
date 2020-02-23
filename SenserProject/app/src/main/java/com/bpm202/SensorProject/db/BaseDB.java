package com.bpm202.SensorProject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class BaseDB {

    public Context mCon;
    public SQLiteDatabase mSQLiteDatabase;
    public DatabaseHelper mDatabaseHelper;

    public final String DESC = " desc";
    public final String ASC = " asc";

    public static final int DATABASE_VIRSON_BEFORE = 0;
    public static final int DATABASE_VIRSION = 1;
    public static final String DB_NAME = "BPM202.db";

    private final String[] TABLE_NAMES = {
            TABLE_USER.TABLE_NAME,

    };


    private final String[] CREATE_TABLES_SQL = {
            TABLE_USER.CREATE_TABLE,
    };

    public abstract Object selectDB(Object obj);

    public abstract Boolean insertDB(Object obj);

    public abstract Boolean updateDB(Object obj);

    public abstract Boolean deleteDB(Object obj);

    class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DATABASE_VIRSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                for (String table : CREATE_TABLES_SQL) {
                    db.execSQL(table);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                for (String table : TABLE_NAMES) {
                    db.execSQL("DROP TABLE " + table);
                    Log.d("table drop : ", table);
                }

                for (String table : CREATE_TABLES_SQL) {
                    db.execSQL(table);
                    Log.d("table create : ", "create");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
