package com.example.to_do_sql;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class UpdateTodoActivity extends AppCompatActivity {

    private EditText taskEditText;
    private Button updateButton;


    private TimePicker timePicker;
    private TodoDBHelper todoDBHelper;
    private Todo todo;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo);

        todoDBHelper = new TodoDBHelper(this);
        taskEditText = findViewById(R.id.taskEditText);
        updateButton = findViewById(R.id.updateButton);
        timePicker=findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);

        todoDBHelper = new TodoDBHelper(this);


        Intent intent = getIntent();
        Todo todo = getIntent().getParcelableExtra("todo");


        if (todo != null) {
            taskEditText.setText(todo.getTask());
            timePicker.setHour(todo.getReminderHour());
            timePicker.setMinute(todo.getReminderMinute());
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = taskEditText.getText().toString();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                todo.setTask(task);
                todo.setReminderHour(hour);
                todo.setReminderMinute(minute);

                todoDBHelper.updateTodo(todo);
                setAlarm(hour,minute,todo);
                Intent intent = new Intent(UpdateTodoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setAlarm(int hour, int minute, Todo todo) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("TODO", todo.getTask());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    @Override
    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(taskEditText.getWindowToken(), 0);
        super.onBackPressed();
    }
}