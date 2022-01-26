package com.prabhutech.prabhupackages.wallet.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.prabhutech.prabhupackages.wallet.core.utils.interfaces.CompletionCallback;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.wallet.core.utils.TalkBackUtils;

public class PinInput extends LinearLayout {
    public static final int MODE_EDITTEXT = 0;
    public static final int MODE_DOTS = 1;
    public static final int MODE_BLOCKS = 2;

    private Context context;
    private int length;
    private EditText[] inputField;
    private View[] pins;
    private int mode;

    ConstraintLayout pinLayout;

    // When mode is edittext
    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;


    // When mode is dots
    TextInputLayout textInputLayoutDots;
    TextInputEditText textInputEditTextDots;

    // Submit callback
    CompletionCallback callback;

    public PinInput(Context context) {
        super(context);
    }

    public PinInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PinInput, 0, 0);

        length = a.getInteger(R.styleable.PinInput_length, 4);
        mode = a.getInt(R.styleable.PinInput_mode, MODE_EDITTEXT);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_pin_input, this, true);

        textInputLayout = v.findViewById(R.id.pinLayout);
        textInputEditText = v.findViewById(R.id.pin);

        textInputLayoutDots = v.findViewById(R.id.pinDotsLayout);
        textInputEditTextDots = v.findViewById(R.id.pinDots);

        pinLayout = v.findViewById(R.id.layout_pins);
        textInputLayout = v.findViewById(R.id.pinLayout);
        textInputEditText = v.findViewById(R.id.pin);

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (TalkBackUtils.talkBackActive(getContext())) {
            textInputLayout.setPasswordVisibilityToggleEnabled(false);
        } else {
            textInputLayout.setPasswordVisibilityToggleEnabled(true);
        }

        if (mode == MODE_EDITTEXT) {
            String hint = a.getString(R.styleable.PinInput_pin_hint);
            textInputLayoutDots.setVisibility(GONE);
            pinLayout.setVisibility(GONE);
            textInputLayout.setErrorEnabled(true);

            if (hint != null) {
                textInputLayout.setHint(hint);
                ViewCompat.setAccessibilityDelegate(textInputEditText, new
                        AccessibilityDelegateCompat() {
                            @Override
                            public void onInitializeAccessibilityNodeInfo(View host,
                                                                          AccessibilityNodeInfoCompat info) {
                                super.onInitializeAccessibilityNodeInfo(host, info);
                                if (info.getText() == null || info.getText().toString().isEmpty()) {
                                    info.setClassName(null);
                                    info.setContentDescription(hint + getResources().getString(R.string.enter_mpin_CD));
                                } else {
                                    info.setClassName(null);
                                    info.setContentDescription(info.getText().toString());
                                }
                            }
                        });
            } else {
                ViewCompat.setAccessibilityDelegate(textInputEditText, new
                        AccessibilityDelegateCompat() {
                            @Override
                            public void onInitializeAccessibilityNodeInfo(View host,
                                                                          AccessibilityNodeInfoCompat info) {
                                super.onInitializeAccessibilityNodeInfo(host, info);
                                if (info.getText() == null || info.getText().toString().isEmpty()) {
                                    info.setClassName(null);
                                    info.setContentDescription(getResources().getString(R.string.enter_mpin_CD));
                                } else {
                                    info.setClassName(null);
                                    info.setContentDescription(info.getText().toString());
                                }
                            }
                        });
            }

            pinLayout.setVisibility(GONE);
        } else if (mode == MODE_DOTS) {

            textInputLayout.setVisibility(GONE);

            pins = new View[4];
            pins[0] = v.findViewById(R.id.pin_1);
            pins[1] = v.findViewById(R.id.pin_2);
            pins[2] = v.findViewById(R.id.pin_3);
            pins[3] = v.findViewById(R.id.pin_4);

            textInputEditTextDots.addTextChangedListener(watchForPinChanges);

            textInputLayoutDots.setErrorEnabled(true);
            textInputEditTextDots.setLongClickable(false);
//            textInputEditTextDots.setCustomSelectionActionModeCallback(removeCopyPaste);
            textInputLayoutDots.setPasswordVisibilityToggleEnabled(false);
            textInputEditTextDots.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (callback != null) callback.onComplete();
                        return true;
                    }
                    return false;
                }
            });

        } else {
            inputField = new EditText[length];

            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            setOrientation(HORIZONTAL);
            setGravity(Gravity.CENTER);

            for (int i = 0; i < length; i++) {
                inputField[i] = new EditText(context);

                inputField[i].setHeight(48);
                inputField[i].setWidth(96);
                inputField[i].setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});

                inputField[i].setTextSize(22f);
                inputField[i].setTextAlignment(TEXT_ALIGNMENT_CENTER);
                inputField[i].setInputType(InputType.TYPE_CLASS_NUMBER);

                this.addView(inputField[i]);
            }

            handlePinInput();

        }

        a.recycle();
    }

    private void handlePinInput() {
        /*
         * Controls whether to update edittext or not
         * Prevents recursive invoking of function like onTextChanged
         * */
        final boolean[] react = {true};

        /*
         * Backspace listener for empty edittext
         * */
        for (int i = 0; i < length; i++) {
            int finalI = i;
            InputFilter filter = (charSequence, start, end, dest, dStart, dEnd) -> {
                if (end == 0 || dStart < dEnd) {
                    if (finalI > 0) {
                        if (inputField.length > 0) {
                            if (react[0]) {
                                react[0] = false;
                                inputField[finalI].setText("");
                                inputField[finalI - 1].requestFocus();
                                react[0] = true;
                            }
                        }
                    }
                }
                return charSequence;
            };

            inputField[i].setFilters(new InputFilter[]{filter});
        }

        for (int k = 0; k < length; k++) {
            int finalK = k;
            inputField[k].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                    String inputChar = charSequence.toString();

                    if (react[0]) {
                        react[0] = false;
                        if (inputChar.length() == 0) {
                            inputField[finalK].setText("");
                        } else {
                            inputField[finalK].setText(inputChar.substring(0, 1));
                            inputField[finalK].setSelection(1);

                            if (finalK < length - 1
                                    && count != 0) {

                                inputField[finalK + 1].requestFocus();

                                if (inputField[finalK + 1].getText().length() > 0)
                                    inputField[finalK + 1].setSelection(inputField[finalK + 1].getText().length());
                            }

                            if (finalK == length - 1) {
                                com.prabhutech.coop.wallet.utils.MiscUtils.hideKeyboard(inputField[finalK], context);
                            }
                        }

                        react[0] = true;
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        invalidate();
        requestLayout();
    }

    public String getText() {

        if (mode == MODE_EDITTEXT) {
            return textInputEditText.getText().toString();
        }

        if (mode == MODE_DOTS) {
            return textInputEditTextDots.getText().toString();
        }

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < length; i++) {
            if (!TextUtils.isEmpty(inputField[i].getText())) {
                stringBuffer.append(inputField[i].getText());
            }
        }

        return stringBuffer.toString();
    }

    public void _requestFocus() {
        if (mode == MODE_DOTS) {
            textInputEditTextDots.requestFocus();
        }
    }

    public void _clearFocus() {
        if (mode == MODE_DOTS) {
            textInputEditTextDots.clearFocus();
        }
    }

    public boolean isValid() {

        if (mode == MODE_EDITTEXT) {
            if (TextUtils.isEmpty(textInputEditText.getText())) {
                textInputLayout.setError("Required");
                return false;
            }

            if (textInputEditText.getText().length() != 4) {
                textInputLayout.setError("Invalid PIN");
                return false;
            }

            textInputLayout.setError("");
            return true;
        }

        if (mode == MODE_DOTS) {
            if (TextUtils.isEmpty(textInputEditTextDots.getText())) {
                textInputLayoutDots.setError("Required");
                return false;
            }

            if (textInputEditTextDots.getText().length() != 4) {
                textInputLayoutDots.setError("Invalid PIN");
                return false;
            }

            textInputLayoutDots.setError("");
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (TextUtils.isEmpty(inputField[i].getText())) {
                return false;
            }
        }
        return true;
    }

    public void setText(String s) {
        if (mode == MODE_EDITTEXT) {
            textInputEditText.setText(s);
        }
    }

    TextWatcher watchForPinChanges = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int length = editable.length();
            for (int i = 0; i < length; i++) {
                pins[i].setBackground(getResources().getDrawable(R.drawable.pin_input_active));
            }

            for (int i = length; i < pins.length; i++) {
                pins[i].setBackground(getResources().getDrawable(R.drawable.pin_input_inactive));
            }
        }
    };

    public void viewPIN(boolean views) {
        if (views) {
            for (int i = length; i < pins.length; i++) {
                pins[i].setBackground(getResources().getDrawable(R.drawable.pin_input_inactive));
            }
        } else {
            for (int i = 0; i < pins.length; i++) {
                pins[i].setBackground(getResources().getDrawable(R.drawable.pin_input_active));
            }
        }

    }

    public void setOnSubmitListener(CompletionCallback callback) {
        this.callback = callback;
    }

    ActionMode.Callback removeCopyPaste = new ActionMode.Callback() {

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }
    };
}
