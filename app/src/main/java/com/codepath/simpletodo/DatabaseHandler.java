package com.codepath.simpletodo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler singleton = null;

    //Creating a singleton instance for the database
    synchronized static DatabaseHandler getInstance(Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandler(context);
        }
        return singleton;
    }

    private DatabaseHandler(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    //overriding method to create tables of database
    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + Constant.TABLE_TASK_LIST + " ( " +
                Constant.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                Constant.COLUMN_TITLE + " TEXT ," +
                Constant.COLUMN_DATE + " TEXT , " +
                Constant.COLUMN_PRIORITY + " INTEGER , " +
                Constant.COLUMN_STATUS + " TEXT " +
                ")";
        db.execSQL(query);

        query = "CREATE TABLE " + Constant.TABLE_CHECKLIST + " ( " +
                Constant.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                Constant.COLUMN_ITEM_NAME + " TEXT ," +
                Constant.COLUMN_CHECKED + " TEXT , " +
                Constant.COLUMN_LIST_ID + " INTEGER NOT NULL," +
                "FOREIGN KEY (" + Constant.COLUMN_LIST_ID + " ) REFERENCES " +
                Constant.TABLE_TASK_LIST + " (" + Constant.COLUMN_ID + " ));";
        db.execSQL(query);

    }

    //overriding method to upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_TASK_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_CHECKLIST);
        onCreate(db);

    }

    //Get all the database entry from table task_list and convert to list
    public List<TaskEntry> taskListTableToList() {
        List<TaskEntry> entryList = new ArrayList<TaskEntry>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + Constant.TABLE_TASK_LIST + " WHERE 1;";

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_ID)) > 0) {
                TaskEntry entry = new TaskEntry();
                entry.set_id(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_ID)));
                entry.set_title(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_TITLE)));
                entry.set_date(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_DATE)));
                entry.set_priority(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_PRIORITY)));
                entry.set_status(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_STATUS)));
                entryList.add(entry);
            }
            cursor.moveToNext();
        }
        db.close();
        cursor.close();
        return entryList;
    }

    //Delete checklist item by id
    public void deleteCheckListItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constant.TABLE_CHECKLIST, Constant.COLUMN_ID + " = " + id, null);
        db.close();
    }

    //add entry in task_list table
    public long addEntry(TaskEntry entry) {

        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_TITLE, entry.get_title());
        values.put(Constant.COLUMN_DATE, entry.get_date());
        values.put(Constant.COLUMN_PRIORITY, entry.get_priority());
        values.put(Constant.COLUMN_STATUS,entry.get_status());

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(Constant.TABLE_TASK_LIST, null, values);
        Log.i("Database", "Save entry ID " + id);
        db.close();
        return id;
    }
    //add entry in Checklist table
    public long addCheckListItem(CheckListItem item) {


        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_ITEM_NAME, item.get_item_name());
        values.put(Constant.COLUMN_CHECKED, item.get_checked());
        values.put(Constant.COLUMN_LIST_ID, item.get_list_id());

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(Constant.TABLE_CHECKLIST, null, values);
        Log.i("Database", "Save item ID checklist " + id);
        db.close();
        return  id;
    }


    //get entry from task list from id
    public TaskEntry getEntry(long id) {
        TaskEntry entry = new TaskEntry();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + Constant.TABLE_TASK_LIST + " WHERE " + Constant.COLUMN_ID + " = " + id + " ;";

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(Constant.COLUMN_DATE)) != null) {
                entry.set_id(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_ID)));
                entry.set_title(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_TITLE)));
                entry.set_date(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_DATE)));
                entry.set_priority(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_PRIORITY)));
                entry.set_status(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_STATUS)));
            }
            cursor.moveToNext();
        }
        db.close();
        cursor.close();
        return entry;
    }

    //Get all the database entry from table checklist and convert to list
    public List<CheckListItem> checklistToList(long id) {

        List<CheckListItem> noteList = new ArrayList<CheckListItem>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + Constant.TABLE_CHECKLIST + " WHERE " +
                Constant.COLUMN_LIST_ID + " = " + id + " ;";

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_LIST_ID)) >= 0) {
                CheckListItem item = new CheckListItem();
                item.set_id(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_ID)));
                item.set_item_name(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_ITEM_NAME)));
                item.set_checked(cursor.getString(cursor.getColumnIndex(Constant.COLUMN_CHECKED)));
                item.set_list_id(cursor.getInt(cursor.getColumnIndex(Constant.COLUMN_LIST_ID)));
                noteList.add(item);
            }
            cursor.moveToNext();
        }
        db.close();
        cursor.close();
        return noteList;
    }

    //Update checklist table to Checked or unchecked
    public void updateCheckListItemChecked(CheckListItem item) {


        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_CHECKED, item.get_checked());

        SQLiteDatabase db = getWritableDatabase();
        db.update(Constant.TABLE_CHECKLIST, values, Constant.COLUMN_ID + " = " + item.get_id(), null);
        db.close();
    }

    //delete entry from task_list and checklist table
    public void deleteEntry(long id) {

        SQLiteDatabase db = getWritableDatabase();

        db.delete(Constant.TABLE_TASK_LIST, Constant.COLUMN_ID + " = " + id, null);
        db.delete(Constant.TABLE_CHECKLIST, Constant.COLUMN_LIST_ID + " = " + id, null);

        db.close();
    }

    //update task_list table
    public int updateEntry(TaskEntry entry) {

        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_TITLE, entry.get_title());
        values.put(Constant.COLUMN_DATE, entry.get_date());
        values.put(Constant.COLUMN_PRIORITY, entry.get_priority());
        values.put(Constant.COLUMN_STATUS,entry.get_status());

        SQLiteDatabase db = getWritableDatabase();
        long id = db.update(Constant.TABLE_TASK_LIST, values, Constant.COLUMN_ID+" = "+entry.get_id(),null);
        db.close();
        return (int) id;
    }

    //update checklist item
    public void updateCheckListItem(CheckListItem item) {

        Log.i("check",item.get_id()+":"+item.get_item_name());


        if (item.get_id() >= 0) {
            ContentValues values = new ContentValues();
            values.put(Constant.COLUMN_ITEM_NAME, item.get_item_name());

            SQLiteDatabase db = getWritableDatabase();
            db.update(Constant.TABLE_CHECKLIST, values, Constant.COLUMN_ID + " = " + item.get_id(), null);
            db.close();

        } else {
            addCheckListItem(item);
        }


    }
}
