package com.unesc.wslock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unesc.wslock.R;
import com.unesc.wslock.models.Lock;
import com.unesc.wslock.models.lists.LockList;

import java.util.List;

public class LockSelectListAdapter extends ArrayAdapter<Lock> {
    private final Context context;
    private final List<Lock> data;

    public LockSelectListAdapter(Context context, LockList lockList) {
        super(context, 0, lockList.locks);

        this.context = context;
        this.data = lockList.locks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.lock_list_select_item, parent, false);
        }

        if (this.data != null && this.data.size() > 0) {
            TextView lockNameTextView = view.findViewById(R.id.lock_list_select_item_name);

            Lock lock = this.data.get(position);

            lockNameTextView.setText(lock.getName());
        }

        return view;
    }

    public List<Lock> getData() {
        return data;
    }
}
