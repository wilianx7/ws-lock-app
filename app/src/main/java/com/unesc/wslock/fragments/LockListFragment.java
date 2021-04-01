package com.unesc.wslock.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
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
    private ImageView illustrationImage;
    private MaterialTextView illustrationDescription;
    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_list, container, false);

        this.lockListView = view.findViewById(R.id.fragment_lock_list_view);
        this.progressBar = view.findViewById(R.id.lock_list_progress_bar);
        this.illustrationImage = view.findViewById(R.id.lock_list_illustration);
        this.illustrationDescription = view.findViewById(R.id.lock_list_illustration_description);
        this.floatingActionButton = view.findViewById(R.id.lock_list_floating_action_button);

        this.handleLockListViewItemClick();
        this.handleFloatingButtonClick();
        this.hideIllustrationImage();
        this.loadLocks();

        return view;
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

    private void loadLocks() {
        this.showProgressBar();

        LockService lockService = BaseService.getRetrofitInstance().create(LockService.class);
        Call<LockList> request = lockService.index(AuthenticatedUser.getToken(getContext()), "createdByUser");

        request.enqueue(new Callback<LockList>() {
            @Override
            public void onResponse(Call<LockList> call, Response<LockList> response) {
                if (response.isSuccessful()) {
                    LockList responseBody = response.body();

                    if (responseBody != null && responseBody.locks.size() > 0) {
                        lockListAdapter = new LockListAdapter(getContext(), responseBody);

                        lockListView.setAdapter(lockListAdapter);
                    } else {
                        showIllustrationImage();
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

    private void showIllustrationImage() {
        this.illustrationImage.setVisibility(View.VISIBLE);
        this.illustrationDescription.setVisibility(View.VISIBLE);
    }

    private void hideIllustrationImage() {
        this.illustrationImage.setVisibility(View.GONE);
        this.illustrationDescription.setVisibility(View.GONE);
    }
}