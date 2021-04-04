package com.unesc.wslock.dialogs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.unesc.wslock.R;

public class ConfirmationDialog extends Dialog {
    public ConfirmationDialog(@NonNull Context context, String title, String message, View.OnClickListener onConfirm, View.OnClickListener onCancel) {
        super(context);

        setContentView(R.layout.dialog_confirmation);
        setCancelable(true);

        TextView titleTextView = findViewById(R.id.dialog_confirmation_title);
        TextView messageTextView = findViewById(R.id.dialog_confirmation_message);

        titleTextView.setText(title);
        messageTextView.setText(message);

        MaterialButton confirmButton = findViewById(R.id.dialog_confirmation_positive_btn);
        MaterialButton cancelButton = findViewById(R.id.dialog_confirmation_negative_btn);

        confirmButton.setOnClickListener(onConfirm);
        cancelButton.setOnClickListener(onCancel);
    }

    @Override
    public void show() {
        super.show();

        Window window = this.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }
}
