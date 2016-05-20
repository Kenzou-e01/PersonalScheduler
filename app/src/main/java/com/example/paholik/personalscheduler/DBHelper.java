package com.example.paholik.personalscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";

    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static abstract class EventTable implements BaseColumns {
        public static final String EVENTS_TABLE_NAME = "events";
        public static final String EVENTS_COLUMN_TITLE = "title";
        public static final String EVENTS_COLUMN_DESC = "desc";
        public static final String EVENTS_COLUMN_DATE = "time";
        public static final String COMMA = ", ";
        public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + EVENTS_TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY" + COMMA +
            EVENTS_COLUMN_TITLE + " VARCHAR(50)" + COMMA +
            EVENTS_COLUMN_DESC + " VARCHAR(200)" + COMMA +
            EVENTS_COLUMN_DATE + " TEXT" +
            ")";
        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EventTable.SQL_DELETE_TABLE);
        onCreate(db);
    }

    public boolean insertEvent (String title, String desc, String time) {
        LogUtils.d("------- insertEvent", "entered method");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventTable.EVENTS_COLUMN_TITLE, title);
        contentValues.put(EventTable.EVENTS_COLUMN_DESC, desc);
        contentValues.put(EventTable.EVENTS_COLUMN_DATE, time);
        db.insert(EventTable.EVENTS_TABLE_NAME, null, contentValues);

        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + EventTable.EVENTS_TABLE_NAME + " WHERE " + EventTable._ID + "=" + id + "", null);

        return res;
    }

    public int nuberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EventTable.EVENTS_TABLE_NAME);

        return numRows;
    }

    public boolean updateEvent(Integer id, String title, String desc, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventTable.EVENTS_COLUMN_TITLE, title);
        contentValues.put(EventTable.EVENTS_COLUMN_DESC, desc);
        contentValues.put(EventTable.EVENTS_COLUMN_DATE, time);
        db.update(EventTable.EVENTS_TABLE_NAME, contentValues, EventTable._ID + "= ?", new String[] {Integer.toString(id)});

        LogUtils.d("DB", time);

        return true;
    }

    public Integer deleteEvent(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(EventTable.EVENTS_TABLE_NAME, EventTable._ID + "= ?", new String[] {Integer.toString(id)});
    }

    public DBRecords getAllEvents() {
        ArrayList<Integer> list_ids = new ArrayList<>();
        ArrayList<String> list_titles = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + EventTable.EVENTS_TABLE_NAME, null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            list_ids.add(res.getInt(res.getColumnIndex(EventTable._ID)));
            list_titles.add(res.getString(res.getColumnIndex(EventTable.EVENTS_COLUMN_TITLE)));
            res.moveToNext();
        }

        res.close();

        return new DBRecords(list_ids, list_titles);
    }

    public DBRecords getEventsInMonthYear(int month, int year) {
        String dateInQuery = "%." + month + "." + year;

        LogUtils.d("DBHelper", dateInQuery);

        ArrayList<Integer> listIDs = new ArrayList<>();
        ArrayList<String> listDates = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + EventTable.EVENTS_TABLE_NAME + " WHERE " + EventTable.EVENTS_COLUMN_DATE + " LIKE '" + dateInQuery +"'", null);
        res.moveToFirst();

        LogUtils.d("DBHelper", "before while loop");

        while(!res.isAfterLast()) {
            LogUtils.d("DBHelper", "in loop");
            listIDs.add(res.getInt(res.getColumnIndex(EventTable._ID)));
            listDates.add(res.getString(res.getColumnIndex(EventTable.EVENTS_COLUMN_DATE)));
            res.moveToNext();
        }

        res.close();

        return new DBRecords(listIDs, listDates);
    }
}
