package com.prabhutech.prabhupackages.wallet.utils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.utils.KeyboardUtils;
import com.prabhutech.prabhupackages.wallet.utils.interfaces.BiometricCallback;
import com.prabhutech.prabhupackages.wallet.utils.interfaces.DismissCallback;

public class BiometricDialog extends BottomSheetDialogFragment implements View.OnClickListener, DialogInterface.OnDismissListener {
    private static final String TAG = "BiometricDialog";

    private Button btnCancelBiometric, btnCancelPin, btnSubmitPin, btnChangeToPin;
    private Button confirmationYes, confirmationNo;
    private com.prabhutech.prabhupackages.wallet.utils.PinInput pinInput;

    TextView confirmationDescription, pinDescription, fingerprintDescription;
    TextView fingerprintTitle;

    private ConstraintLayout layoutBiometric, layoutPinInput, layoutConfirmation;

    private BiometricCallback biometricCallback;
    private DismissCallback dismissCallback;

    private String title, subTitle, description;

    public BiometricDialog() {
    }

    @SuppressLint("ValidFragment")
    public BiometricDialog(BiometricCallback biometricCallback, DismissCallback dismissCallback) {
        this.biometricCallback = biometricCallback;
        this.dismissCallback = dismissCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_biometric, null);
        layoutBiometric = view.findViewById(R.id.biometric_fingerprintInput_layout);
        layoutPinInput = view.findViewById(R.id.biometric_pinInput_layout);
        layoutConfirmation = view.findViewById(R.id.confirmation_dialog);
        fingerprintTitle = view.findViewById(R.id.title);
        pinDescription = view.findViewById(R.id.description_pin);
        fingerprintDescription = view.findViewById(R.id.description);
        pinInput = view.findViewById(R.id.pinInput_field);

        btnCancelBiometric = view.findViewById(R.id.biometric_cancel);
        btnSubmitPin = view.findViewById(R.id.pinInput_submit);
        btnCancelPin = view.findViewById(R.id.pinInput_cancel);
        btnChangeToPin = view.findViewById(R.id.biometric_switchToPin);

        confirmationDescription = view.findViewById(R.id.confirm_description);

        confirmationYes = view.findViewById(R.id.confirmation_confirm);
        confirmationNo = view.findViewById(R.id.confirmation_cancel);

        btnCancelBiometric.setOnClickListener(this);
        btnSubmitPin.setOnClickListener(this);
        btnCancelPin.setOnClickListener(this);
        btnChangeToPin.setOnClickListener(this);

        Bundle intent = getArguments();
        assert intent != null;
        title = intent.getString("title");
        description = intent.getString("description");
        boolean pinEnabled = intent.getBoolean("pinEnabled");
        boolean fingerprintEnabled = intent.getBoolean("fingerprintEnabled");

        updateDescription(description);
        updateTitle(title);

        layoutPinInput.setVisibility(View.GONE);
        layoutConfirmation.setVisibility(View.GONE);

        if (!pinEnabled && !fingerprintEnabled) {
            switchToDirectConfirmationMode();

        } else {
            if (!pinEnabled) {
                Log.d(TAG, "Pin disabled");
                btnChangeToPin.setVisibility(View.GONE);
            } else if (!fingerprintEnabled) {
                // if fingerprint is disabled show mpin even if pin is disabled
                switchToMpinMode();
                Log.d(TAG, "Fingerprint disabled");
            }
        }

        confirmationYes.setOnClickListener(v -> biometricCallback.onSuccess(""));
        confirmationNo.setOnClickListener(v -> {
            biometricCallback.onFailure("");
            dismiss();
        });

        // listen for done action
        pinInput.setOnSubmitListener(() -> submitMpin());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new KeyboardUtils(getActivity(), view);
    }

    private void updateTitle(String title) {
        if (!TextUtils.isEmpty(title))
            fingerprintTitle.setText(title);
    }

    private void updateDescription(String description) {
        if (TextUtils.isEmpty(description)) {
            confirmationDescription.setVisibility(View.GONE);
            fingerprintDescription.setVisibility(View.GONE);
            pinDescription.setVisibility(View.GONE);
        } else {
            confirmationDescription.setText(description);
            fingerprintDescription.setText(description);
            pinDescription.setText(description);
        }
    }

    private void switchToDirectConfirmationMode() {
        layoutBiometric.setVisibility(View.GONE);
        layoutPinInput.setVisibility(View.GONE);
        layoutConfirmation.setVisibility(View.VISIBLE);
    }

    private void switchToMpinMode() {
        Log.d(TAG, "Switched to pin mode");
        layoutBiometric.setVisibility(View.GONE);
        layoutPinInput.setVisibility(View.VISIBLE);
        pinInput._requestFocus();
        KeyboardUtils.showKeyboard(getActivity());
    }

    private void submitMpin() {
        if (!pinInput.isValid()) {
//                    Toast.makeText(getContext(), "Please enter valid PIN", Toast.LENGTH_SHORT).show();
            return;
        }
        String mpin = pinInput.getText();
        UserCore.pinNumber = mpin;
        biometricCallback.onSubmitMpin(mpin);
        dismissDialog();
    }

    private void dismissDialog() {
        dismiss();
        biometricCallback.onDismiss();
        dismissCallback.onDismiss();
        KeyboardUtils.hideKeyboard2(getActivity());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.biometric_cancel || id == R.id.pinInput_cancel) {
            dismissDialog();
        } else if (id == R.id.biometric_switchToPin) {
            switchToMpinMode();
        } else if (id == R.id.pinInput_submit) {
            submitMpin();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        pinInput._clearFocus();
        biometricCallback.onDismiss();
        dismissCallback.onDismiss();
        KeyboardUtils.hideKeyboard2(getActivity());
    }
}
