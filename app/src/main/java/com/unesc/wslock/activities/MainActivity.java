package com.unesc.wslock.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unesc.wslock.R;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bottomNavigationView = findViewById(R.id.main_bottom_navigation);

        this.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_navigation_home_option:
                        return true;

                    case R.id.bottom_navigation_locks_option:
                        return true;

                    case R.id.bottom_navigation_lock_histories_option:
                        return true;

                    case R.id.bottom_navigation_user_profile_option:
                        return true;

                    default: return false;
                }
            }
        });
    }
}