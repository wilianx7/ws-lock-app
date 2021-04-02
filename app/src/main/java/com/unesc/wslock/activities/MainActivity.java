package com.unesc.wslock.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unesc.wslock.R;
import com.unesc.wslock.fragments.HomeFragment;
import com.unesc.wslock.fragments.LockHistoryListFragment;
import com.unesc.wslock.fragments.LockListFragment;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.services.AuthService;
import com.unesc.wslock.services.BaseService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar = findViewById(R.id.main_activity_toolbar);

        toolbar.setTitle("WS Lock");
        setSupportActionBar(toolbar);

        this.fragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this::handleNavigation);

        this.fragmentManager.beginTransaction()
                .replace(R.id.main_container, new HomeFragment(), null)
                .setReorderingAllowed(true)
                .commit();
    }

    @SuppressLint("NonConstantResourceId")
    private boolean handleNavigation(MenuItem item) {
        this.toolbar.setVisibility(View.GONE);

        switch (item.getItemId()) {
            case R.id.bottom_navigation_home_option:
                this.toolbar.setVisibility(View.VISIBLE);

                fragmentManager.beginTransaction()
                        .replace(R.id.main_container, new HomeFragment(), null)
                        .setReorderingAllowed(true)
                        .commit();

                return true;

            case R.id.bottom_navigation_locks_option:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_container, new LockListFragment(), null)
                        .setReorderingAllowed(true)
                        .commit();

                return true;

            case R.id.bottom_navigation_lock_histories_option:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_container, new LockHistoryListFragment(), null)
                        .setReorderingAllowed(true)
                        .commit();

                return true;

            case R.id.bottom_navigation_user_profile_option:
                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_toolbar_logout_action) {
            this.logout();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AuthService authService = BaseService.getRetrofitInstance().create(AuthService.class);
        Call<Boolean> request = authService.logout(AuthenticatedUser.getToken(MainActivity.this));

        request.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    AuthenticatedUser.deleteUserData(MainActivity.this);

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Houve um erro ao deslogar", Toast.LENGTH_LONG).show();
            }
        });
    }
}