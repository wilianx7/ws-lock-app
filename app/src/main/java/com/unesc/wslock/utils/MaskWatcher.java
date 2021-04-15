package com.unesc.wslock.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class MaskWatcher implements TextWatcher {
    private final String mask;
    private boolean isRunning = false;
    private boolean isDeleting = false;

    public MaskWatcher(String mask) {
        this.mask = mask;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        this.isDeleting = count > after;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (this.isRunning || this.isDeleting) {
            return;
        }

        this.isRunning = true;
        int editableLength = editable.length();

        if (editableLength < this.mask.length()) {
            if (this.mask.charAt(editableLength) != '#') {
                editable.append(this.mask.charAt(editableLength));
            } else if (this.mask.charAt(editableLength - 1) != '#') {
                editable.insert(editableLength - 1, mask, editableLength - 1, editableLength);
            }
        } else {
            CharSequence validValue = editable.subSequence(0, this.mask.length());

            editable.clear();

            editable.insert(0, validValue);
        }

        this.isRunning = false;
    }
}
