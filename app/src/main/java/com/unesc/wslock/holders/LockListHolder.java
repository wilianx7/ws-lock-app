package com.unesc.wslock.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unesc.wslock.R;

public class LockListHolder extends RecyclerView.ViewHolder {
    public TextView lock_name;
    public TextView created_by_username;
    public ImageView icon;

    public LockListHolder(@NonNull View itemView) {
        super(itemView);

        this.lock_name = itemView.findViewById(R.id.list_view_lock_name);
        this.created_by_username = itemView.findViewById(R.id.list_view_lock_created_by_user);
        this.icon = itemView.findViewById(R.id.list_view_lock_icon);
    }
}
