package com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.models.UserLogin;
import com.prabhutech.prabhupackages.wallet.views.AppInfoClass;
import com.prabhutech.prabhupackages.wallet.views.QuickUI;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentLoginBinding;
import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;
import com.prabhutech.prabhupackages.wallet.utils.ExceptionHandler;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;
import com.prabhutech.prabhupackages.wallet.views.AnimButton;

import java.util.Objects;

import io.reactivex.observers.DisposableCompletableObserver;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private FragmentLoginBinding binding;
    int radioButtonId = 0;

    private final NavigationRedirection navigationRedirection;
    private final DebugMode debugMode;

    public LoginFragment() {
        navigationRedirection = new NavigationRedirection();
        debugMode = new DebugMode();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.radioGroupLoginType.check(binding.radioBtnPrabhuCoop.getId());
        binding.radioGroupLoginType.setOnCheckedChangeListener(((radioGroup, i) -> {
            if (i == binding.radioBtnPrabhuCoop.getId()) {
                radioButtonId = 0;
                binding.textInputLayoutAccessCode.setVisibility(View.GONE);
                binding.btnRegister.setVisibility(View.VISIBLE);
                binding.textViewCollectorAppLabel.setVisibility(View.GONE);
            } else if (i == binding.radioBtnCollector.getId()) {
                radioButtonId = 1;
                binding.textInputLayoutAccessCode.setVisibility(View.VISIBLE);
                binding.btnRegister.setVisibility(View.GONE);
                binding.textViewCollectorAppLabel.setVisibility(View.VISIBLE);
            }
        }));

        binding.imageView2.setImageDrawable(requireContext().getResources().getDrawable(AppInfoClass.AppLoginIcon));
        binding.btnRegister.setOnClickListener(v ->
                requestForToken(v, R.id.action_login_to_register, binding.btnRegister));

        binding.btnForgotPassword.setOnClickListener(v ->
                requestForToken(v, R.id.action_login_to_recover, binding.btnForgotPassword));

        binding.btnLoginUser.setOnClickListener(v -> {
            if (radioButtonId == 1) {
                navigationRedirection.navigateToFragmentWithAction(v, R.id.action_login_to_collector_landing_activity);
            } else if (radioButtonId == 0) {
                /*TODO: Remove this before going live*/
                binding.textInputEditTextUsername.setText("9801979161");
                binding.textInputEditTextPassword.setText("Prabhu@123");

                getToken(
                        v,
                        Objects.requireNonNull(binding.textInputEditTextUsername.getText()).toString().trim(),
                        Objects.requireNonNull(binding.textInputEditTextPassword.getText()).toString().trim(),
                        binding.btnLoginUser
                );
            }
        });

        return binding.getRoot();
    }

    private void requestForToken(View v, int navigationDirection, Button actionButton) {
        actionButton.setEnabled(false);
        JsonObject requestTokenCredentials = new JsonObject();
        requestTokenCredentials.addProperty("username", UserCore.token_username);
        requestTokenCredentials.addProperty("password", UserCore.token_password);

        UserRepo.getAppToken(requireContext(), requestTokenCredentials).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                actionButton.setEnabled(true);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                actionButton.setEnabled(true);
                debugMode.showLog(TAG, "onError: " + e.getMessage());
            }
        });

        navigationRedirection.navigateToFragmentWithAction(v, navigationDirection);
    }

    /**
     * Request for token before login
     *
     * @param v            - View for navigation
     * @param username     - String for request
     * @param password     - String for request
     * @param btnLoginUser - AnimButton for setBusy loading animation
     */
    private void getToken(View v, String username, String password, AnimButton btnLoginUser) {
        if (username.isEmpty()) {
            binding.textInputLayoutUsername.setError("Invalid user");
            return;
        } else
            binding.textInputLayoutUsername.setError(null);

        if (password.isEmpty()) {
            binding.textInputLayoutPassword.setError("Invalid password");
            return;
        } else
            binding.textInputLayoutPassword.setError(null);

        JsonObject loginRequestBody = new JsonObject();
        loginRequestBody.addProperty("username", username);
        loginRequestBody.addProperty("password", password);

        btnLoginUser.setBusy(true);
        buttonSetBusy(binding.btnRegister, false);
        buttonSetBusy(binding.btnForgotPassword, false);
        UserRepo.getAppToken(requireContext(), loginRequestBody).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                UserLogin userLogin = new UserLogin();
                userLogin.username = username;
                userLogin.password = password;
                userLogin.rememberMe = true;
                userLogin.apiLevel = String.valueOf(Build.VERSION.SDK_INT);
                userLogin.appVersion = "";
                userLogin.buildNumber = "";
                userLogin.deviceModelName = Build.MODEL;
                userLogin.product = Build.MODEL;
                userLogin.manufacturer = Build.MANUFACTURER;
                userLogin.deviceName = Build.DEVICE;

                login(v, userLogin, btnLoginUser);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                btnLoginUser.setBusy(false);
                buttonSetBusy(binding.btnRegister, true);
                buttonSetBusy(binding.btnForgotPassword, true);
                ExceptionHandler.handleException(requireActivity(), e, action -> {
                    if (action == ExceptionHandler.RETRY) {
                        getToken(v, username, password, btnLoginUser);
                        btnLoginUser.setBusy(true);
                        buttonSetBusy(binding.btnRegister, false);
                        buttonSetBusy(binding.btnForgotPassword, false);
                    } else
                        showLoginFailedDialog(e.getMessage());
                });
//                Log.e(TAG, "onError: " + e.getMessage());
                debugMode.showLog(TAG, "onError: " + e.getMessage());
            }
        });
    }

    /**
     * Request for login user
     *
     * @param v            - View for navigation
     * @param userLogin    - User login object for user credentials (Username and password)
     * @param btnLoginUser - AnimButton for setBusy loading animation
     */
    private void login(View v, UserLogin userLogin, AnimButton btnLoginUser) {
        UserRepo.login(requireContext(), userLogin).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                btnLoginUser.setBusy(false);
                buttonSetBusy(binding.btnRegister, true);
                buttonSetBusy(binding.btnForgotPassword, true);
                navigationRedirection.navigateToFragmentWithAction(v, R.id.action_login_to_landing_activity);

                binding.textInputEditTextUsername.setText(null);
                binding.textInputEditTextPassword.setText(null);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                btnLoginUser.setBusy(false);
                buttonSetBusy(binding.btnForgotPassword, true);
                buttonSetBusy(binding.btnRegister, true);
                ExceptionHandler.handleException(requireActivity(), e, action -> {
                    if (action == ExceptionHandler.RETRY) {
                        login(v, userLogin, btnLoginUser);
                        btnLoginUser.setBusy(true);
                        buttonSetBusy(binding.btnForgotPassword, false);
                        buttonSetBusy(binding.btnRegister, false);
                    } else
                        showLoginFailedDialog(e.getMessage());
                });
                Log.e(TAG, "onError: " + e.getMessage());
                debugMode.showLog(TAG, "onError: " + e.getMessage());
            }
        });
    }

    private void buttonSetBusy(Button button, boolean busy) {
        button.setFocusable(busy);
        button.setClickable(busy);
    }

    private void showLoginFailedDialog(String message) {
        QuickUI.showDialog(requireActivity(), message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}