package com.example.paholik.personalscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String EVENTS_TABLE_NAME = "events";
    public static final String EVENTS_COLUMN_ID = "id";
    public static final String EVENTS_COLUMN_TITLE = "title";
    public static final String EVENTS_COLUMN_DESC = "desc";
    public static final String EVENTS_COLUMN_TIME = "time";

    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EVENTS_TABLE_NAME + "(" +
            "id INTEGER PRIMARY KEY," +
            "title VARCHAR(50)," +
            "desc VARCHAR(200)" +
            "time TEXT" +
        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertEvent (String title, String desc, String time) {
        Log.d("------- insertEvent", "entered method");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_COLUMN_TITLE, title);
        contentValues.put(EVENTS_COLUMN_DESC, desc);
        contentValues.put(EVENTS_COLUMN_TIME, time);
        db.insert(EVENTS_TABLE_NAME, null, contentValues);

        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + EVENTS_TABLE_NAME + " WHERE " + EVENTS_COLUMN_ID + "=" + id + "", null);

        return res;
    }

    public int nuberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EVENTS_TABLE_NAME);

        return numRows;
    }

    public boolean updateEvent(Integer id, String title, String desc, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_COLUMN_TITLE, title);
        contentValues.put(EVENTS_COLUMN_DESC, desc);
        contentValues.put(EVENTS_COLUMN_TIME, time);
        db.update(EVENTS_TABLE_NAME, contentValues, EVENTS_COLUMN_ID + "= ?", new String[] {Integer.toString(id)});

        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(EVENTS_TABLE_NAME, EVENTS_COLUMN_ID + "= ?", new String[] {Integer.toString(id)});
    }

    public DBRecords getAllEvents() {
        ArrayList<Integer> list_ids = new ArrayList<>();
        ArrayList<String> list_titles = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + EVENTS_TABLE_NAME, null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            list_ids.add(res.getInt(res.getColumnIndex(EVENTS_COLUMN_ID)));
            list_titles.add(res.getString(res.getColumnIndex(EVENTS_COLUMN_TITLE)));
            res.moveToNext();
        }

        return new DBRecords(list_ids, list_titles);
    }
}
