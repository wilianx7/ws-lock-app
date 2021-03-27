package com.unesc.wslock.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.unesc.wslock.R;
import com.unesc.wslock.models.User;
import com.unesc.wslock.models.lists.UserList;

import java.util.List;

public class LockFormUserSelectListAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final List<User> data;

    public LockFormUserSelectListAdapter(Context context, UserList userList) {
        super(context, 0, userList.users);

        this.context = context;
        this.data = userList.users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.user_list_select_item, parent, false);
        }

        if (this.data != null && this.data.size() > 0) {
            TextView usernameTextView = view.findViewById(R.id.user_list_select_item_name);
            CheckBox userCheckbox = view.findViewById(R.id.user_list_select_item_checkbox);

            User user = this.data.get(position);

            usernameTextView.setText(user.getName());
            userCheckbox.setChecked(user.hasLockAccess());
        }

        return view;
    }

    public List<User> getData() {
        return data;
    }
}
