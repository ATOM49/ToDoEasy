package com.world.atom.todoeasy;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.world.atom.todoeasy.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TodoDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "todoList";

    // tasks table name
    private static final String TABLE_TASKS = "tasks";

    // tasks Table Columns names
    private static final String TASK_ID = "taskID";
    private static final String TASK_TEXT = "taskText";
    private static final String IS_DONE = "isDone";
    private static final String TASK_TYPE = "taskType";

    public TodoDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_taskS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + TASK_ID + "  INTEGER PRIMARY KEY,"
                + TASK_TEXT + " TEXT ," + IS_DONE + " INTEGER,"
                + TASK_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_taskS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new task
    void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_ID, task.getTaskId());
        values.put(TASK_TEXT, task.getTaskText());
        values.put(IS_DONE, task.isDone() ? 1 : 0);
        values.put(TASK_TYPE, task.getTaskType());

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All tasks
    public List<Task> getAlltasks() {
        List<Task> taskList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTaskId(cursor.getInt(0));
                task.setTaskText(cursor.getString(1));
                task.setDone(cursor.getInt(2) > 0);
                task.setTaskType(cursor.getString(3));
                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return task list
        return taskList;
    }

    // Getting All tasks
    public ArrayList<Task> getTasksBasedOnStatus(boolean status) {
        ArrayList<Task> taskList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + IS_DONE + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        String[] statusSelectionArgs = new String[]{String.valueOf(status ? 1 : 0)};

        Cursor cursor = db.rawQuery(selectQuery, statusSelectionArgs);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTaskId(cursor.getInt(0));
                task.setTaskText(cursor.getString(1));
                task.setDone(cursor.getInt(2) > 0);
                task.setTaskType(cursor.getString(3));
                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return task list
        return taskList;
    }

    // Updating single task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_TEXT, task.getTaskText());
        values.put(IS_DONE, task.isDone() ? 1 : 0);
        values.put(TASK_TYPE, task.getTaskType());

        // updating row
        return db.update(TABLE_TASKS, values, TASK_ID + " = ?",
                new String[]{String.valueOf(task.getTaskId())});
    }

    // Deleting single task
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, TASK_ID + " = ?",
                new String[]{String.valueOf(task.getTaskId())});
        db.close();
    }


    // Getting tasks Count
    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
