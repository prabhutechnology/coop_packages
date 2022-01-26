package com.prabhutech.prabhupackages.wallet.core.utils.customviews;

import static com.prabhutech.prabhupackages.wallet.core.utils.validators.Validators.isValidEmail;
import static com.prabhutech.prabhupackages.wallet.core.utils.validators.Validators.isValidGlobalPhone;
import static com.prabhutech.prabhupackages.wallet.core.utils.validators.Validators.isValidPhone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.prabhutech.prabhupackages.R;

public class EmailPhoneInputView extends FrameLayout implements com.prabhutech.coop.wallet.core.utils.customviews.FormItemV {
    private static final String TAG = "EmailPhoneInputView";

    private TextInputLayout textInputLayout;
    private EditText editText;

    public EmailPhoneInputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmailPhoneInputView, 0, 0);
        int underlineColor = a.getColor(R.styleable.FormInputView_underlineColor, Color.argb(255, 121, 121, 121));
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_email_phone_input, this, true);

        textInputLayout = view.findViewById(R.id.input_layout);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setHintTextAppearance(R.style.InputFieldLayout);
        textInputLayout.setErrorTextAppearance(R.style.InputFieldError);

        editText = view.findViewById(R.id.input);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setBackgroundTintList(ColorStateList.valueOf(underlineColor));
        }

        textInputLayout.setHint("Mobile/Email");

        editText.addTextChangedListener(handleTextChange);
        editText.setOnFocusChangeListener(handleFocusChange);
    }

    TextWatcher handleTextChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("");
            String s = editable.toString();
            if (s.length() < 4) return;

            if (isPhone(s)) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
            } else {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
            }
        }
    };

    OnFocusChangeListener handleFocusChange = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                String s = editText.getText().toString();
                if (s.length() < 4) return;

                if (isPhone(s)) {
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
                    if (isValidGlobalPhone(s)) {
                        textInputLayout.setError("");
                    } else {
                        textInputLayout.setError("Invalid Phone Number");
                    }
                } else {
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                    if (isValidEmail(s)) {
                        textInputLayout.setError("");
                    } else {
                        textInputLayout.setError("Invalid Email Address");
                    }
                }
            }

        }
    };

    public static boolean isPhone(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }

        return false;
    }

    public boolean isPhone() {
        String s = getText();
        return isPhone(s) && isValidPhone(s);
    }

    public boolean isEmail() {
        String s = getText();
        return !isPhone(s) && isValidEmail(s);
    }

    public String getText() {
        if (!TextUtils.isEmpty(editText.getText())) {
            return editText.getText().toString();
        }

        return "";
    }

    public void setText(String loginIdentification) {
        editText.setText(loginIdentification);
    }

    public void setEnabled(boolean enabled) {
        editText.setEnabled(enabled);
    }

    @Override
    public boolean validate() {
        String s = editText.getText().toString();

        if (isPhone(s)) {
            if (isValidGlobalPhone(s)) {
                textInputLayout.setError("");
                return true;
            } else {
                textInputLayout.setError("Invalid Phone Number");
                return false;
            }
        } else {
            if (isValidEmail(s)) {
                textInputLayout.setError("");
                return true;
            } else {
                textInputLayout.setError("Invalid Email Address");
                return false;
            }
        }
    }

    public void setError(String error) {
        textInputLayout.setError(error);
    }
}
