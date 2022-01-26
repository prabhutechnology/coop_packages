package com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.UrlsContracts;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.utils.ExceptionHandler;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;
import com.prabhutech.prabhupackages.wallet.views.AnimButton;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentRegisterBinding;

import java.util.Objects;

import io.reactivex.observers.DisposableCompletableObserver;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private FragmentRegisterBinding binding;

    private final NavigationRedirection navigationRedirection;

    public RegisterFragment() {
        navigationRedirection = new NavigationRedirection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.btnRegister.setOnClickListener(v -> registerUser(
                v,
                Objects.requireNonNull(binding.textInputEditTextMobileNumber.getText()).toString().trim(),
                binding.btnRegister
        ));

        return binding.getRoot();
    }

    private void registerUser(View view, String mobileNumber, AnimButton btnRegister) {

        if (mobileNumber.isEmpty()) {
            binding.textInputLayoutMobileNumber.setError("Mobile number is compulsory");
            return;
        } else if (mobileNumber.length() < 10) {
            binding.textInputLayoutMobileNumber.setError("Invalid mobile number");
            return;
        } else {
            binding.textInputLayoutMobileNumber.setError(null);
        }

        btnRegister.setBusy(true);

        JsonObject req = new JsonObject();
        req.addProperty("MobileNo", mobileNumber);
        UserRepo.register(requireContext(), req).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Toast.makeText(requireActivity(), "Registration Complete", Toast.LENGTH_SHORT).show();
                btnRegister.setBusy(false);

                RegisterFragmentDirections.ActionRegisterToVerifyOTP actionRegisterToSendOTP =
                        RegisterFragmentDirections.actionRegisterToVerifyOTP(UrlsContracts.OTP_TYPE_REGISTRATION, mobileNumber);
                navigationRedirection.navigateToFragmentWithAction(view, actionRegisterToSendOTP);

                binding.textInputEditTextMobileNumber.setText(null);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                ExceptionHandler.handleException(requireActivity(), e, action -> {
                    if (action == ExceptionHandler.RETRY)
                        registerUser(view, mobileNumber, btnRegister);
                    else {
                        if (e instanceof RuntimeException) { // special exceptions
                            if (e.getMessage() != null && e.getMessage().equals("registered")) {
                                Toast.makeText(requireContext(), R.string.err_user_is_already_registered, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            binding.textInputLayoutMobileNumber.setError(e.getMessage());
                            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
                btnRegister.setBusy(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}