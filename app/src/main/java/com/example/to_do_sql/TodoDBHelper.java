package com.example.to_do_sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "TodoDBB";

    private static final String TABLE_NAME = "todos1";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_time = "time";
    private static final String COLUMN_TASK = "task";

    public static final String COLUMN_REMINDER_HOUR = "hour";
    public static final String COLUMN_REMINDER_MINUTE = "minute";

    public TodoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TASK + " TEXT,"
                + COLUMN_REMINDER_HOUR + " INTEGER,"
                + COLUMN_REMINDER_MINUTE + " INTEGER"
                + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
//    public void addColumn() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_REMINDER_MINUTE + "INTEGER");
////        db.execSQL("ALTER TABLE " + TABLE_NAME + " RENAME COLUMN" + COLUMN_time+);
//        db.close();
//    }
    public void addTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, todo.getTask());
        values.put(COLUMN_REMINDER_HOUR, todo.getReminderHour());
        values.put(COLUMN_REMINDER_MINUTE, todo.getReminderMinute());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Todo getTodo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_TASK,COLUMN_REMINDER_HOUR,COLUMN_REMINDER_MINUTE},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        @SuppressLint("Range") Todo todo = new Todo(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TASK)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_REMINDER_HOUR)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_REMINDER_MINUTE))

        );

        cursor.close();
        return todo;
    }

    public List<Todo> getAllTodos() {
        List<Todo> todoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                todo.setTask(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK)));
                todo.setReminderHour(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_HOUR)));
                todo.setReminderMinute(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_MINUTE)));
                todoList.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todoList;
    }

    public void updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, todo.getTask());
        values.put(COLUMN_REMINDER_HOUR,todo.getReminderHour());
        values.put(COLUMN_REMINDER_MINUTE,todo.getReminderMinute());
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[] { String.valueOf(todo.getId()) });
        db.close();
    }

    public void deleteTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(todo.getId()) });
        db.close();
    }
}

