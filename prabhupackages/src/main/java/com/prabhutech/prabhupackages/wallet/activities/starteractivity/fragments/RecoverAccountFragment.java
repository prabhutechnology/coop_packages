package com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.UrlsContracts;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.core.utils.validators.Validators;
import com.prabhutech.prabhupackages.wallet.utils.ExceptionHandler;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;
import com.prabhutech.prabhupackages.wallet.views.AnimButton;
import com.prabhutech.prabhupackages.databinding.FragmentRecoverAccountBinding;
import com.prabhutech.prabhupackages.wallet.core.UserCore;

import java.util.Objects;

import io.reactivex.observers.DisposableObserver;

public class RecoverAccountFragment extends Fragment {
    private FragmentRecoverAccountBinding binding;

    private final NavigationRedirection navigationRedirection;

    public RecoverAccountFragment() {
        navigationRedirection = new NavigationRedirection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecoverAccountBinding.inflate(inflater, container, false);

        binding.btnRecover.setOnClickListener(v -> recoverAccount(
                v,
                Objects.requireNonNull(binding.textInputEditTextRecoverMobileEmail.getText()).toString().trim(),
                binding.btnRecover
        ));

        return binding.getRoot();
    }

    private void recoverAccount(View v, String mobileNumber, AnimButton btnRecover) {
        if (!Validators.isValidPhone(mobileNumber)) {
            binding.textInputLayoutRecoverMobileEmail.setError("Invalid mobile number");
            return;
        } else
            binding.textInputLayoutRecoverMobileEmail.setError(null);

        btnRecover.setBusy(true);

        JsonObject requestBodyRecoverAccount = new JsonObject();
        requestBodyRecoverAccount.addProperty("MobileNo", mobileNumber);
        requestBodyRecoverAccount.addProperty("username", UserCore.token_username);
        UserRepo.recoverPasswordSendOTP(requireContext(), requestBodyRecoverAccount).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                btnRecover.setBusy(false);
                RecoverAccountFragmentDirections.ActionRecoverToVerifyOTP actionRecoverToVerifyOTP =
                        RecoverAccountFragmentDirections.actionRecoverToVerifyOTP(UrlsContracts.OTP_TYPE_FORGOT_PASSWORD, mobileNumber);
                navigationRedirection.navigateToFragmentWithAction(v, actionRecoverToVerifyOTP);

                binding.textInputEditTextRecoverMobileEmail.setText(null);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                btnRecover.setBusy(false);
                ExceptionHandler.handleException(requireActivity(), e, action -> {
                    if (action == ExceptionHandler.RETRY) {
                        recoverAccount(v, mobileNumber, btnRecover);
                    } else {
                        new AlertDialog.Builder(requireActivity()).setTitle("Something went wrong!")
                                .setMessage(e.getMessage())
                                .setNeutralButton("Try Again", (dialog, which) -> {
                                }).show();
                                /*
                                new AlertDialog.Builder(activity).setTitle("Activation Code Already Sent")
                                        .setMessage(e.getMessage())
                                        .setPositiveButton("I have activation code", (dialog, which) -> {
                                            activity.mProgressHelper.free(PR_LOGIN);
                                            recoverAccount.setBusy(false);
                                            //animation secttion
                                            recoveryLayout.startAnimation(slide_left_out);
                                            slide_left_out.setAnimationListener(new Animation.AnimationListener() {
                                                @Override
                                                public void onAnimationStart(Animation animation) {
                                                }

                                                @Override
                                                public void onAnimationEnd(Animation animation) {
                                                    recoveryLayout.setVisibility(View.GONE);
                                                    verifyMobileLayout.startAnimation(slide_right_in);
                                                    slide_right_in.setAnimationListener(new Animation.AnimationListener() {
                                                        @Override
                                                        public void onAnimationStart(Animation animation) {
                                                            verifyMobileLayout.setVisibility(View.VISIBLE);
                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animation animation) {
                                                        }

                                                        @Override
                                                        public void onAnimationRepeat(Animation animation) {
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animation animation) {
                                                }
                                            });
                                        }).setNeutralButton("Cancel", (dialog, which) -> {
                                }).show();
                                */
                    }
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