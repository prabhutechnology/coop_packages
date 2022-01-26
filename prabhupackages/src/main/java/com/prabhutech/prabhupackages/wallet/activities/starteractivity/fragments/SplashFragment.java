package com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;
import com.prabhutech.prabhupackages.wallet.core.utils.Connectivity;
import com.prabhutech.prabhupackages.wallet.otpverification.AppSignatureHelper;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;
import com.prabhutech.prabhupackages.BuildConfig;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentSplashBinding;

import java.util.ArrayList;

public class SplashFragment extends Fragment {
    private static final String TAG = "SplashFragment";

    private FragmentSplashBinding binding;

    private boolean busy;
    private boolean isOnline = false;

    public static Uri isDeeplinkRedirect = null;
    public static Uri deepLink = null;

    private NavController navController;

    private final NavigationRedirection navigationRedirection;
    private final DebugMode debugMode;

    public SplashFragment() {
        navigationRedirection = new NavigationRedirection();
        debugMode = new DebugMode();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false);

        navController = navigationRedirection.setupNavController(requireActivity(), R.id.nav_host_fragment);

        ArrayList<String> appSignatures = new AppSignatureHelper(requireContext()).getAppSignatures();
        for (String s : appSignatures) {
//            Log.d(TAG, "onCreate: " + s);
            debugMode.showLog(TAG, "onCreate: " + s, "d");
        }

        try {
            deepLink = requireActivity().getIntent().getData();
            if (deepLink != null) {
                if (String.valueOf(deepLink).toLowerCase().contains("p.prabhupay.com")) {
                    join();
                } else {
//                    Log.e(TAG, "onCreateView: start deep web activity");
                    debugMode.showLog(TAG, "onCreateView: start deep web activity");
                }
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "No Intent", Toast.LENGTH_SHORT).show();
        }

        try {
            isDeeplinkRedirect = requireActivity().getIntent().getParcelableExtra("deeplink");
        } catch (Exception e) {
            isDeeplinkRedirect = null;
        }

        if (BuildConfig.SERVER != 0)
            Toast.makeText(requireContext(), "Test Mode", Toast.LENGTH_LONG).show();

     //   setupFirebase();

        join();

        return binding.getRoot();
    }

    private void setupFirebase() {
//        Log.i(TAG, "setupFirebase: ");
        debugMode.showLog(TAG, "setupFirebase: ", "i");
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(requireActivity());
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        // subscribe to topics 'all'
        if (!UserPref.getUserStatus(requireContext(), UserPref.SuperFlousStatus.NOTIFICATION_ALL_TOPIC_SUBSCRIBED))
            FirebaseMessaging.getInstance().subscribeToTopic("all");
        // Use the below code to get firebase token, token is the important one.
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();
//                Log.w(TAG, "setupFirebase: " + token + "  " + task.getResult().getId());
                debugMode.showLog(TAG, "setupFirebase: " + token + " " + task.getResult().getId());
            }
        });
    }

    /**
     * Determine whether to auto-login or not.
     */
    private void join() {
        isOnline = Connectivity.ping(requireContext());
        if (!isOnline) {
            setOfflineMode();
        } else {
            checkUpdatesThenAutoLogin();
        }
    }

    private void checkUpdatesThenAutoLogin() {
        autoLogin();
        /*UserPref.setNotificationDetails(requireContext(), false, false);
        UserRepo.getAppToken(requireContext()).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                autoLogin();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (!(e instanceof UnNeededException)) {
                    e.printStackTrace();
                    Log.e(TAG, "onError: " + e.getMessage());
                }
            }
        });*/
    }

    public void setOfflineMode() {
        new AlertDialog.Builder(requireContext())
                .setMessage(R.string.no_internet_message_while_login)
                .setNeutralButton(R.string.yes, (dialogInterface, i) -> {
                    join();
                    dialogInterface.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    private void autoLogin() {
        //Navigate to authentication fragment
        navigationRedirection.navigateWithNavController(navController, R.id.action_splash_to_authentication);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}