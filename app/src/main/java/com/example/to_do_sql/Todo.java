package com.example.to_do_sql;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Todo implements Parcelable {

    private int id;
    private String task;
    private int reminderHour;
    private int reminderMinute;
    public Todo(){

    }

    public Todo(String task, int reminderHour, int reminderMinute) {
        this.task = task;
        this.reminderHour = reminderHour;
        this.reminderMinute = reminderMinute;
    }

    public Todo(int id, String task, int reminderHour, int reminderMinute) {
        this.id = id;
        this.task = task;
        this.reminderHour = reminderHour;
        this.reminderMinute = reminderMinute;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setReminderHour(int reminderHour) {
        this.reminderHour = reminderHour;
    }

    public void setReminderMinute(int reminderMinute) {
        this.reminderMinute = reminderMinute;
    }

    protected Todo(Parcel in) {
        id = in.readInt();
        task = in.readString();
        reminderHour = in.readInt();
        reminderMinute = in.readInt();
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public int getReminderHour() {
        return reminderHour;
    }

    public int getReminderMinute() {
        return reminderMinute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(task);
        parcel.writeInt(reminderHour);
        parcel.writeInt(reminderMinute);
    }
    @Override
    public String toString() {
        return task +" "+reminderHour + ":" + reminderMinute;
    }

}
