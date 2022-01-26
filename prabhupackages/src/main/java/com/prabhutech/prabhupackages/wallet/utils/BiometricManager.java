package com.prabhutech.prabhupackages.wallet.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;
import androidx.fragment.app.FragmentActivity;


import com.prabhutech.prabhupackages.wallet.utils.interfaces.BiometricCallback;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.ProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

@TargetApi(Build.VERSION_CODES.M)
public class BiometricManager {
    private static final String TAG = "BiometricManager";
    private static final String KEY_NAME = UUID.randomUUID().toString();

    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManagerCompat.CryptoObject cryptoObject;

    public Context context;

    protected String title;
    protected String description;
    protected boolean pinEnabled, fingerprintEnabled, dialogEnabled;

    protected CancellationSignal mCancellationSignal = new CancellationSignal();

    private BiometricDialog biometricDialog;

    private int MAX_ALLOWED_ATTEMPT = 3;
    private int attemptCount ;

    public BiometricManager(final BiometricBuilder biometricBuilder) {
        this.context = biometricBuilder.context;
        this.title = biometricBuilder.title;
        this.description = biometricBuilder.description;
        this.pinEnabled = biometricBuilder.pinEnabled;
        this.fingerprintEnabled = biometricBuilder.fingerprintEnabled;
        this.dialogEnabled = biometricBuilder.dialogEnabled;
        this.attemptCount = -1;

    }

    @SuppressLint("MissingPermission")
    public void displayBiometricPrompt(final BiometricCallback biometricCallback) {
        generateKey();

        if (!fingerprintEnabled) {
            // Disable fingerprint and show dialog for mpin only
            displayBiometricDialog(biometricCallback);
            return;
        }

        if (initCipher()) {

            cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
            FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(context);

            fingerprintManagerCompat.authenticate(cryptoObject, 0, mCancellationSignal,
                    new FingerprintManagerCompat.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errMsgId, CharSequence errString) {
                            super.onAuthenticationError(errMsgId, errString);

                            // skip the message "User cancelled" as an error
                            if (errMsgId != BiometricPrompt.BIOMETRIC_ERROR_USER_CANCELED && errMsgId
                                    != BiometricPrompt.BIOMETRIC_ACQUIRED_TOO_FAST) {
                                biometricCallback.onFailure(String.valueOf(errString));
                            }
//                            cancelAuthentication();
                            Log.d(TAG, "Error: " + errMsgId + " " + errString.toString());
                        }

                        @Override
                        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                            super.onAuthenticationHelp(helpMsgId, helpString);
                            biometricCallback.onFailure(String.valueOf(helpString));
//                            cancelAuthentication();
                            Log.d(TAG, "Help: " + helpString.toString());
                        }

                        @Override
                        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
//                            cancelAuthentication();
                            Log.d(TAG, "Authentication successful");
                            if (fingerprintEnabled) {
                                biometricCallback.onSuccess("Authentication successful");
                            } else {
                                biometricCallback.onFailure("Biometric disabled");
                            }

                            // If dialog is not enabled
                            if (biometricDialog != null) biometricDialog.dismiss();
                        }


                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            Log.d(TAG, "Error: Invalid fingerprint");
//                            cancelAuthentication();

                            // show only invalid fingerprint error on first invalid attempt
                            if(attemptCount == -1) {
                                biometricCallback.onFailure("Invalid fingerprint");
                                attemptCount = 0;
                                return;
                            }

                            if (attemptCount++ > MAX_ALLOWED_ATTEMPT) {
                                biometricCallback.onFailure("Too many attempts. Try again later.");
                            } else {
                                biometricCallback.onFailure(String.format("Invalid fingerprint. %d attempts remaining.", MAX_ALLOWED_ATTEMPT - attemptCount +1));
                            }
                        }
                    }, null);

            if (dialogEnabled) {
                Log.d(TAG, "Show biometric dialog");
                displayBiometricDialog(biometricCallback);
            }
        }
    }

    public void authenticate(@NonNull BiometricCallback biometricCallback) {

        // If fingerprint is being request but if the device doesn't support it , disable fingerprint and show pin directly
        if (this.fingerprintEnabled && !com.prabhutech.prabhupackages.wallet.utils.BiometricUtils.shouldUseBiometric(context)) {
            this.fingerprintEnabled = false;
            this.pinEnabled = true;
        }

        // If both pin and fingerprint is set to false, show confirmation dialog
        displayBiometricPrompt(biometricCallback);
    }

    public void cancelAuthentication() {
        Log.d(TAG, "Authentication cancellation signal");

        if (!mCancellationSignal.isCanceled())
            mCancellationSignal.cancel();
    }

    public void dismiss() {
        biometricDialog.dismiss();
        cancelAuthentication();
    }

    private void displayBiometricDialog(final BiometricCallback biometricCallback) {


        // If pin has not been setup, redirect to set pin mode
        /*if (TextUtils.isEmpty(UserPrefs.getMpin(context))) {
            new AlertDialog.Builder(context)
                    .setMessage("MPIN has not been setup.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, OfflineFormActivity.class);
                            intent.putExtra(Intent.EXTRA_INTENT, "changeMpin");
                            context.startActivity(intent);
                        }
                    }).show();
            return;
        }*/

        biometricDialog = new BiometricDialog(biometricCallback, ()->{
            cancelAuthentication();
        });

        FragmentActivity activity = (FragmentActivity) context;

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putBoolean("pinEnabled", pinEnabled);
        args.putBoolean("fingerprintEnabled", fingerprintEnabled);

        biometricDialog.setArguments(args);

        biometricDialog.show(activity.getSupportFragmentManager(), "Biometric");
        // AppUtils.shakira(context);
    }

    public static class BiometricBuilder {

        private String title;
        private String description;
        private boolean pinEnabled;
        private boolean fingerprintEnabled;
        public boolean dialogEnabled;

        private Context context;

        public BiometricBuilder(Context context) {
            this.context = context;
        }

        public BiometricBuilder setTitle(@NonNull final String title) {
            this.title = title;
            return this;
        }

        public BiometricBuilder setDescription(@NonNull final String description) {
            this.description = description;
            return this;
        }

        public BiometricBuilder setPinEnabled(@NonNull final boolean pinEnabled) {
            this.pinEnabled = pinEnabled;
            return this;
        }

        public BiometricBuilder setFingerprintEnabled(@NonNull final boolean fingerprintEnabled) {
            this.fingerprintEnabled = fingerprintEnabled;
            return this;
        }

        public BiometricBuilder setDialogEnabled(final boolean dialogEnabled) {
            this.dialogEnabled = dialogEnabled;
            return this;
        }

        public BiometricManager build() {
            return new BiometricManager(this);
        }
    }

    private void generateKey() {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | ProviderException
                | IOException exc) {
            exc.printStackTrace();
        }
    }


    private boolean initCipher() {

        if (!this.fingerprintEnabled) {
            return false;
        }
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;


        } catch (KeyPermanentlyInvalidatedException e) {
            return false;

        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {

            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
