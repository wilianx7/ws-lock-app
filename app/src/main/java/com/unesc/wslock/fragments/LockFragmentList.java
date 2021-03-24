package com.unesc.wslock.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.unesc.wslock.R;
import com.unesc.wslock.adapters.LockListAdapter;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.lists.LockList;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.LockService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockFragmentList extends Fragment {
    private RecyclerView recyclerView;
    private LockListAdapter adapter;
    private ProgressBar progressBar;
    private ImageView illustrationImage;
    private MaterialTextView illustrationDescription;
    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_list, container, false);

        this.recyclerView = view.findViewById(R.id.lock_recycler_view);
        this.progressBar = view.findViewById(R.id.lock_list_progress_bar);
        this.illustrationImage = view.findViewById(R.id.lock_list_illustration);
        this.illustrationDescription = view.findViewById(R.id.lock_list_illustration_description);
        this.floatingActionButton = view.findViewById(R.id.lock_list_floating_action_button);

        this.handleFloatingButtonClick();
        this.hideIllustrationImage();
        this.prepareRecyclerView();
        this.loadLocks();

        return view;
    }

    private void handleFloatingButtonClick() {
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    private void loadLocks() {
        this.showProgressBar();

        LockService lockService = BaseService.getRetrofitInstance().create(LockService.class);
        Call<LockList> request = lockService.index("Bearer" + AuthenticatedUser.getToken(getContext()), "createdByUser");

        request.enqueue(new Callback<LockList>() {
            @Override
            public void onResponse(Call<LockList> call, Response<LockList> response) {
                if (response.isSuccessful()) {
                    LockList locks = response.body();

                    if (response.body().locks.size() > 0) {
                        adapter = new LockListAdapter(locks);
                        recyclerView.setAdapter(adapter);
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

    private void prepareRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(this.recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.recycler_view_vertical_divider);

        verticalDecoration.setDrawable(verticalDivider);
        this.recyclerView.addItemDecoration(verticalDecoration);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(this.recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.recycler_view_horizontal_divider);

        horizontalDecoration.setDrawable(horizontalDivider);
        this.recyclerView.addItemDecoration(horizontalDecoration);
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