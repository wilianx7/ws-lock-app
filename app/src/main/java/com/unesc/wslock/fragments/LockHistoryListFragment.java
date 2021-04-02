package com.unesc.wslock.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unesc.wslock.R;
import com.unesc.wslock.adapters.LockHistoryListAdapter;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.lists.LockHistoryList;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.LockHistoryService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockHistoryListFragment extends Fragment {
    private ListView lockHistoryListView;
    private LockHistoryListAdapter lockHistoryListAdapter;
    private ProgressBar progressBar;
    private LinearLayout welcomeIllustrationLayout;
    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_history_list, container, false);

        this.lockHistoryListView = view.findViewById(R.id.fragment_lock_history_list_view);
        this.progressBar = view.findViewById(R.id.lock_list_history_progress_bar);
        this.welcomeIllustrationLayout = view.findViewById(R.id.welcome_illustration_layout);
        this.floatingActionButton = view.findViewById(R.id.lock_list_history_floating_action_button);

        this.hideWelcomeIllustration();
        this.loadLockHistories();

        return view;
    }

    public void loadLockHistories() {
        this.showProgressBar();

        LockHistoryService lockHistoryService = BaseService.getRetrofitInstance().create(LockHistoryService.class);
        Call<LockHistoryList> request = lockHistoryService.index(AuthenticatedUser.getToken(getContext()), "lock,user", null);

        request.enqueue(new Callback<LockHistoryList>() {
            @Override
            public void onResponse(Call<LockHistoryList> call, Response<LockHistoryList> response) {
                if (response.isSuccessful()) {
                    LockHistoryList responseBody = response.body();

                    if (getContext() != null && responseBody != null && responseBody.lockHistories.size() > 0) {
                        lockHistoryListAdapter = new LockHistoryListAdapter(getContext(), responseBody);

                        lockHistoryListView.setAdapter(lockHistoryListAdapter);

                        hideWelcomeIllustration();
                    } else {
                        showWelcomeIllustration();
                    }

                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<LockHistoryList> call, Throwable t) {
                hideProgressBar();

                Toast.makeText(getContext(), "Houve um erro ao carregar os históricos", Toast.LENGTH_LONG).show();
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