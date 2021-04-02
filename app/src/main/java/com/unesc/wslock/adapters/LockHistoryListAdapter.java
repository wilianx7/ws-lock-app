package com.unesc.wslock.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.unesc.wslock.R;
import com.unesc.wslock.models.LockHistory;
import com.unesc.wslock.models.lists.LockHistoryList;

import java.text.SimpleDateFormat;
import java.util.List;

public class LockHistoryListAdapter extends ArrayAdapter<LockHistory> {
    private final Context context;
    private final List<LockHistory> data;
    private final SimpleDateFormat brazilianDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public LockHistoryListAdapter(Context context, LockHistoryList lockHistoryList) {
        super(context, 0, lockHistoryList.lockHistories);

        this.context = context;
        this.data = lockHistoryList.lockHistories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.lock_history_list_view_item, parent, false);
        }

        if (this.data != null && this.data.size() > 0) {
            TextView lockName = view.findViewById(R.id.list_view_lock_name);
            TextView username = view.findViewById(R.id.list_view_lock_history_username);
            TextView description = view.findViewById(R.id.list_view_lock_history_description);
            TextView createdAt = view.findViewById(R.id.list_view_lock_history_created_at);

            LockHistory lockHistory = this.data.get(position);

            lockName.setText(lockHistory.getLock().getName());
            username.setText(lockHistory.getUser().getName());
            description.setText(lockHistory.getDescription());
            createdAt.setText(this.brazilianDateFormat.format(lockHistory.getCreatedAt()));

            if (lockHistory.getDescription().equals("Fechadura aberta!")) {
                description.setTextColor(ContextCompat.getColor(context, R.color.green_500));
            } else if (lockHistory.getDescription().equals("Fechadura trancada!")) {
                description.setTextColor(ContextCompat.getColor(context, R.color.red_500));
            }
        }

        return view;
    }

    public List<LockHistory> getData() {
        return data;
    }
}
