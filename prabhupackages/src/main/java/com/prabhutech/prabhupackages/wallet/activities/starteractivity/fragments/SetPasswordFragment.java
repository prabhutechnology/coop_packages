package com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;
import com.prabhutech.prabhupackages.wallet.views.AnimButton;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentSetPasswordBinding;

import java.util.Objects;

import io.reactivex.observers.DisposableCompletableObserver;

public class SetPasswordFragment extends Fragment {
    private static final String TAG = "SetPasswordFragment";

    private FragmentSetPasswordBinding binding;

    private final NavigationRedirection navigationRedirection;

    public SetPasswordFragment() {
        navigationRedirection = new NavigationRedirection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSetPasswordBinding.inflate(inflater, container, false);

        String mobileNumber = SetPasswordFragmentArgs.fromBundle(getArguments()).getMobileNumber();

        binding.btnVerifyPassword.setOnClickListener(v ->
                setPassword(
                        v,
                        Objects.requireNonNull(binding.textInputEditTextNewPassword.getText()).toString().trim(),
                        Objects.requireNonNull(binding.textInputEditTextConfirmNewPassword.getText()).toString().trim(),
                        mobileNumber,
                        binding.btnVerifyPassword
                ));

        return binding.getRoot();
    }

    private void setPassword(View v, String newPassword, String confirmPassword, String mobileNumber, AnimButton btnVerifyPassword) {
        if (newPassword.isEmpty()) {
            binding.textInputLayoutNewPassword.setError("This cannot be empty");
            return;
        }
        if (confirmPassword.isEmpty()) {
            binding.textInputLayoutConfirmNewPassword.setError("This cannot be empty");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            binding.textInputLayoutNewPassword.setError("Password Mismatch");
            binding.textInputLayoutConfirmNewPassword.setError("Password Mismatch");
            return;
        }

        if (newPassword.length() <= 3) {
            binding.textInputLayoutNewPassword.setError("Must be at least 4 characters");
            return;
        }

        if (confirmPassword.length() <= 3) {
            binding.textInputLayoutConfirmNewPassword.setError("Must be at least 4 characters");
            return;
        }

        btnVerifyPassword.setBusy(true);

        JsonObject requestBodySetPassword = new JsonObject();
        requestBodySetPassword.addProperty("Username", mobileNumber);
        requestBodySetPassword.addProperty("Password", newPassword);
        requestBodySetPassword.addProperty("ConfirmPassword", confirmPassword);

        UserRepo.setPassword(requireContext(), requestBodySetPassword).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                btnVerifyPassword.setBusy(false);
                navigationRedirection.navigateToFragmentWithAction(v, R.id.action_setPassword_to_getStarted);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                btnVerifyPassword.setBusy(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}