package com.unesc.wslock.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unesc.wslock.R;
import com.unesc.wslock.holders.LockListHolder;
import com.unesc.wslock.models.Lock;
import com.unesc.wslock.models.lists.LockList;

import java.util.List;

public class LockListAdapter extends RecyclerView.Adapter<LockListHolder> {
    private final List<Lock> data;

    public LockListAdapter(LockList lockList) {
        this.data = lockList.locks;
    }

    @NonNull
    @Override
    public LockListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LockListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lock_list_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LockListHolder holder, int position) {
        holder.lock_name.setText(this.data.get(position).getName());
        holder.created_by_username.setText(this.data.get(position).getCreatedByUser().getName());

        if (this.data.get(position).getState().equals("LOCKED")) {
            holder.icon.setImageResource(R.drawable.ic_baseline_lock_closed_24);
        } else {
            holder.icon.setImageResource(R.drawable.ic_baseline_lock_open_24);
        }
    }

    @Override
    public int getItemCount() {
        return this.data != null ? this.data.size() : 0;
    }
}
