package com.unesc.wslock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.unesc.wslock.R;
import com.unesc.wslock.fragments.HomeFragment;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.Lock;
import com.unesc.wslock.models.lists.LockList;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.MQTTService;

import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeListAdapter extends ArrayAdapter<Lock> {
    private final Context context;
    private final List<Lock> data;
    private final HomeFragment homeFragment;
    private final SimpleDateFormat brazilianDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public HomeListAdapter(Context context, LockList lockList, HomeFragment homeFragment) {
        super(context, 0, lockList.locks);

        this.context = context;
        this.data = lockList.locks;
        this.homeFragment = homeFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.home_list_view_item, parent, false);
        }

        if (this.data != null && this.data.size() > 0) {
            TextView lockName = view.findViewById(R.id.home_list_view_item_lock_name);
            ImageView lockNameIcon = view.findViewById(R.id.home_list_view_item_lock_icon);
            TextView lastActivityAt = view.findViewById(R.id.home_list_view_last_activity_at);
            TextView lastActivityByUsername = view.findViewById(R.id.home_list_view_last_executed_by_user);
            ImageView openLockImageView = view.findViewById(R.id.home_list_open_lock_image_view);
            ImageView closeLockImageView = view.findViewById(R.id.home_list_close_lock_image_view);

            Lock lock = this.data.get(position);

            lockName.setText(lock.getName());

            if (lock.getLastLockHistory() != null && lock.getLastLockHistory().getCreatedAt() != null) {
                lastActivityAt.setText(this.brazilianDateFormat.format(lock.getLastLockHistory().getCreatedAt()));
                lastActivityByUsername.setText(lock.getLastLockHistory().getUser().getName());
            }

            if (lock.getState().equals("LOCKED")) {
                lockNameIcon.setImageResource(R.drawable.ic_baseline_lock_red_24);
                openLockImageView.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_inactive_shape));
                closeLockImageView.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_active_shape));
            } else {
                lockNameIcon.setImageResource(R.drawable.ic_baseline_lock_open_teal_24);
                openLockImageView.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_active_shape));
                closeLockImageView.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_inactive_shape));
            }

            openLockImageView.setOnClickListener(v -> openDoor(lock));

            closeLockImageView.setOnClickListener(v -> closeLock(lock));
        }

        return view;
    }

    public List<Lock> getData() {
        return data;
    }

    private void openDoor(Lock lock) {
        MQTTService mqttService = BaseService.getRetrofitInstance().create(MQTTService.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), lock.getJsonMacAddress());
        Call<String> request = mqttService.openDoor(AuthenticatedUser.getToken(context), body);

        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body().equals("success")) {
                        homeFragment.loadLockCards();

                        Toast.makeText(context, "Fechadura destrancada com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Houve um erro ao destrancar a fechadura", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Houve um erro ao destrancar a fechadura", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void closeLock(Lock lock) {
        MQTTService mqttService = BaseService.getRetrofitInstance().create(MQTTService.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), lock.getJsonMacAddress());
        Call<String> request = mqttService.closeDoor(AuthenticatedUser.getToken(context), body);

        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body().equals("success")) {
                        homeFragment.loadLockCards();

                        Toast.makeText(context, "Fechadura trancada com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Houve um erro ao trancar a fechadura", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Houve um erro ao trancar a fechadura", Toast.LENGTH_LONG).show();
            }
        });
    }
}
