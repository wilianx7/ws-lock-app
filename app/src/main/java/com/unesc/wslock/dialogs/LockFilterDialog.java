package com.unesc.wslock.dialogs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.unesc.wslock.R;
import com.unesc.wslock.adapters.LockSelectListAdapter;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.lists.LockList;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.LockService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockFilterDialog extends Dialog {
    private final ProgressBar progressBar;
    private final ListView listView;
    public LockSelectListAdapter lockListAdapter;

    public LockFilterDialog(@NonNull Context context, AdapterView.OnItemClickListener onItemClickListener) {
        super(context);

        setContentView(R.layout.dialog_lock_filter);
        setCancelable(true);

        this.listView = findViewById(R.id.dialog_lock_filter_list_view);
        this.progressBar = findViewById(R.id.dialog_lock_filter_progress_bar);

        this.listView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void show() {
        super.show();

        Window window = this.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        this.loadLocks();
    }

    private void loadLocks() {
        this.showProgressBar();

        LockService lockService = BaseService.getRetrofitInstance().create(LockService.class);
        Call<LockList> request = lockService.index(AuthenticatedUser.getToken(getContext()), null);

        request.enqueue(new Callback<LockList>() {
            @Override
            public void onResponse(Call<LockList> call, Response<LockList> response) {
                if (response.isSuccessful()) {
                    LockList responseBody = response.body();

                    if (getContext() != null && responseBody != null && responseBody.locks.size() > 0) {
                        lockListAdapter = new LockSelectListAdapter(getContext(), responseBody);

                        listView.setAdapter(lockListAdapter);
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
}
