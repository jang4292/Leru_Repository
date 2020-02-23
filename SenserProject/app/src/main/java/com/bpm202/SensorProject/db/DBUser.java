package com.bpm202.SensorProject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.bpm202.SensorProject.ValueObject.PersonalInfoObj;

import java.util.ArrayList;


public class DBUser extends BaseDB {
    public DBUser(Context con) {
        mCon = con;
    }

    public DBUser open() throws SQLException {
        mDatabaseHelper = new DatabaseHelper(mCon);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDatabaseHelper.close();
    }

    public Object AllfetchDB() {
        return getCursorData(mSQLiteDatabase.query(TABLE_USER.TABLE_NAME,
                null, null, null, null, null,
                TABLE_USER.COLUMN_IDX + " " + DESC));
    }


    public ArrayList<PersonalInfoObj> selectDB(Object obj) {
        PersonalInfoObj vo = (PersonalInfoObj) obj;
        String selection = String.format("%s=?", TABLE_USER.COLUMN_NAME);
        String[] selectionArgs = {String.valueOf(vo.getNickname())};
        return getCursorData(mSQLiteDatabase.query(TABLE_USER.TABLE_NAME,
                null, selection, selectionArgs, null, null,
                TABLE_USER.COLUMN_IDX + ASC));
    }


    public ArrayList<PersonalInfoObj> getCursorData(Cursor cursor) {
        ArrayList<PersonalInfoObj> lists = new ArrayList<>();
        int end = cursor.getCount();
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                PersonalInfoObj vo = new PersonalInfoObj();
                vo.setIdx(cursor.getInt(cursor.getColumnIndex(TABLE_USER.COLUMN_IDX)));

                vo.setNickname(cursor.getString(cursor.getColumnIndex(TABLE_USER.COLUMN_NAME)));
                vo.setEmail(cursor.getString(cursor.getColumnIndex(TABLE_USER.COLUMN_EMAIL)));
                vo.setHeight(cursor.getInt(cursor.getColumnIndex(TABLE_USER.COLUMN_HEIGHT)));
                vo.setWeight(cursor.getInt(cursor.getColumnIndex(TABLE_USER.COLUMN_WEIGHT)));
                vo.setAge(cursor.getInt(cursor.getColumnIndex(TABLE_USER.COLUMN_AGE)));
                vo.setGender(cursor.getString(cursor.getColumnIndex(TABLE_USER.COLUMN_GENDER)));
                vo.setRegion(cursor.getInt(cursor.getColumnIndex(TABLE_USER.COLUMN_REGION)));
                vo.setRegionStr(cursor.getString(cursor.getColumnIndex(TABLE_USER.COLUMN_REGION_STR)));

                lists.add(vo);
                count++;
            } while (cursor.moveToNext() || count > end);
        }
        cursor.close();
        return lists;
    }


    @Override
    public Boolean insertDB(Object obj) {
        PersonalInfoObj vo = (PersonalInfoObj) obj;
        ContentValues cv = new ContentValues();
        cv.put(TABLE_USER.COLUMN_NAME, vo.getNickname());
        cv.put(TABLE_USER.COLUMN_EMAIL, vo.getEmail());
        cv.put(TABLE_USER.COLUMN_HEIGHT, vo.getHeight());
        cv.put(TABLE_USER.COLUMN_WEIGHT, vo.getWeight());
        cv.put(TABLE_USER.COLUMN_AGE, vo.getAge());
        cv.put(TABLE_USER.COLUMN_GENDER, vo.getGender());
        cv.put(TABLE_USER.COLUMN_REGION, vo.getRegion());
        cv.put(TABLE_USER.COLUMN_REGION_STR, vo.getRegionStr());

        return mSQLiteDatabase.insert(TABLE_USER.TABLE_NAME, null, cv) > 0;
    }

    @Override
    public Boolean updateDB(Object obj) {
        PersonalInfoObj vo = (PersonalInfoObj) obj;
        ContentValues cv = new ContentValues();

        cv.put(TABLE_USER.COLUMN_NAME, vo.getNickname());
        cv.put(TABLE_USER.COLUMN_EMAIL, vo.getEmail());
        cv.put(TABLE_USER.COLUMN_HEIGHT, vo.getHeight());
        cv.put(TABLE_USER.COLUMN_WEIGHT, vo.getWeight());
        cv.put(TABLE_USER.COLUMN_AGE, vo.getAge());
        cv.put(TABLE_USER.COLUMN_GENDER, vo.getGender());
        cv.put(TABLE_USER.COLUMN_REGION, vo.getRegion());
        cv.put(TABLE_USER.COLUMN_REGION_STR, vo.getRegionStr());

        String selection = String.format("%s=?", TABLE_USER.COLUMN_IDX);
        String[] selectionArgs = {String.valueOf(vo.getIdx())};
        return mSQLiteDatabase.update(TABLE_USER.TABLE_NAME, cv, selection, selectionArgs) > 0;

    }

    @Override
    public Boolean deleteDB(Object obj) {
        int idx = (int) obj;
        String selection = String.format("%s=?", TABLE_USER.COLUMN_IDX);
        String[] selectionArgs = {String.valueOf(idx)};
        return mSQLiteDatabase.delete(TABLE_USER.TABLE_NAME, selection, selectionArgs) > 0;
    }

}
