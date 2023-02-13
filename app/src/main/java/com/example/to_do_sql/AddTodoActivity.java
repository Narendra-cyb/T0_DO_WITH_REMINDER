package com.example.to_do_sql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddTodoActivity extends AppCompatActivity {

    private EditText taskEditText;
    private Button saveButton;
    private TodoDBHelper todoDBHelper;

    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        taskEditText = findViewById(R.id.taskEditText);
        saveButton = findViewById(R.id.saveButton);
        timePicker=findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);

//        todoDBHelper.addColumn();
        todoDBHelper= new TodoDBHelper(this);

//        taskEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(taskEditText.getWindowToken(), 0);
//                    return true;
//                }
//                return false;
//            }
//        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = taskEditText.getText().toString();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                Todo todo = new Todo(task,hour,minute);
                setAlarm(hour,minute,todo);
                todoDBHelper.addTodo(todo);
                finish();
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