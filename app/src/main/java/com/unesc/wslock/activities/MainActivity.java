package com.unesc.wslock.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unesc.wslock.R;
import com.unesc.wslock.fragments.LockFragmentList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bottomNavigationView = findViewById(R.id.main_bottom_navigation);

        this.fragmentManager = getSupportFragmentManager();

        this.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigation(item);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private boolean handleNavigation(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_navigation_home_option:
                return true;

            case R.id.bottom_navigation_locks_option:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_container, new LockFragmentList(), null)
                        .setReorderingAllowed(true)
//                        .addToBackStack("name") // name can be null
                        .commit();

                return true;

            case R.id.bottom_navigation_lock_histories_option:
                return true;

            case R.id.bottom_navigation_user_profile_option:
                return true;

            default: return false;
        }
    }
}