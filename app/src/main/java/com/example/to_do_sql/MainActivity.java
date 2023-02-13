package com.example.to_do_sql;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TodoDBHelper todoDBHelper;
    private List<Todo> todoList;
    private ArrayAdapter<Todo> adapter;
    private ListView listView;
    private AppCompatButton toggle;
    private FloatingActionButton addButton;
    private boolean isDarkMode = false;
    final boolean isDarkTheme=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
//        toggle=findViewById(R.id.button_in_appbar);
//        toggle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isDarkTheme) {
//                    setTheme(R.style.Apptheme);
//                } else {
//                    setTheme(R.style.appDarkTheme);
//                }
//                isDarkTheme = !isDarkTheme;
//                recreate();
//            }
//        });
        todoDBHelper = new TodoDBHelper(this);
//        todoDBHelper.addColumn();

        listView = findViewById(R.id.list_view);

        addButton = findViewById(R.id.addButton);

        todoList= new ArrayList<>();

        todoList = todoDBHelper.getAllTodos();
        adapter = new ArrayAdapter<Todo>(this, android.R.layout.simple_list_item_1, todoList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {


                GradientDrawable background = new GradientDrawable();
                background.setCornerRadius(30);


                View view = super.getView(position, convertView, parent);
                if (position == 0) {
                    background.setCornerRadius(25);
                    background.setColor(Color.parseColor("#0c2431"));
                    view.setBackground(background);
                } else if(position == 1){
                    background.setColor(Color.parseColor("#F67280"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }
                else if(position == 2){
                    background.setColor(Color.parseColor("#355C7D"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }else if(position == 3){
                    background.setColor(Color.parseColor("#7aa39a"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }else if(position == 4){
                    background.setColor(Color.parseColor("#363636"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }else if(position == 5){
                    background.setColor(Color.parseColor("#CC527A"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }else if(position == 6){
                    background.setColor(Color.parseColor("#3CA399"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }
                else if(position == 7){
                    background.setColor(Color.parseColor("#99B898"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }else if(position == 8){
                    background.setColor(Color.parseColor("#CC527A"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }else if(position == 9){
                    background.setColor(Color.parseColor("#F26B38"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }else if(position == 10){
                    background.setColor(Color.parseColor("#A7226E"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }else if(position == 11){
                    background.setColor(Color.parseColor("#FF4E50"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }else if(position == 12){
                    background.setColor(Color.parseColor("#45ADA8"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }
                else{
                    background.setColor(Color.parseColor("#3CA399"));
                    background.setCornerRadius(25);
                    view.setBackground(background);
                }
                return view;
            }
        };
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Todo todo = todoList.get(i);
                Intent intent = new Intent(MainActivity.this, UpdateTodoActivity.class);
                intent.putExtra("todo", todo);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                Todo todo = todoList.get(i);
                todoDBHelper.deleteTodo(todo);
                todoList.remove(i);
                adapter.notifyDataSetChanged();
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (int) id, alarmIntent, 0);
                alarmManager.cancel(pendingIntent);
                return true;
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Todo todo = (Todo) data.getSerializableExtra("TODO");
            todoDBHelper.addTodo(todo);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, todo.getReminderHour());
            calendar.set(Calendar.MINUTE, todo.getReminderMinute());
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            intent.putExtra("TODO", todo.getTask());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            todoList.add(todo);
            adapter.notifyDataSetChanged();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        todoList.clear();
        todoList.addAll(todoDBHelper.getAllTodos());
        adapter.notifyDataSetChanged();
    }
}
