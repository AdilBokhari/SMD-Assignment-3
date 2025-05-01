package com.example.a3;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class PastTasks extends AppCompatActivity {
    RecyclerView rvPastTasks;
    DatabaseHelper dbHelper;
    TaskAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_tasks);
        init();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_past);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_upcoming) {
                Intent intent = new Intent(PastTasks.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_past) {
                return true;
            } else if (item.getItemId()==R.id.nav_profile) {
                Intent intent = new Intent(PastTasks.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
    private void init(){
        rvPastTasks = findViewById(R.id.rvPastTasks);
        rvPastTasks.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);
        List<Task> pastTasks = dbHelper.getPastTasks();
        adapter = new TaskAdapter(this, pastTasks);
        rvPastTasks.setAdapter(adapter);
    }
}