package com.prabhutech.coop.wallet.activities.landingactivity.fragments.topup;

import static com.prabhutech.coop.wallet.constants.ValidationMessage.HIT_API;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.INVALID_AMOUNT;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.INVALID_PHONE;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.INVALID_PHONE_LENGTH;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.REQUIRED;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.VALIDATE;

import android.text.Editable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TopUpViewModel extends ViewModel {
    private static final String TAG = "TopUpViewModel";

    //Variables
    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();
    public MutableLiveData<String> amount = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberValidation = new MutableLiveData<>();
    public MutableLiveData<String> amountValidation = new MutableLiveData<>();
    public MutableLiveData<Boolean> checkEmptyField = new MutableLiveData<>();

    /*Getter setter*/
    public MutableLiveData<String> getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(MutableLiveData<String> mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public MutableLiveData<String> getAmount() {
        return amount;
    }

    public void setAmount(MutableLiveData<String> amount) {
        this.amount = amount;
    }

    public MutableLiveData<String> getPhoneNumberValidation() {
        return phoneNumberValidation;
    }

    public void setPhoneNumberValidation(MutableLiveData<String> phoneNumberValidation) {
        this.phoneNumberValidation = phoneNumberValidation;
    }

    public MutableLiveData<String> getAmountValidation() {
        return amountValidation;
    }

    public void setAmountValidation(MutableLiveData<String> amountValidation) {
        this.amountValidation = amountValidation;
    }

    public MutableLiveData<Boolean> getCheckEmptyField() {
        return checkEmptyField;
    }

    public void setCheckEmptyField(MutableLiveData<Boolean> checkEmptyField) {
        this.checkEmptyField = checkEmptyField;
    }

    /*Action button*/
    public void onTopUpClicked() {
        if (mobileNumber.getValue() == null && amount.getValue() == null) {
            checkEmptyField.setValue(false);
        } else if (phoneNumberValidation.getValue() != null && amountValidation.getValue() != null) {
            if (phoneNumberValidation.getValue().equals(HIT_API) && amountValidation.getValue().equals(VALIDATE)) {
                checkEmptyField.setValue(true);
            } else
                checkEmptyField.setValue(false);
        } else
            checkEmptyField.setValue(false);
    }

    /*Text Watcher*/
    public void onPhoneNumberAfterTextChanged(Editable editable) {
        String phoneNumber = editable.toString();
        checkPhoneNumberValidation(phoneNumber);
    }

    public void onAmountAfterTextChanged(Editable editable) {
        String amountValue = editable.toString();
        checkAmountValidation(amountValue);
    }

    /*Validation*/
    private void checkAmountValidation(String amount) {
        if (amount.length() == 0) {
            amountValidation.setValue(REQUIRED);
        } else if (amount.length() > 7) {
            amountValidation.setValue(INVALID_AMOUNT);
        } else if (Float.parseFloat(amount) >= 10.00 && Float.parseFloat(amount) <= 5000.00) {
            amountValidation.setValue(VALIDATE);
        } else {
            amountValidation.setValue(INVALID_AMOUNT);
        }
    }

    public void checkPhoneNumberValidation(String phoneNumber) {
        if (phoneNumber.length() == 0) {
            phoneNumberValidation.setValue(REQUIRED);
        } else if (phoneNumber.length() >= 3) {
            switch (phoneNumber.substring(0, 3)) {
                case "985":
                case "984":
                case "986":
                case "980":
                case "981":
                case "982":
                case "972":
                case "961":
                case "962":
                case "988":
                case "974":
                case "975":
                case "976":
                    phoneNumberValidation.setValue(VALIDATE);
                    if (phoneNumber.length() == 10) {
                        phoneNumberValidation.setValue(HIT_API);
                    } else {
                        phoneNumberValidation.setValue(INVALID_PHONE_LENGTH);
                    }
                    break;
                default:
                    phoneNumberValidation.setValue(INVALID_PHONE);
                    break;
            }
        }
    }
}
