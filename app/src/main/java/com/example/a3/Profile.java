package com.example.a3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {
    EditText etName, etEmail;
    Switch switchTheme;
    Button btnSave;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        boolean isDark = sharedPreferences.getBoolean("dark_mode", false);
//        AppCompatDelegate.setDefaultNightMode(
//                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
//        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
//        switchTheme.setChecked(isDark);
//        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            AppCompatDelegate.setDefaultNightMode(
//                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
//            );
//        });
        btnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", etName.getText().toString());
            editor.putString("email", etEmail.getText().toString());
//            editor.putBoolean("dark_mode", switchTheme.isChecked());
            editor.apply();
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavProfile);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_upcoming) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_past) {
                startActivity(new Intent(this, PastTasks.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
    private void init(){
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
//        switchTheme = findViewById(R.id.switchTheme);
        btnSave = findViewById(R.id.btnSaveProfile);
        etName.setText(sharedPreferences.getString("name", ""));
        etEmail.setText(sharedPreferences.getString("email", ""));
    }
}
