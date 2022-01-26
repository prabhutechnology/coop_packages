package com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.core.utils.validators.Validators;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;
import com.prabhutech.prabhupackages.wallet.utils.ExceptionHandler;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;
import com.prabhutech.prabhupackages.wallet.views.AnimButton;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentVerifyOTPBinding;

import java.util.Objects;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;

public class VerifyOTPFragment extends Fragment {
    private static final String TAG = "VerifyOTPFragment";

    private FragmentVerifyOTPBinding binding;

    private final NavigationRedirection navigationRedirection;
    private final DebugMode debugMode;

    public VerifyOTPFragment() {
        navigationRedirection = new NavigationRedirection();
        debugMode = new DebugMode();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVerifyOTPBinding.inflate(inflater, container, false);

        int otpType = VerifyOTPFragmentArgs.fromBundle(getArguments()).getOTPType();
        String mobileNumber = VerifyOTPFragmentArgs.fromBundle(getArguments()).getMobileNumber();
//        Log.e(TAG, "OTP type: " + otpType + " mobile number: " + mobileNumber);
        debugMode.showLog(TAG, "OTP type: " + otpType + " mobile number: " + mobileNumber);

        binding.btnSendOtp.setOnClickListener(v -> verifyOTP(
                v,
                otpType,
                Objects.requireNonNull(binding.textInputEditTextOTPCode.getText()).toString().trim(),
                mobileNumber,
                binding.btnSendOtp
        ));

        binding.btnResendOtp.setOnClickListener(v -> {
            new CountDownTimer(300000, 1000) {
                @Override
                public void onTick(long l) {
                    String msg = "RESEND IN (" + resendTextMessage(l) + ")";
                    binding.btnResendOtp.setText(msg);
                    binding.btnResendOtp.setTextColor(getResources().getColor(R.color.grey));
                    binding.btnResendOtp.setEnabled(false);
                }

                @Override
                public void onFinish() {
                    binding.btnResendOtp.setEnabled(true);
                    binding.btnResendOtp.setTextColor(getResources().getColor(R.color.colorPrimary));
                    binding.btnResendOtp.setText(R.string.resend);
                }
            }.start();
            resendOTP(otpType, binding.btnResendOtp);
        });

        return binding.getRoot();
    }

    /**
     * @param millisUntilFinished - long
     * @return text = minutes and second
     */
    private String resendTextMessage(long millisUntilFinished) {
        String countDownMessage;

        long minutes = (millisUntilFinished / 1000) / 60;
        long seconds = (millisUntilFinished / 1000) % 60;

        if (seconds < 10) {
            countDownMessage = "" + minutes + ":0" + seconds + "";
        } else countDownMessage = "" + minutes + ":" + seconds + "";
        return countDownMessage;
    }

    private void verifyOTP(View v, int otpType, String otpCode, String mobileNumber, AnimButton btnSendOtp) {
        if (!Validators.isValidActivationCode(otpCode)) {
            binding.textInputLayoutOTPCode.setError("Invalid activation code");
            return;
        }

        JsonObject requestBodyVerifyOTP = new JsonObject();
        requestBodyVerifyOTP.addProperty("OTPCode", otpCode);
        requestBodyVerifyOTP.addProperty("OTPType", otpType);

        btnSendOtp.setBusy(true);

        UserRepo.verifyOTP(requireContext(), requestBodyVerifyOTP).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                btnSendOtp.setBusy(false);
                VerifyOTPFragmentDirections.ActionVerifyOTPToSetPassword actionVerifyOTPToSetPassword =
                        VerifyOTPFragmentDirections.actionVerifyOTPToSetPassword(mobileNumber);
                navigationRedirection.navigateToFragmentWithAction(v, actionVerifyOTPToSetPassword);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                ExceptionHandler.handleException(requireActivity(), e, action -> {
                    if (action == ExceptionHandler.RETRY) {
                        verifyOTP(v, otpType, otpCode, mobileNumber, btnSendOtp);
                    } else
                        binding.textInputLayoutOTPCode.setError(e.getMessage());
                });
                btnSendOtp.setBusy(false);
            }
        });
    }

    private void resendOTP(int otpType, Button btnResendOtp) {
        btnResendOtp.setEnabled(false);

        JsonObject requestBodyResendOTP = new JsonObject();
        requestBodyResendOTP.addProperty("OTPType", otpType);

        UserRepo.resendOTP(requireContext(), requestBodyResendOTP).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                btnResendOtp.setEnabled(true);
                String message = jsonObject.get("Message").getAsString();
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                btnResendOtp.setEnabled(true);
                ExceptionHandler.handleException(requireActivity(), e, action -> {
                    if (action == ExceptionHandler.RETRY) {
                        resendOTP(otpType, binding.btnResendOtp);
                    } else
                        new AlertDialog.Builder(requireActivity()).setTitle("Sorry!")
                                .setMessage(e.getMessage()).setPositiveButton("Ok", (dialog, which) -> {
                        }).show();
                });
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}