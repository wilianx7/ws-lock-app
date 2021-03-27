package com.unesc.wslock.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unesc.wslock.R;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.Lock;
import com.unesc.wslock.models.lists.LockList;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.LockService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockListAdapter extends ArrayAdapter<Lock> {
    private final Context context;
    private final List<Lock> data;

    public LockListAdapter(Context context, LockList lockList) {
        super(context, 0, lockList.locks);

        this.context = context;
        this.data = lockList.locks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.lock_list_view_item, parent, false);
        }

        if (this.data != null && this.data.size() > 0) {
            TextView lockName = view.findViewById(R.id.list_view_lock_name);
            TextView createdByUsername = view.findViewById(R.id.list_view_lock_created_by_user);
            ImageView icon = view.findViewById(R.id.list_view_lock_icon);
            LinearLayout deleteButton = view.findViewById(R.id.lock_list_delete_button);

            Lock lock = this.data.get(position);

            lockName.setText(lock.getName());
            createdByUsername.setText(lock.getCreatedByUser().getName());

            if (lock.getState().equals("LOCKED")) {
                icon.setImageResource(R.drawable.ic_baseline_lock_closed_24);
            } else {
                icon.setImageResource(R.drawable.ic_baseline_lock_open_24);
            }

            deleteButton.setOnClickListener(v -> deleteLock(lock));
        }

        return view;
    }

    public List<Lock> getData() {
        return data;
    }

    private void deleteLock(Lock lock) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context, R.style.AlertDialogCustom);

        alertDialogBuilder.setTitle("Exclusão de Fechadura");
        alertDialogBuilder.setMessage("Tem certeza de que deseja excluir? (" + lock.getName() + ")");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("Confirmar", (dialog, which) -> {
            LockService lockService = BaseService.getRetrofitInstance().create(LockService.class);
            Call<Boolean> request = lockService.destroy(AuthenticatedUser.getToken(context), String.valueOf(lock.getId()));

            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        notifyDataSetChanged();

                        Toast.makeText(context, "Fechadura excluída com sucesso!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(context, "Houve um erro ao excluir a fechadura", Toast.LENGTH_LONG).show();
                }
            });
        });

        alertDialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }
}
