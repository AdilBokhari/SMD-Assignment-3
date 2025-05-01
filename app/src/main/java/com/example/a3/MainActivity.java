package com.example.a3;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
    CalendarView cvCalendar;
    RecyclerView rvTasks;
    FloatingActionButton fabAddTask;
    DatabaseHelper dbHelper;
    TaskAdapter taskAdapter;
    List<Task> taskList;
    long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        cvCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                selectedDateMillis = calendar.getTimeInMillis();
            }
        });
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });
        loadFutureTasks();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottomNavigation) {
                return true;
            } else if (item.getItemId() == R.id.nav_past) {
                Intent intent = new Intent(MainActivity.this, PastTasks.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
    private void loadFutureTasks() {
        taskList = dbHelper.getFutureTasks();
        if (taskAdapter == null) {
            taskAdapter = new TaskAdapter(this, taskList);
            rvTasks.setAdapter(taskAdapter);
        } else {
            taskAdapter.updateTasks(taskList);
        }
    }
    private void showAddTaskDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.task_dialog);
        dialog.setCancelable(true);
        TextInputEditText etTaskTitle = dialog.findViewById(R.id.etTaskTitle);
        TextInputEditText etTaskDescription = dialog.findViewById(R.id.etTaskDescription);
        TextInputEditText etTaskTime = dialog.findViewById(R.id.etTaskTime);
        TextView tvSelectedDate = dialog.findViewById(R.id.tvSelectedDate);
        Button btnSaveTask = dialog.findViewById(R.id.btnSaveTask);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        Date selectedDate = new Date(selectedDateMillis);
        tvSelectedDate.setText("Selected date: " + dateFormat.format(selectedDate));
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTaskTitle.getText().toString().trim();
                String description = etTaskDescription.getText().toString().trim();
                String timeStr = etTaskTime.getText().toString().trim();
                if (title.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (timeStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a time", Toast.LENGTH_SHORT).show();
                    return;
                }
                int hour = 0;
                int minute = 0;
                try {
                    String[] timeParts = timeStr.split(":");
                    if (timeParts.length != 2) {
                        Toast.makeText(MainActivity.this, "Please enter time in HH:MM format", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    hour = Integer.parseInt(timeParts[0]);
                    minute = Integer.parseInt(timeParts[1]);
                    if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                        Toast.makeText(MainActivity.this, "Please enter a valid time (HH:MM)", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Please enter time in HH:MM format", Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar taskCalendar = Calendar.getInstance();
                taskCalendar.setTimeInMillis(selectedDateMillis);
                taskCalendar.set(Calendar.HOUR_OF_DAY, hour);
                taskCalendar.set(Calendar.MINUTE, minute);
                taskCalendar.set(Calendar.SECOND, 0);
                taskCalendar.set(Calendar.MILLISECOND, 0);
                Task newTask = new Task();
                newTask.setTitle(title);
                newTask.setDescription(description);
                newTask.setDateTime(taskCalendar.getTimeInMillis());
                newTask.setStatus("pending");
                long taskId = dbHelper.addTask(newTask);
                if (taskId != -1) {
                    Toast.makeText(MainActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadFutureTasks();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add task", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    private void init()
    {
        dbHelper = new DatabaseHelper(this);
        cvCalendar = findViewById(R.id.cvCalendar);
        rvTasks = findViewById(R.id.rvTasks);
        fabAddTask = findViewById(R.id.fabAddTask);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        Calendar calendar = Calendar.getInstance();
        selectedDateMillis = calendar.getTimeInMillis();
        cvCalendar.setDate(selectedDateMillis);
    }
}