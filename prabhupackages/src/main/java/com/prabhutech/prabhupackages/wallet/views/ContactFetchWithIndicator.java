package com.prabhutech.prabhupackages.wallet.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.wallet.contracts.DataContracts;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;

public class ContactFetchWithIndicator extends FrameLayout {
    public ContactInterface contactInterface;
    AppCompatActivity activity;
    public Button operatorSignal;
    TextView alertView;

    public static String PhoneTYPE = "mobileNumber";
    public static String LandlineTYPE = "landline";
    public static String BOTH_TYPE = "both";

    ImageButton selfContactButton, contactFetchButton;

    private ImageButton button;
    private String title, inputType;
    private TextView contactName;
    private TextInputLayout textInputLayout, contactInputLayout;
    private TextInputEditText editText;
    String opCode;
    boolean selfFetchType = true, showContactFetchIcon = true;
    String utiltype = BOTH_TYPE;
    boolean required;
    String hint;
    Context context;
    String showOp = "true";

    EmptyTextListener textChanged;


    public ContactFetchWithIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContactFetchWithIndicator, 0, 0);
        hint = a.getString(R.styleable.ContactFetchWithIndicator_contact_hint);
        required = a.getBoolean(R.styleable.ContactFetchWithIndicator_contact_required, false);
        // utiltype =a.getString(R.styleable.ContactFetchWithIndicator_contact_inputTypes);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_contact_fetch_indicator, this, true);
        textInputLayout = view.findViewById(R.id.contact_fetch_layout);
        contactInputLayout = view.findViewById(R.id.contact_input_holder);
        contactFetchButton = view.findViewById(R.id.contact_input_button);
        editText = view.findViewById(R.id.contact_input_contact);
        selfContactButton = view.findViewById(R.id.self_contact_button);
        operatorSignal = view.findViewById(R.id.btn_service_provider);
        operatorSignal.setVisibility(GONE);
        alertView = findViewById(R.id.alertView);
        alertView.setVisibility(GONE);

        contactName = view.findViewById(R.id.contactName);
        button = view.findViewById(R.id.contact_input_button);

//        textInputLayout.setHint("Enter or select mobileNumber number");
        selfContactButton.setOnClickListener(handleSelfButton);
        textInputLayout.setHintTextAppearance(R.style.InputFieldLayout);
        textInputLayout.setErrorTextAppearance(R.style.InputFieldError);
        button.setOnClickListener(handleButtonClick);

        editText.setOnFocusChangeListener(handleFocusChange);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        utilType(BOTH_TYPE);
        contactInputLayout.setErrorEnabled(true);

        setupHint();
    }

    public void addTextWatcher(EmptyTextListener listener) {
        this.textChanged = listener;
    }

    public void showOperatorName(boolean show){
        if(show){
            showOp = "true";
            operatorSignal.setVisibility(VISIBLE);
        } else{
            showOp = "false";
            operatorSignal.setVisibility(GONE);
        }
    }

    private void setUpType() {
        if (utiltype.equals(PhoneTYPE)) {
            contactInputLayout.setHint(getResources().getString(R.string.mobile_number));
            editText.addTextChangedListener(handlePhoneTypeChange);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DataContracts.MOBILE_NO_LIMIT)});

        } else if (utiltype.equals(LandlineTYPE)) {
            contactInputLayout.setHint(getResources().getString(R.string.landline_number));

            editText.addTextChangedListener(handleLandlineTypeChange);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DataContracts.LANDLINE_NO_LIMIT)});
        } else {
            utiltype = BOTH_TYPE;
            editText.addTextChangedListener(handleNumberChange);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DataContracts.MOBILE_NO_LIMIT)});
        }
    }

    //Watchers! Watchout!
    TextWatcher handlePhoneTypeChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkPhoneOperatorForVisibility();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkPhoneOperatorForVisibility();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkPhoneOperatorForVisibility();
            if (textChanged != null) {
                if (editable.toString().isEmpty()) {
                    textChanged.onEmpty();
                } else {
                    textChanged.onValue();
                }
            }
        }
    };

    TextWatcher handleLandlineTypeChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkLandlineOperatorForVisibility();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkLandlineOperatorForVisibility();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkLandlineOperatorForVisibility();
            if (textChanged != null) {
                if (editable.toString().isEmpty()) {
                    textChanged.onEmpty();
                } else {
                    textChanged.onValue();
                }
            }
        }
    };

    private void checkLandlineOperatorForVisibility() {
        alertView.setVisibility(GONE);
        String landlineNumber = editText.getText().toString();
        if (landlineNumber.length() >= 4) {
            String landline_f3 = landlineNumber.substring(0, 3);
            String landline_f1 = landlineNumber.substring(0, 1);
            String landline_f5 = landlineNumber.substring(3, 4);
            operatorSignal.setVisibility(View.VISIBLE);

            if (landline_f3.equals("014") || landline_f3.equals("015") || landline_f3.equals("016") || landline_f3.equals("012")) {
                switch (landline_f3) {
                    case "012":
                        opCode = "18";
                        operatorSignal.setText("UTL");
                        break;
                    case "014":
                    case "015":
                    case "016":
                        opCode = "15";
                        operatorSignal.setText("NTC PSTN");
                        break;
                    default:
                        operatorSignal.setVisibility(View.GONE);
                        alertView.setVisibility(View.VISIBLE);
                        break;
                }
            } else {
                switch (landline_f5) {
                    case "4":
                    case "5":
                    case "6":
                        opCode = "15";
                        operatorSignal.setText("NTC PSTN");
                        break;
                    case "2":
                        opCode = "18";
                        operatorSignal.setText("UTL");
                        break;
                    default:
                        operatorSignal.setVisibility(View.GONE);
                        alertView.setVisibility(View.VISIBLE);
                        break;
                }
            }
        } else {
            alertView.setVisibility(View.GONE);
            operatorSignal.setVisibility(View.GONE);
        }
    }

    private void checkPhoneOperatorForVisibility() {
        alertView.setVisibility(GONE);
        String phoneNumber = editText.getText().toString();
        if (phoneNumber.length() >= 3) {
            String operatorCode = editText.getText().toString().substring(0, 3);
            if (showOp.equals("true")) {
                operatorSignal.setVisibility(View.VISIBLE);
            } else {
                operatorSignal.setVisibility(View.GONE);
            }

            switch (operatorCode) {
                case "975":
                    opCode = "10";
                    operatorSignal.setText("CDMA Postpaid");
                    break;
                case "985":
                    opCode = "14";
                    operatorSignal.setText("NT Postpaid");
                    break;
                case "980":
                case "981":
                case "982":
                    opCode = "2";
                    operatorSignal.setText("Ncell");
                    break;
                case "984":
                case "986":
                case "976":
                    opCode = "1";
                    operatorSignal.setText("NTC");
                    break;
                case "972":
                    opCode = "3";
                    operatorSignal.setText("UTL");
                    break;
                case "961":
                case "962":
                case "988":
                    opCode = "4";
                    operatorSignal.setText("Smartcell");
                    break;
                case "974":
                    opCode = "9";
                    operatorSignal.setText("CDMA");
                    break;
                default:
                    opCode = null;
                    operatorSignal.setVisibility(View.GONE);
                    alertView.setVisibility(View.VISIBLE);
                    break;

            }
        } else {
            opCode = null;
            alertView.setVisibility(View.GONE);
            operatorSignal.setVisibility(View.GONE);
        }
    }

    TextWatcher handleNumberChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String operator = getOperatorName(editable.toString());
            if (operator == null) {
                operatorSignal.setVisibility(View.GONE);
            } else {
                operatorSignal.setVisibility(View.VISIBLE);
                operatorSignal.setText(operator);
            }
            textInputLayout.setError("");
        }
    };

    private void setupHint() {
        if (hint != null) {
            if (required) {
                contactInputLayout.setHint(hint);
            } else {
                contactInputLayout.setHint(hint + " (optional)");
            }
        }
    }

    public void setError(String error) {
        contactInputLayout.setError(error);
    }

    OnFocusChangeListener handleFocusChange = (view, b) -> {
        if (!b) isValid();
    };

    public View.OnClickListener handleSelfButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editText.setText(UserPref.getUser(context, UserPref.PREF_PHONE));
        }
    };

    OnClickListener handleButtonClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (contactInterface != null)
                contactInterface.onContactClick();
        }
    };

    public void selfNumberEnabled(boolean selfFetchMode) {
        selfFetchType = selfFetchMode;
        setUpSelfFetch();
    }

    public void contactFetchEnabled(boolean contactType) {
        showContactFetchIcon = contactType;
        setUpContactFetch();
    }

    private void setUpSelfFetch() {
        if (selfFetchType) {
            selfContactButton.setVisibility(VISIBLE);
        } else {
            selfContactButton.setVisibility(GONE);
        }
    }

    private void setUpContactFetch(){
        if (showContactFetchIcon) {
            contactFetchButton.setVisibility(VISIBLE);
        } else {
            contactFetchButton.setVisibility(GONE);
        }
    }

    public void onContactButtonClick(ContactInterface contactInterface) {

        this.contactInterface = contactInterface;
    }

    public String getText() {
        return editText.getText().toString();
    }

    public String getOP() {
        return opCode;
    }

    public void setText(String phone, String name) {
        editText.setText(phone);
        contactName.setText(name);
    }

    public void utilType(String type) {
        utiltype = type;
        setUpType();
    }

    public TextInputLayout getTextInputLayout() {
        return contactInputLayout;
    }

    private boolean isValid() {
        if (isEmpty()) {
            if (required) {
                textInputLayout.setError("Required");
                return false;
            } else {
                textInputLayout.setError("");
                return true;
            }
        } else {
            if (utiltype.equals(BOTH_TYPE)) {
                if (editText.getText() != null && getOperatorName(editText.getText().toString()) != null) {
                    return true;
                } else {
                    textInputLayout.setError("Invalid Number");
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    private boolean isEmpty() {
        return TextUtils.isEmpty(editText.getText());
    }

    private String getOperatorName(String number) {

        if (number.length() < 4) return null;

        String operatorCode = number.substring(0, 3);
        if (number.length() == 10 || operatorCode.substring(0, 2).equals("01") || operatorCode.substring(0, 1).equals("9")) {
            switch (operatorCode) {
                case "985":
                    return "NT Postpaid";
                case "980":
                case "981":
                case "982":
                    return "Ncell";
                case "984":
                case "986":
                case "976":
                    return "NTC";
                case "972":
                    return "UTL";
                case "961":
                case "962":
                    return "Smartcell";
                case "988":
                case "974":
                    return "CDMA";

                case "012":
                    return "UTL";

                case "014":
                case "015":
                case "016":
                    return "NTC PSTN";

                default:
                    return null;
            }
        } else {
            switch (number.substring(3, 4)) {
                case "4":
                case "5":
                case "6":
                    return "NTC PSTN";
                case "2":
                    return "UTL";
                default:
                    return null;
            }
        }
    }

    public void showError() {
        operatorSignal.setVisibility(View.GONE);
        alertView.setVisibility(View.VISIBLE);
    }

    public void hideError() {
        alertView.setVisibility(View.GONE);
    }

    public void clearError() {
        textInputLayout.setError("");
    }

    public void clearText() {
        editText.setText("");
    }

    public interface ContactInterface {
        void onContactClick();
    }

    public interface EmptyTextListener {
        public void onEmpty();

        public void onValue();
    }
}
