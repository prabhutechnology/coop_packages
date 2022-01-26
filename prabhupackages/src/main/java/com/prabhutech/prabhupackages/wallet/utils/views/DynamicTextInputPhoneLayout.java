package com.prabhutech.prabhupackages.wallet.utils.views;

import android.content.Context;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;

public class DynamicTextInputPhoneLayout {
    private ContactInterface contactInterface;
    private final Context context;
    private final TextInputLayout textInputLayout;
    private final TextInputEditText textInputEditText;
    private final ImageButton imageButtonOwnNumber;
    private final ImageButton imageButtonShowContact;

    public DynamicTextInputPhoneLayout(Context context, TextInputLayout textInputLayout, TextInputEditText textInputEditText, ImageButton imageButtonOwnNumber, ImageButton imageButtonShowContact) {
        this.context = context;
        this.textInputLayout = textInputLayout;
        this.textInputEditText = textInputEditText;
        this.imageButtonOwnNumber = imageButtonOwnNumber;
        this.imageButtonShowContact = imageButtonShowContact;
    }

    public void setOwnPhoneNumberInEditText() {
        imageButtonOwnNumber.setOnClickListener(view -> textInputEditText.setText(UserPref.getUser(context, UserPref.PREF_PHONE)));
    }

    public void showPhoneContactList() {
        imageButtonShowContact.setOnClickListener(view -> contactInterface.onContactClick());
    }

    public void onContactButtonClick(ContactInterface contactInterface) {
        this.contactInterface = contactInterface;
    }
}
