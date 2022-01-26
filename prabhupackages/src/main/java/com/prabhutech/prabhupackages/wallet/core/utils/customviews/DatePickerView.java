package com.prabhutech.prabhupackages.wallet.core.utils.customviews;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.hornet.dateconverter.DateConverter;
import com.hornet.dateconverter.Model;
import com.prabhutech.prabhupackages.R;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerView extends FrameLayout {
    public static final String TAG = "DatePickerView";

    public static final String BS = "BS";
    public static final String AD = "AD";

    Context context;
    FragmentActivity fragmentActivity;

    private TextInputLayout textInputLayout;
    private EditText editText;

    private String hint;
    private boolean required;

    private long minDate = 0, maxDate = 0;

    String mode = AD;

    DatePickerDialog datePickerDialogAD;
    com.hornet.dateconverter.DatePicker.DatePickerDialog datePickerDialogBS;

    public void setFragmentActivity(FragmentActivity activity) {
        this.fragmentActivity = activity;
    }


    public DatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Locale locale = new Locale("en", "np");
        Locale.setDefault(locale);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FormInputView, 0, 0);
        hint = a.getString(R.styleable.FormInputView_hint);
        required = a.getBoolean(R.styleable.FormInputView_required, false);
        if (TextUtils.isEmpty(hint)) hint = "Date";

        String extra = a.getString(R.styleable.FormInputView_extra);
        boolean iconEnabled = a.getBoolean(R.styleable.FormInputView_iconEnabled, false);
        int underlineColor = a.getColor(R.styleable.FormInputView_underlineColor, Color.argb(255, 121, 121, 121));
        if (extra == null) extra = "";
        a.recycle();

        editText = new EditText(new ContextThemeWrapper(context, R.style.InputFieldLayoutdate));
        editText.setLongClickable(false);
        textInputLayout = new TextInputLayout(new ContextThemeWrapper(context, R.style.InputFieldLayout2));
        textInputLayout.setHintTextAppearance(R.style.InputFieldLayout2);
        textInputLayout.setErrorTextAppearance(R.style.InputFieldError);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setBackgroundTintList(ColorStateList.valueOf(underlineColor));
        }

        /**
         * If simple show only edittext -- no animation, no error
         * */
        if (extra.equals("simple")) {
            addView(editText);
            editText.setHint("Date");
            if (iconEnabled)
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);
        } else {
            textInputLayout.addView(editText);
            setupHint();
            addView(textInputLayout);
        }

        editText.setFocusable(false);
        editText.setOnClickListener(view -> {
            if (mode.equals(AD))
                showEnglishDatePickerDialog();
            else
                showNepaliDatePickerDialog();
        });

        textInputLayout.setErrorEnabled(true);

        initPickerDialogs();
        ViewCompat.setAccessibilityDelegate(editText, new
                AccessibilityDelegateCompat() {
                    @Override
                    public void onInitializeAccessibilityNodeInfo(View host,
                                                                  AccessibilityNodeInfoCompat info) {
                        super.onInitializeAccessibilityNodeInfo(host, info);
                        info.setClassName(null);
                        info.setContentDescription(getResources().getString(R.string.select_CD) + hint + getResources().getString(R.string.datepicker_CD));
                    }
                });
    }

    private void initPickerDialogs() {
        // init AD calendar
        Calendar calendar = Calendar.getInstance();

        datePickerDialogAD = new DatePickerDialog(context,
                  R.style.DialogTheme,
                (datePicker, year, monthOfYear, dayOfMonth) -> {
                    String date = setFormattedDate(year, monthOfYear + 1, dayOfMonth);
                    setText(date);

                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));
        datePickerDialogAD.getDatePicker().getTouchables().get(0).performClick();


        // removes white background with date in some devices
        datePickerDialogAD.setTitle(null);

        // init BS calendar

        datePickerDialogBS = new com.hornet.dateconverter.DatePicker.DatePickerDialog().newInstance(null);
        datePickerDialogBS.showYearPickerFirst(true);
        datePickerDialogBS.setVersion(com.hornet.dateconverter.DatePicker.DatePickerDialog.Version.VERSION_2);

    }

    public void setRequired(boolean required) {
        this.required = required;
        setupHint();
    }

    public void setDrawableRight(int right) {
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, right, 0);
    }

    private void setupHint() {
        if (required)
            textInputLayout.setHint(hint);
        else
            textInputLayout.setHint(hint + " (optional)");
    }

    public void setErrorText(String error) {
        textInputLayout.setError(error);
    }

    private void showEnglishDatePickerDialog() {

        textInputLayout.setError("");

        // Set max date
        if (maxDate != 0) {
            datePickerDialogAD.getDatePicker().setMaxDate(maxDate);
        }

        if (minDate != 0) {
            datePickerDialogAD.getDatePicker().setMinDate(minDate);
        }

        datePickerDialogAD.show();
    }

    private void showNepaliDatePickerDialog() {

        datePickerDialogBS.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
            String date = setFormattedDate(year, monthOfYear + 1, dayOfMonth);
            setText(date);
        });
        textInputLayout.setError("");

        if (maxDate != 0) {
            Calendar cMax = Calendar.getInstance();
            cMax.setTimeInMillis(maxDate);

            // Max in AD
            Model maxDModel = new Model();
            maxDModel.setYear(cMax.get(Calendar.YEAR));
            maxDModel.setMonth(cMax.get(Calendar.MONTH));
            maxDModel.setDay(cMax.get(Calendar.DAY_OF_MONTH));
            maxDModel.setDayOfWeek(cMax.get(Calendar.DAY_OF_WEEK));

            DateConverter dc = new DateConverter();
            Model maxDModelBS = dc.getNepaliDate(
                    maxDModel.getYear(),
                    maxDModel.getMonth() + 1,
                    maxDModel.getDay());
            datePickerDialogBS.setMaxDate(maxDModelBS);
        }

        if (minDate != 0) {
            Calendar cMin = Calendar.getInstance();
            cMin.setTimeInMillis(minDate);

            // Min in AD
            Model minDModel = new Model();
            minDModel.setYear(cMin.get(Calendar.YEAR));
            minDModel.setMonth(cMin.get(Calendar.MONTH));
            minDModel.setDay(cMin.get(Calendar.DAY_OF_MONTH));
            minDModel.setDayOfWeek(cMin.get(Calendar.DAY_OF_WEEK));

            DateConverter dc = new DateConverter();
            Model minDModelBS = dc.getNepaliDate(
                    minDModel.getYear(),
                    minDModel.getMonth() + 1,
                    minDModel.getDay());
            datePickerDialogBS.setMinDate(minDModelBS);
        }


        if (fragmentActivity != null) {
            datePickerDialogBS.show(fragmentActivity.getSupportFragmentManager(), "DatePickerDialog");
        } else {
            datePickerDialogBS.show(((FragmentActivity) context).getSupportFragmentManager(), "DatePickerDialog");
        }

    }

    public void setDateMode(String newMode) {
        this.mode = newMode;
    }

    public void updateDateMode(String newMode) {
        if (this.mode.equals(AD) && newMode.equals(BS)) {
            setText(getDate(BS));
        } else if (this.mode.equals(BS) && newMode.equals(AD)) {
            setText(getDate(AD));
        }
        this.mode = newMode;
    }

    private String setFormattedDate(int year, int monthOfYear, int dayOfMonth) {
        return String.format("%s-%02d-%02d", year, monthOfYear, dayOfMonth);
    }

    public String getText() {
        if (!TextUtils.isEmpty(editText.getText())) return editText.getText().toString();
        else return "";
    }

    public void setText(String s) {
        if (!TextUtils.isEmpty(s))
            textInputLayout.setErrorEnabled(false);

        editText.setText(s);
    }

    public String getDate(String mode) {

        try {
            if (mode.equals(BS))
                return getDateBS();
            else
                return getDateAD();
        } catch (Exception e) {
            return "";
        }
    }

    private String getDateBS() throws Exception {
        if (isEmpty()) return "";

        if (this.mode.equals(BS)) {
            return editText.getText().toString();
        }

        DateConverter dc = new DateConverter();

        String[] engDate = editText.getText().toString().split("-");
        if (engDate.length != 3) return "";

        Model dcOutput = dc.getNepaliDate(Integer.parseInt(engDate[0]), Integer.parseInt(engDate[1]), Integer.parseInt(engDate[2]));
        String npDate = setFormattedDate(dcOutput.getYear(), dcOutput.getMonth() + 1, dcOutput.getDay());
        return npDate;
    }

    private String getDateAD() throws Exception {

        if (isEmpty()) return "";

        if (this.mode.equals(AD)) {
            return editText.getText().toString();
        }

        DateConverter dc = new DateConverter();

        String[] npDate = editText.getText().toString().split("-");
        if (npDate.length != 3) return "";

        Model dcOutput = dc.getEnglishDate(Integer.parseInt(npDate[0]), Integer.parseInt(npDate[1]), Integer.parseInt(npDate[2]));
        String engDate = setFormattedDate(dcOutput.getYear(), dcOutput.getMonth() + 1, dcOutput.getDay());
        return engDate;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(editText.getText());
    }

    public void setEnabled(boolean enabled) {
        editText.setEnabled(enabled);
    }

    public boolean isValid() {
        if (isEmpty()) {
            if (required) {
                textInputLayout.setError(hint + " " + getResources().getString(R.string.is_required));
                return false;
            } else {
                textInputLayout.setError("");
                return true;
            }
        } else {
            if (isValidDate(getText())) {
                textInputLayout.setError("");
                return true;
            } else {
                textInputLayout.setError("Please enter valid date");
                return false;
            }
        }
    }

    public boolean isValidDate(String date) {
        // TODO: may require more validation (eg. check date from future and limit on past)
        return true;
    }

    public void setMaxDate(int calendar, int duration) {
        Calendar c = Calendar.getInstance();
        c.add(calendar, duration);
        maxDate = c.getTimeInMillis();
    }

    public void setMinDate(int calendar, int duration) {
        Calendar c = Calendar.getInstance();
        c.add(calendar, duration);
        minDate = c.getTimeInMillis();
    }
}
