package com.prabhutech.prabhupackages.wallet.utils.interfaces;

public interface BiometricCallback {
    void onSuccess(String message);
    void onFailure(String message);
    void onSubmitMpin(String pin);
    void onDismiss();
}
