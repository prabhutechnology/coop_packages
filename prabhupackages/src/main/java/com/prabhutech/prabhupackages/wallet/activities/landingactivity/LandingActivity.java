package com.prabhutech.prabhupackages.wallet.activities.landingactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.utils.TalkBackUtils;
import com.prabhutech.prabhupackages.wallet.core.utils.customviews.BottomSheetMaker;
import com.prabhutech.prabhupackages.wallet.core.utils.interfaces.BasicResponseCallback;
import com.prabhutech.prabhupackages.wallet.core.utils.permissions.PermissionManager;
import com.prabhutech.prabhupackages.wallet.core.utils.permissions.Permissions;
import com.prabhutech.prabhupackages.wallet.utils.TimerUtils;
import com.prabhutech.prabhupackages.wallet.views.AlertDialog;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.ActivityLandingBinding;
import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;
import com.prabhutech.prabhupackages.wallet.views.AppInfoClass;

import java.util.Objects;

import io.reactivex.observers.DisposableObserver;

public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "LandingActivity";

    //Variables
    private final String greeting = "Hi, " + UserCore.fullName;
    private boolean isToolbarOptionShow = true;
    private String fragmentLabel;

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @SuppressLint("StaticFieldLeak")
    public static Toolbar landingToolbar;
    private ActivityLandingBinding binding;

    private Object quitTimeOut;

    private final NavigationRedirection navigationRedirection;
    private final DebugMode debugMode;

    public LandingActivity() {
        navigationRedirection = new NavigationRedirection();
        debugMode = new DebugMode();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(AppInfoClass.AppTheme);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landing);
        landingToolbar = binding.toolbarLanding;

        landingToolbar.setTitle(greeting);

        setSupportActionBar(landingToolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.exploreFragment)
                .setOpenableLayout(binding.drawerLanding)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_landing);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navigationViewLanding, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            fragmentLabel = String.valueOf(destination.getLabel());
            if (fragmentLabel.equals("Explore Fragment")) {
                fragmentBehaviorHandling(View.VISIBLE, greeting, DrawerLayout.LOCK_MODE_UNLOCKED, true);
            } else if (fragmentLabel.equals("Show Result")) {
                fragmentBehaviorHandling(View.GONE, fragmentLabel, DrawerLayout.LOCK_MODE_LOCKED_CLOSED, false);
            } else {
                fragmentBehaviorHandling(View.VISIBLE, fragmentLabel, DrawerLayout.LOCK_MODE_LOCKED_CLOSED, false);
            }
            supportInvalidateOptionsMenu();
        });

        binding.navigationViewLanding.setNavigationItemSelectedListener(this);
        ImageView view = binding.navigationViewLanding.getHeaderView(0).findViewById(R.id.logo);
        view.setImageDrawable(getResources().getDrawable(AppInfoClass.AppDrawableIcon));

    }

    private void fragmentBehaviorHandling(int visibility, String title, int lockMode, boolean toolbarOptionVisibility) {
        landingToolbar.setVisibility(visibility);
        landingToolbar.setTitle(title);
        binding.drawerLanding.setDrawerLockMode(lockMode);
        isToolbarOptionShow = toolbarOptionVisibility;
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.are_you_sure_you_want_to_logout))
                .setMessage(getResources().getString(R.string.you_will_have_to_login_again_using_password_mpin))
                .setPositiveButton(getResources().getString(R.string.ok), (dI, I) -> logoutUser())
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .setButtonHighlight(AlertDialog.BUTTON_NEGATIVE)
                .show();
    }

    private void logoutUser() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = navigationRedirection.setupNavController(LandingActivity.this, R.id.fragment_container_landing);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        //Request Cheque Fragment
        if (itemId == R.id.requestChequeFragment) {
            navigationRedirection.replaceFragmentContainerWithFragment(this, R.id.fragment_container_landing, R.id.requestChequeFragment);
            binding.drawerLanding.closeDrawer(GravityCompat.START);
        }
        // Complain
        else if (itemId == R.id.complainFragment) {
            navigationRedirection.replaceFragmentContainerWithFragment(this, R.id.fragment_container_landing, R.id.complainFragment);
            binding.drawerLanding.closeDrawer(GravityCompat.START);
        }
        // Change password
        else if (itemId == R.id.changePassword) {
            showUpdatePasswordBottomSheet();
            binding.drawerLanding.closeDrawer(GravityCompat.START);
        }
        // Logout
        else if (itemId == R.id.logout) {
            showLogoutDialog();
            binding.drawerLanding.closeDrawer(GravityCompat.START);
        }
        // Default
        else {
            navigationRedirection.replaceFragmentContainerWithFragment(this, R.id.fragment_container_landing, R.id.comingSoon);
            binding.drawerLanding.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_side_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem itemNotification = menu.findItem(R.id.item_notification);
        final MenuItem itemQRScan = menu.findItem(R.id.item_qr_scan);
        View notificationItemView = itemNotification.getActionView();
//        notificationNumber = notificationItemView.findViewById(R.id.notification_size);
        notificationItemView.setOnClickListener(v ->
                navigationRedirection.replaceFragmentContainerWithFragment(LandingActivity.this, R.id.fragment_container_landing, R.id.notificationFragment)
        );
        itemNotification.setVisible(isToolbarOptionShow);
        itemQRScan.setVisible(isToolbarOptionShow);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_qr_scan) {
            PermissionManager.ask(LandingActivity.this, Manifest.permission.CAMERA, Permissions.CAMERA_REQUEST, null, new BasicResponseCallback() {
                @Override
                public void onSuccess() {
                    navigationRedirection.replaceFragmentContainerWithFragment(LandingActivity.this, R.id.fragment_container_landing, R.id.qrScanFragment);
                }

                @Override
                public void onFailure() {

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUpdatePasswordBottomSheet() {
        BottomSheetMaker passwordBottomSheet = new BottomSheetMaker(R.layout.update_password_dialog, v -> {
            TextInputLayout oldPasswordLayout = v.findViewById(R.id.old_password_textinputlayout);
            TextInputLayout newPasswordLayout = v.findViewById(R.id.new_password_textinputlayout);
            TextInputLayout confirmPasswordLayout = v.findViewById(R.id.confirm_password_textinputlayout);
            TextInputEditText oldPassword = v.findViewById(R.id.old_password_edittext);
            TextInputEditText newPassword = v.findViewById(R.id.new_password_edittext);
            TextInputEditText confirmPassword = v.findViewById(R.id.confirm_new_password_edittext);
            Button submit = v.findViewById(R.id.update_password_btn);
            Button cancel = v.findViewById(R.id.cancel);

            if (TalkBackUtils.talkBackActive(this)) {
                oldPasswordLayout.setPasswordVisibilityToggleEnabled(false);
                newPasswordLayout.setPasswordVisibilityToggleEnabled(false);
                confirmPasswordLayout.setPasswordVisibilityToggleEnabled(false);
            } else {
                oldPasswordLayout.setPasswordVisibilityToggleEnabled(true);
                newPasswordLayout.setPasswordVisibilityToggleEnabled(true);
                confirmPasswordLayout.setPasswordVisibilityToggleEnabled(true);
            }

            BottomSheetMaker bm = (BottomSheetMaker) getSupportFragmentManager().findFragmentByTag("UPDATEPASSWORD");
            cancel.setOnClickListener(v1 -> {
                if (bm != null) {
                    bm.dismiss();
                }
            });

            oldPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    oldPasswordLayout.setError(" ");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            newPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    newPasswordLayout.setError("");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            submit.setOnClickListener(v12 -> {
                String currentPassword = Objects.requireNonNull(oldPassword.getText()).toString();
                String newPW = Objects.requireNonNull(newPassword.getText()).toString();
                String confirmPW = Objects.requireNonNull(confirmPassword.getText()).toString();

                if (validatePassword(currentPassword, newPW, confirmPW, oldPasswordLayout, newPasswordLayout, confirmPasswordLayout)) {
                    JsonObject req = new JsonObject();
                    req.addProperty("CustomerId", UserCore.customerId);
                    req.addProperty("OldPassword", currentPassword);
                    req.addProperty("Password", newPW);
                    req.addProperty("ConfirmPassword", confirmPW);
                    UserRepo.changePassword(LandingActivity.this, req)
                            .subscribe(new DisposableObserver<JsonObject>() {
                                @Override
                                public void onNext(@NonNull JsonObject jsonObject) {
                                    JsonObject result = jsonObject.get("Result").getAsJsonObject();
                                    String successMessage = result.get("Message").getAsString();
                                    Toast.makeText(LandingActivity.this, "" + successMessage, Toast.LENGTH_SHORT).show();
//                                    Log.e(TAG, "onNext: " + result.toString());
                                    debugMode.showLog(TAG, "onNext: " + result.toString());
                                    if (bm != null) {
                                        bm.dismiss();
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
//                                    Log.e(TAG, "onError: " + e.getMessage());
                                    debugMode.showLog(TAG, "onError: " + e.getMessage());
                                    Toast.makeText(LandingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            });
        });

        passwordBottomSheet.show(getSupportFragmentManager(), "UPDATEPASSWORD");
    }

    private boolean validatePassword(String oldPassword, String newPassword, String confirmPassword, TextInputLayout oldPasswordLayout, TextInputLayout newPasswordLayout, TextInputLayout confirmPasswordLayout) {
        if (oldPassword == null || oldPassword.isEmpty()) {
            oldPasswordLayout.setError("Enter old password");
            return false;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            newPasswordLayout.setError("Enter new password");
            return false;
        }

        if (newPassword.length() < 4) {
            com.prabhutech.prabhupackages.wallet.views.Toast.show(this, "Password should be greater than 3");
            return false;
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            confirmPasswordLayout.setError("Enter your new password");
            return false;
        }

        if (confirmPassword.length() < 4) {
            com.prabhutech.prabhupackages.wallet.views.Toast.show(this, "Password should be greater than 3");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            com.prabhutech.prabhupackages.wallet.views.Toast.show(this, "Password don't match");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (fragmentLabel.equals("Explore Fragment")) {
            if (quitTimeOut == null) {
                Toast.makeText(this, "Press back again to logout", Toast.LENGTH_SHORT).show();
                quitTimeOut = TimerUtils.setTimeout(() -> {
                    TimerUtils.clearTimeout(quitTimeOut);
                    quitTimeOut = null;
                }, 2000);
            } else
                showLogoutDialog();
        } else
            super.onBackPressed();
    }
}