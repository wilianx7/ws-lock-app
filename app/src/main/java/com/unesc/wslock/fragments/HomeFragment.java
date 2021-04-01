package com.unesc.wslock.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.unesc.wslock.R;
import com.unesc.wslock.activities.LockFormActivity;
import com.unesc.wslock.adapters.HomeListAdapter;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.lists.LockList;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.LockService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ListView homeListView;
    private HomeListAdapter homeListAdapter;
    private ProgressBar progressBar;
    private LinearLayout welcomeIllustrationLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.homeListView = view.findViewById(R.id.fragment_home_list_view);
        this.progressBar = view.findViewById(R.id.home_fragment_progress_bar);
        this.welcomeIllustrationLayout = view.findViewById(R.id.welcome_illustration_layout);

        MaterialButton registerFirstLockBtn = view.findViewById(R.id.register_first_lock_btn);

        registerFirstLockBtn.setOnClickListener(v -> startActivity(new Intent(getContext(), LockFormActivity.class)));

        hideWelcomeIllustration();

        this.loadLockCards();

        return view;
    }

    public void loadLockCards() {
        this.showProgressBar();

        LockService lockService = BaseService.getRetrofitInstance().create(LockService.class);
        Call<LockList> request = lockService.index(AuthenticatedUser.getToken(getContext()), "lastLockHistory.user");

        HomeFragment homeFragment = this;

        request.enqueue(new Callback<LockList>() {
            @Override
            public void onResponse(Call<LockList> call, Response<LockList> response) {
                if (response.isSuccessful()) {
                    LockList responseBody = response.body();

                    if (responseBody != null && responseBody.locks.size() > 0) {
                        homeListAdapter = new HomeListAdapter(getContext(), responseBody, homeFragment);

                        homeListView.setAdapter(homeListAdapter);

                        hideWelcomeIllustration();
                    } else {
                        showWelcomeIllustration();
                    }

                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<LockList> call, Throwable t) {
                hideProgressBar();

                Toast.makeText(getContext(), "Houve um erro ao carregar suas fechaduras", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    private void showWelcomeIllustration() {
        this.welcomeIllustrationLayout.setVisibility(View.VISIBLE);
    }

    private void hideWelcomeIllustration() {
        this.welcomeIllustrationLayout.setVisibility(View.GONE);
    }
}