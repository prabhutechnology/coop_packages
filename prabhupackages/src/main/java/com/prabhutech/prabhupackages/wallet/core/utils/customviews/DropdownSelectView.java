package com.prabhutech.prabhupackages.wallet.core.utils.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.prabhutech.prabhupackages.R;

import java.util.List;

public class DropdownSelectView extends FrameLayout {
    private List<NameValue> listData;
    private String[] arrayData;

    private final Context context;

    private TextInputLayout textInputLayout;
    private AutoCompleteTextView inputView;

    private String hint, errorText;
    private boolean isRequired;

    private int selectedItemPosition = -1;

    public ItemSelectionListener itemSelectionListener;
    public OnClickListener onClickListener;
    public OnFocusChangeListener onFocusChangeListener;

    public DropdownSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FormInputView, 0, 0);
        hint = a.getString(R.styleable.FormInputView_hint);
        errorText = a.getString(R.styleable.FormInputView_errorText);
        isRequired = a.getBoolean(R.styleable.FormInputView_required, false);
        int underlineColor = a.getColor(R.styleable.FormInputView_underlineColor, Color.argb(255, 121, 121, 121));


        a.recycle();

        textInputLayout = new TextInputLayout(new ContextThemeWrapper(context, R.style.InputFieldLayout));
        inputView = new AutoCompleteTextView(new ContextThemeWrapper(context, R.style.InputFieldLayout));

//        textInputLayout.setErrorEnabled(true);
        textInputLayout.setHintTextAppearance(R.style.InputFieldLayout);
        textInputLayout.setErrorTextAppearance(R.style.InputFieldError);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            inputView.setBackgroundTintList(ColorStateList.valueOf(underlineColor));
        }

        inputView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_caret, 0);
        inputView.setFocusable(false);
        inputView.setInputType(InputType.TYPE_NULL);

        textInputLayout.addView(inputView);
        addView(textInputLayout);

        textInputLayout.setErrorEnabled(true);
        setupHint(hint);

        ViewCompat.setAccessibilityDelegate(inputView, new
                AccessibilityDelegateCompat() {
                    @Override
                    public void onInitializeAccessibilityNodeInfo(View host,
                                                                  AccessibilityNodeInfoCompat info) {
                        super.onInitializeAccessibilityNodeInfo(host, info);
                        if (info.getText() == null || info.getText().toString().isEmpty()) {
                            info.setClassName(null);
                            info.setContentDescription(hint + getResources().getString(R.string.dropdown_CD));
                        } else {
                            info.setClassName(null);
                            info.setContentDescription(getSelectedItem().Name + getResources().getString(R.string.selected_CD));
                        }
                    }
                });
    }

    public void setErrorText(String errorText) {
        if (errorText.isEmpty()) {
            textInputLayout.setError("");
//            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setError(errorText);
//            textInputLayout.setErrorEnabled(true);
        }

    }

    public void setupHint(String hint) {
        if (isRequired)
            textInputLayout.setHint(hint);
        else
            textInputLayout.setHint(hint + " (optional)");
    }


    public void setData(List<NameValue> data) {
        this.listData = data;
        this.arrayData = null;

        String[] array = NameValue.getNamesFromList(data);
        updateDropdown(array);
    }

    public void setData(String[] data) {
        this.arrayData = data;
        this.listData = null;
        updateDropdown(data);
    }

    public void clear() {
        inputView.setText("");
        selectedItemPosition = -1;
        this.arrayData = null;
        this.listData = null;
    }

    public NameValue getSelectedItem() {

        NameValue selectedItem = new NameValue();

        if (selectedItemPosition == -1) {
            return selectedItem;
        }

        if (this.listData != null) {
            selectedItem.Name = this.listData.get(selectedItemPosition).Name;
            selectedItem.Value = this.listData.get(selectedItemPosition).Value;
        }

        if (this.arrayData != null) {
            selectedItem.Name = this.arrayData[selectedItemPosition];
            selectedItem.Value = this.arrayData[selectedItemPosition];
        }

        return selectedItem;
    }

    /*public void setSelection(int i) {
        if (listData != null && i < listData.size()) {
            selectedItemPosition = i;
            inputView.setText(inputView.getAdapter().getItem(i).toString());
        }

        if (arrayData != null && i < arrayData.length) {
            selectedItemPosition = i;
            inputView.setText(inputView.getAdapter().getItem(i).toString());
        }
    }*/

    /**
     * Set selection by the value
     * Clear any error
     * Enable error to show margin at bottom
     *
     * @param value
     */
    public void setSelection(String value) {
        if (listData != null && !listData.isEmpty()) {
            int index = NameValue.getIndexOfValue(value, listData);
            if (index == -1 || index > listData.size()) return;

            selectedItemPosition = index;
            inputView.setText(listData.get(index).Name);

            if (this.itemSelectionListener != null)
                this.itemSelectionListener.onItemSelected(listData.get(index));
        }

        if (arrayData != null && arrayData.length != 0) {
            int index = NameValue.findIndexInArray(arrayData, value);
            if (index == -1 || index > arrayData.length) return;

            selectedItemPosition = index;
            inputView.setText(arrayData[index]);

            if (this.itemSelectionListener != null)
                this.itemSelectionListener.onItemSelected(new DropdownSelectView.NameValue(arrayData[index], arrayData[index]));
        }

//        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError("");
    }

    public void setRequired(boolean required) {
        this.isRequired = required;
        setupHint(hint);
    }

    public void setEnabled(boolean enabled) {
        inputView.setEnabled(enabled);
    }

    public boolean isValid() {
        Log.d("tag", "isValid: " + getSelectedItem().Name + "");
        if (isRequired) {
            if (TextUtils.isEmpty(getSelectedItem().Name)) {
                textInputLayout.setError(hint + " " + getResources().getString(R.string.is_required));
                return false;
            } else {
                textInputLayout.setError("");
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Update data and adapter
     *
     * @param data
     */
    private void updateDropdown(String[] data) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_view_item, android.R.id.text1, data);
        inputView.setAdapter(adapter);
        inputView.setOnClickListener(v -> {

            // Shows all options
            if (!inputView.getText().toString().equals("")) {
                adapter.getFilter().filter(null);
            }
            inputView.showDropDown();
            if (this.onClickListener != null)
                this.onClickListener.onClick(v);
        });

        inputView.setOnItemClickListener((adapterView, view12, i, l) -> {
            selectedItemPosition = i;

            if (this.itemSelectionListener != null)
                this.itemSelectionListener.onItemSelected(getSelectedItem());

            // Invoke setselectio to remove error
            setSelection(getSelectedItem().Value);
        });
    }

    public void showDropDown() {
        inputView.showDropDown();
    }

    public void hideDropDown() {
        inputView.dismissDropDown();
    }

    public void setOnItemSelectedListener(ItemSelectionListener listener) {
        this.itemSelectionListener = listener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
//        this.inputView.setOnClickListener(onClickListener);
        this.onClickListener = onClickListener;
    }

    public void setOnLayoutClickListener(View.OnClickListener layoutClickListener) {
        this.inputView.setOnClickListener(layoutClickListener);
    }

    public void setonFocus(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;

    }


    /**
     * Name is the display name to be displayed on the list
     * Value is the actual data to be used
     */

    // TODO use hashmap if possible
    public static class NameValue implements Comparable<NameValue> {
        public String Name;
        public String Value;

        public NameValue() {
            Name = "";
            Value = "";
        }

        public NameValue(String name, String value) {
            Name = name;
            Value = value;
        }

        public static String[] getNamesFromList(List<NameValue> data) {
            String[] array = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                array[i] = data.get(i).Name;
            }

            return array;
        }

        /**
         * Find index if item in the list, comparison by value of the item
         *
         * @param item
         * @param list
         * @return
         */
        public static int findIndexOf(NameValue item, List<NameValue> list) {
            if (list == null) return -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Value.equals(item.Value))
                    return i;
            }
            return -1;
        }

        public static String getNameOfValue(String value, List<NameValue> list) {
            for (NameValue nv : list) {
                if (nv.Value.equals(value)) {
                    return nv.Name;
                }
            }
            return null;
        }

        public static String getValueOfName(String name, List<NameValue> list) {
            for (NameValue nv : list) {
                if (nv.Name.equals(name)) {
                    return nv.Value;
                }
            }
            return null;
        }

        public static int getIndexOfValue(String value, List<NameValue> list) {
            if (list == null) return -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Value.equals(value))
                    return i;
            }
            return -1;
        }

        private static int findIndexInArray(String[] a, String s) {
            for (int i = 0; i < a.length; i++) {
                if (a[i].equals(s)) return i;
            }
            return -1;
        }

        @Override
        public int compareTo(NameValue nv) {
            if (Name == null || nv.Name == null) {
                return 0;
            }
            return Name.compareTo(nv.Name);
        }
    }

    public interface ItemSelectionListener {
        void onItemSelected(NameValue item);
    }
}
