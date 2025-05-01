package com.example.a3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATETIME = "datetime";
    private static final String COLUMN_STATUS = "status";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_DATETIME + " INTEGER, "
                + COLUMN_STATUS + " TEXT"
                + ")";
        db.execSQL(createTableQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DATETIME, task.getDateTime());
        values.put(COLUMN_STATUS, task.getStatus());
        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }
    public List<Task> getFutureTasks() {
        List<Task> taskList = new ArrayList<>();
        long currentTime = Calendar.getInstance().getTimeInMillis();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS +
                " WHERE " + COLUMN_DATETIME + " > " + currentTime +
                " ORDER BY " + COLUMN_DATETIME + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                task.setDateTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATETIME)));
                task.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }
    public List<Task> getPastTasks() {
        List<Task> taskList = new ArrayList<>();
        long currentTime = Calendar.getInstance().getTimeInMillis();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS +
                " WHERE " + COLUMN_DATETIME + " <= " + currentTime +
                " ORDER BY " + COLUMN_DATETIME + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                task.setDateTime(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATETIME)));
                task.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }
}