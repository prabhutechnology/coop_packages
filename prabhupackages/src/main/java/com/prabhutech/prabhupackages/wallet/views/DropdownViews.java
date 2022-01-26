package com.prabhutech.prabhupackages.wallet.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.prabhutech.prabhupackages.R;

import java.util.List;

public class DropdownViews extends FrameLayout {
    private List<NameValue> listData;
    private String[] arrayData;
    private Context context;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView inputView;
    private String hint, errorText;
    private boolean isRequired;
    private NameValue selectedItem = new NameValue();
    private int selectedItemPosition;
    public ItemSelectionListener itemSelectionListener;

    public DropdownViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FormInputView, 0, 0);
        hint = a.getString(R.styleable.FormInputView_hint);
        errorText = a.getString(R.styleable.FormInputView_errorText);
        isRequired = a.getBoolean(R.styleable.FormInputView_required, false);
//        int underlineColor = a.getColor(R.styleable.FormInputView_underlineColor, Color.argb(255, 121,121, 121));
        a.recycle();

        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vs = inflater.inflate(R.layout.style_auto_complete,this,true);

     /*   textInputLayout = new TextInputLayout(new ContextThemeWrapper(context, R.style.InputFieldLayout3));
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setHintTextAppearance(R.style.InputFieldLayout3);
        textInputLayout.setErrorTextAppearance(R.style.InputFieldError);

        inputView = new AutoCompleteTextView(new ContextThemeWrapper(context, R.style.InputFieldLayout4));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            inputView.setBackgroundTintList(ColorStateList.valueOf(underlineColor));
        }
        inputView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_caret, 0);
        inputView.setFocusable(false);
        inputView.setInputType(InputType.TYPE_NULL);
        textInputLayout.addView(inputView);
        addView(textInputLayout);*/
//        addView(inputView);

        textInputLayout  = vs.findViewById(R.id.textInputLayoutCustom);
        inputView = vs.findViewById(R.id.autoCompleteTextCustom);
        inputView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_caret, 0);

        inputView.setInputType(InputType.TYPE_NULL);
        setupHint(hint);
        ViewCompat.setAccessibilityDelegate(inputView, new
                AccessibilityDelegateCompat() {
                    @Override
                    public void onInitializeAccessibilityNodeInfo(View host,
                                                                  AccessibilityNodeInfoCompat info) {
                        super.onInitializeAccessibilityNodeInfo(host, info);
                        if (info.getText() == null || info.getText().toString().isEmpty()) {
                            info.setClassName(null);
                            info.setContentDescription(getResources().getString(R.string.select_CD) + textInputLayout.getHint() + getResources().getString(R.string.dropdown_CD));
                        } else {
                            info.setClassName(null);
                            info.setContentDescription(getSelectedItem().Name + getResources().getString(R.string.selected_CD));
                        }
                    }
                });
    }

    public DropdownViews(Context context) {
        super(context);
    }


    public DropdownViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setErrorText(String errorText) {
        textInputLayout.setError(errorText);
    }

    public void clearError() {
        textInputLayout.setError("");
    }

    //added to get if the text is empty in dropdown view [water drop down validation]
    public boolean isEmpty() {
        if (inputView.getText().toString().isEmpty()) {
            return true;
        } else return false;
    }

    public void setupHint(String hint) {
        if (isRequired)
            textInputLayout.setHint(hint + " *");
        else
            textInputLayout.setHint(hint);
    }

    public String getHint() {
        return hint;
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

    public List<NameValue> getDataList() {
        return this.listData;
    }

    public String[] getDataArr() {
        return this.arrayData;
    }

    public NameValue getSelectedItem() {
        if (this.listData != null) {
            selectedItem.Name = this.listData.get(selectedItemPosition).Name;
            selectedItem.Id = this.listData.get(selectedItemPosition).Id;
            selectedItem.Value = this.listData.get(selectedItemPosition).Value;
            selectedItem.Description = this.listData.get(selectedItemPosition).Description;
        } else if (this.arrayData != null) {
            selectedItem.Name = this.arrayData[selectedItemPosition];
            selectedItem.Value = this.arrayData[selectedItemPosition];
        }
        return selectedItem;
    }

    public void setSelection(String value) {
        if (listData != null && !listData.isEmpty()) {
            int index = NameValue.getIndexOfValue(value, listData);
            if (index == -1 || index > listData.size()) return;
            selectedItemPosition = index;
            inputView.setText(listData.get(index).Name);
        }
        if (arrayData != null && arrayData.length != 0) {
            int index = NameValue.findIndexInArray(arrayData, value);
            if (index == -1 || index > arrayData.length) return;
            selectedItemPosition = index;
            inputView.setText(arrayData[index]);
        }
    }

    public void setSelectionById(String id) {
        if (listData != null && !listData.isEmpty()) {
            int index = NameValue.getIndexOfValue(id, listData);
            if (index == -1 || index > listData.size()) return;
            selectedItemPosition = index;
            inputView.setText(listData.get(index).Name);
        }
        if (arrayData != null && arrayData.length != 0) {
            int index = NameValue.findIndexInArray(arrayData, id);
            if (index == -1 || index > arrayData.length) return;
            selectedItemPosition = index;
            inputView.setText(arrayData[index]);
        }
    }

    public void setRechargeSelection(String value) {
        if (listData != null && !listData.isEmpty()) {
            int index = NameValue.getIndexOfRecharge(value, listData);
            if (index == -1 || index > listData.size()) return;
            selectedItemPosition = index;
            inputView.setText(listData.get(index).Name);
        }
        if (arrayData != null && arrayData.length != 0) {
            int index = NameValue.findIndexInArray(arrayData, value);
            if (index == -1 || index > arrayData.length) return;
            selectedItemPosition = index;
            inputView.setText(arrayData[index]);
        }
    }

    public void setRequired(boolean required) {
        this.isRequired = required;
        setupHint(hint);
    }

    public void clearText() {
        inputView.setText("");
    }

    public void setEnabled(boolean enabled) {
        inputView.setEnabled(enabled);
    }

    public boolean isValid() {
        if (isRequired) {
            if (selectedItem.Name == null) {
                textInputLayout.setError(hint + " is required");
                return false;
            } else {
                textInputLayout.setError("");
                return true;
            }
        } else {
            return true;
        }
    }

    private void updateDropdown(String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_view_item, android.R.id.text1, data);
        inputView.setAdapter(adapter);
        inputView.setOnClickListener(v -> {
            // Shows all options
            if (!inputView.getText().toString().equals("")) {
                adapter.getFilter().filter(null);
            }
            inputView.showDropDown();
        });
        inputView.setOnItemClickListener((adapterView, view12, i, l) -> {
            selectedItemPosition = i;
            if (this.itemSelectionListener != null)
                this.itemSelectionListener.onItemSelected(getSelectedItem());
        });
    }

    public void setOnItemSelectedListener(ItemSelectionListener listener) {
        this.itemSelectionListener = listener;
    }

    public static class NameValue {
        public String Name;
        public String Value;
        public String Id;
        public String Description;

        public NameValue() {
            Name = "";
            Value = "";
        }

        public NameValue(String name, String value) {
            Name = name;
            Value = value;
        }

        public NameValue(String name, String id, String value) {
            Id = id;
            Name = name;
            Value = value;
        }

        public NameValue(String name, String id, String value, String description) {
            Id = id;
            Name = name;
            Value = value;
            Description = description;
        }

        public static String[] getNamesFromList(List<NameValue> data) {
            String[] array = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                array[i] = data.get(i).Name;
            }
            return array;
        }

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

        public static int getIndexOfId(String id, List<NameValue> list) {
            if (list == null) return -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Id.equals(id))
                    return i;
            }
            return -1;
        }


        public static int getIndexOfValue(String value, List<NameValue> list) {
            if (list == null) return -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Value.equals(value))
                    return i;
            }
            return -1;
        }

        public static int getIndexOfRecharge(String value, List<NameValue> list) {
            if (list == null) return -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Name.equals(value))
                    return i;
            }
            return -1;
        }

        private static int findIndexInArray(String[] a, String s) {
            for (int i = 0; i < a.length; i++) {
                if (a[i].equals(s)) return i;
            }
            return 0;
        }
    }

    public interface ItemSelectionListener {
        void onItemSelected(NameValue item);
    }
}
