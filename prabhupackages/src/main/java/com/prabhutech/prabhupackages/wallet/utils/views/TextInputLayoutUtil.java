package com.prabhutech.prabhupackages.wallet.utils.views;

import android.content.Context;
import android.text.InputFilter;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TextInputLayoutUtil {
    private Context context;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;

    public TextInputLayoutUtil(Context context, TextInputLayout textInputLayout, TextInputEditText textInputEditText) {
        this.context = context;
        this.textInputLayout = textInputLayout;
        this.textInputEditText = textInputEditText;
    }

    public void setHint(String hint) {
        textInputLayout.setHint(hint);
    }

    public void setVisibility(int visibility) {
        textInputLayout.setVisibility(visibility);
    }

    public void setMaxLength(int maxLength) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(maxLength);
        textInputEditText.setFilters(filters);
    }

    public void setInputType(int textType) {
        textInputEditText.setInputType(textType);
    }

    public String getText() {
        return textInputEditText.getText().toString();
    }
}
