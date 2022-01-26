package com.prabhutech.prabhupackages.wallet.utils;

import android.accounts.AuthenticatorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.models.UserLogin;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.core.utils.interfaces.BasicResponseCallback;
import com.prabhutech.prabhupackages.wallet.utils.interfaces.BiometricCallback;
import com.prabhutech.prabhupackages.wallet.views.QuickUI;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;

public class PaymentWall {
    private Context context;
    private boolean fingerprintEnabled, pinEnabled, checkBalance = true;
    private String description;
    private String transactionAmount;
    private boolean isMpinEnabled = false;

    private Callback callback;

    public PaymentWall() {
    }

    public PaymentWall(Builder builder) {
        this.context = builder.context;
        this.description = builder.description;
        this.transactionAmount = builder.amount;
        this.checkBalance = builder.checkBalance;

        initPaymentWall();
        if (UserCore.isMpinSet && UserCore.isMPINTrxnEnable)
            showBiometricPrompt();
        else if (!UserCore.isMpinSet && !UserCore.isMPINTrxnEnable)
            showBiometricPrompt();

        isMpinEnabled = UserCore.isMpinSet;
        // if (this.cancelListener != null) this.cancelListener.call();
    }

    public void checkForSufficientBalance(Context context, String transactionAmount, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.transactionAmount = transactionAmount;
        checkBalanceAndContinue();
    }

    private void showBiometricPrompt() {
        BiometricManager biometricManager = new BiometricManager.BiometricBuilder(context)
                .setFingerprintEnabled(fingerprintEnabled)
                .setPinEnabled(pinEnabled)
                .setDescription(description)
                .setDialogEnabled(true)
                .build();

        biometricManager.authenticate(new BiometricCallback() {
            @Override
            public void onSuccess(String message) {
                checkBalanceAndContinue();
                biometricManager.dismiss();
            }

            @Override
            public void onFailure(String message) {
                QuickUI.showToast(context, message);
                callback.onComplete();
            }

            @Override
            public void onSubmitMpin(String pin) {
                checkBalanceAndContinue();
/*
                validatePin(pin, new BasicResponseCallback() {
                    @Override
                    public void onSuccess() {
                        checkBalanceAndContinue();
                    }

                    @Override
                    public void onFailure() {
                        callback.onComplete();
                    }
                }, true);*/

                biometricManager.dismiss();
            }

            @Override
            public void onDismiss() {
                callback.onComplete();
            }
        });
    }

    public void validatePassword(Context context, String password, BasicResponseCallback
            pinCallback) {

        UserLogin req = new UserLogin();
        req.username = !TextUtils.isEmpty(UserCore.mobileNumber) ? UserCore.mobileNumber : UserCore.email;
        req.password = password;
        req.rememberMe = true;

        UserRepo.login(context, req)
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        pinCallback.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof TimeoutException) {
                            Toast.makeText(context, "Timeout", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof AuthenticatorException) {
                            Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }
                        pinCallback.onFailure();
                    }
                });
    }

    private void checkBalanceAndContinue() {
        callback.onSuccess();
        callback.onComplete();
        /*if (!checkBalance) {
            callback.onSuccess();
            callback.onComplete();
            return;
        }

        if (TextUtils.isEmpty(transactionAmount) || Float.parseFloat(transactionAmount) < 0) {
            callback.onComplete();
            showInvalidTransactionAmountPage();
            return;
        }

        HashMap<String, String> accountDetail = UserPref.getAccountDetail(context);
        String sharedPrefAccountNo = accountDetail.get(UserPref.ACCOUNT_NO_KEY);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AccountNo", sharedPrefAccountNo);
        UserRepo.getCBSBalance(context, jsonObject).subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String balance) {
                if (transactionAmount == null) {
                    Toast.makeText(context, "Cannot proceed this transaction. Please try again..", Toast.LENGTH_SHORT).show();
                    callback.onComplete();
                    return;
                }

                if (Float.parseFloat(transactionAmount) <= Float.parseFloat(balance)) {
                    callback.onSuccess();
                } else {
                    showInsufficientBalancePage();
                    callback.onComplete();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                if (e instanceof SocketTimeoutException) {
                    showTimeOutPage();
                } else {
                    Toast.makeText(context, "Cannot get your balance right now.", Toast.LENGTH_SHORT).show();
                }
                callback.onComplete();
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        });*/
    }

    public static void showFingerprintPinCheckPrompt(Context context, String
            message, BasicResponseCallback callback) {
        new BiometricManager.BiometricBuilder(context)
                .setFingerprintEnabled(true)
                .setPinEnabled(true)
                .setDescription(message)
                .setDialogEnabled(true)
                .build()
                .authenticate(new BiometricCallback() {
                    @Override
                    public void onSuccess(String message) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        callback.onFailure();
                    }

                    @Override
                    public void onSubmitMpin(String pin) {
                        ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Requesting");
                        progressDialog.setCancelable(false);
                        callback.onSuccess();
//                        progressDialog.show();
//                        validatePin(context, pin, new BasicResponseCallback() {
//                            @Override
//                            public void onSuccess() {
//                                callback.onSuccess();
//                                progressDialog.dismiss();
//                            }
//
//                            @Override
//                            public void onFailure() {
//                                callback.onFailure();
//                                progressDialog.dismiss();
//                            }
//                        });
                    }

                    @Override
                    public void onDismiss() {
                        callback.onFailure();
                    }
                });
    }

    public static void validatePin(Context context, String pin, BasicResponseCallback
            pinCallback) {

        UserLogin req = new UserLogin();
        req.username = !TextUtils.isEmpty(UserCore.mobileNumber) ? UserCore.mobileNumber : UserCore.email;
        req.password = pin;
        new DebugMode().showLog("PinCheck", UserCore.pinNumber);
        req.rememberMe = true;

        UserRepo.login(context, req)
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        pinCallback.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof TimeoutException) {
                            Toast.makeText(context, "Timeout", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof AuthenticatorException) {
                            Toast.makeText(context, "Invalid mPIN", Toast.LENGTH_SHORT).show();
                        }
                        pinCallback.onFailure();
                    }
                });
    }

    public void submit(Callback callback) {
        this.callback = callback;
    }

    private void showInvalidTransactionAmountPage() {
        /*Intent intent = new Intent(context, TransactionFinalActivity.class);
        intent.putExtra(TransactionFinalActivity.INTENT_ERR_TYPE, "");
        intent.putExtra(TransactionFinalActivity.INTENT_MESSAGE, "Invalid amount supplied.\nPlease validate your transaction and try again");
        context.startActivity(intent);*/
    }

    private void initPaymentWall() {
        fingerprintEnabled = UserPref.getFingerprintPinEnabledForTransaction(context);
        pinEnabled = UserCore.isMPINTrxnEnable;
    }

    private void showInsufficientBalancePage() {
        /*Intent intent = new Intent(context, TransactionFinalActivity.class);
        intent.putExtra(TransactionFinalActivity.INTENT_ERR_TYPE, Transaction.INSUFFICIENT_BALANCE);
        context.startActivity(intent);*/
    }

    private void showTimeOutPage() {
        /*Intent intent = new Intent(context, TransactionFinalActivity.class);
        intent.putExtra(TransactionFinalActivity.INTENT_ERR_TYPE, Transaction.TIMEOUT);
        context.startActivity(intent);*/
    }

    private void showAuthenticationErrorPage() {
        /*Intent intent = new Intent(context, TransactionFinalActivity.class);
        intent.putExtra(TransactionFinalActivity.INTENT_ERR_TYPE, "");
        intent.putExtra(TransactionFinalActivity.INTENT_MESSAGE, "Authentication failed.\nPlease check your mPIN and try again");
        context.startActivity(intent);*/
    }

    public static class Builder {

        private Context context;
        private String description;
        private String amount;
        private boolean checkBalance = true;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder shouldCheckBalance(boolean checkBalance) {
            this.checkBalance = checkBalance;
            return this;
        }

        public Builder setDescription(String text) {
            this.description = text;
            return this;
        }

        public Builder setAmount(String amount) {
            this.amount = amount;
            return this;
        }

        public Builder setAmount(float amount) {
            this.amount = String.valueOf(amount);
            return this;
        }

        public PaymentWall build() {
            return new PaymentWall(this);
        }
    }

    public interface Callback {
        public void onSuccess();

        public void onComplete();
    }
}
