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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unesc.wslock.R;
import com.unesc.wslock.activities.LockFormActivity;
import com.unesc.wslock.adapters.LockListAdapter;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.Lock;
import com.unesc.wslock.models.lists.LockList;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.LockService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockListFragment extends Fragment {
    private ListView lockListView;
    private LockListAdapter lockListAdapter;
    private ProgressBar progressBar;
    private LinearLayout welcomeIllustrationLayout;
    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_list, container, false);

        this.lockListView = view.findViewById(R.id.fragment_lock_list_view);
        this.progressBar = view.findViewById(R.id.lock_list_progress_bar);
        this.welcomeIllustrationLayout = view.findViewById(R.id.welcome_illustration_layout);
        this.floatingActionButton = view.findViewById(R.id.lock_list_floating_action_button);

        MaterialButton registerFirstLockBtn = view.findViewById(R.id.register_first_lock_btn);

        registerFirstLockBtn.setOnClickListener(v -> startActivity(new Intent(getContext(), LockFormActivity.class)));

        this.handleLockListViewItemClick();
        this.handleFloatingButtonClick();
        this.hideWelcomeIllustration();
        this.loadLocks();

        return view;
    }

    public void loadLocks() {
        this.showProgressBar();

        LockService lockService = BaseService.getRetrofitInstance().create(LockService.class);
        Call<LockList> request = lockService.index(AuthenticatedUser.getToken(getContext()), "createdByUser");

        LockListFragment lockListFragment = this;

        request.enqueue(new Callback<LockList>() {
            @Override
            public void onResponse(Call<LockList> call, Response<LockList> response) {
                if (response.isSuccessful()) {
                    LockList responseBody = response.body();

                    if (getContext() != null && responseBody != null && responseBody.locks.size() > 0) {
                        lockListAdapter = new LockListAdapter(getContext(), responseBody, lockListFragment);

                        lockListView.setAdapter(lockListAdapter);

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

    private void handleLockListViewItemClick() {
        this.lockListView.setOnItemClickListener((parent, view, position, id) -> {
            Lock lock = lockListAdapter.getData().get(position);
            Intent intent = new Intent(getContext(), LockFormActivity.class);

            intent.putExtra("lock", lock);
            startActivity(intent);
        });
    }

    private void handleFloatingButtonClick() {
        this.floatingActionButton.setOnClickListener(v -> startActivity(new Intent(getContext(), LockFormActivity.class)));
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