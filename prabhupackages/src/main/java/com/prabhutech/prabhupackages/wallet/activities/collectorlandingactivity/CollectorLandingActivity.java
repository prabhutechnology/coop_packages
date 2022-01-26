package com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.prabhutech.prabhupackages.wallet.core.utils.permissions.PermissionManager;
import com.prabhutech.prabhupackages.wallet.core.utils.permissions.Permissions;
import com.prabhutech.prabhupackages.wallet.utils.TimerUtils;
import com.prabhutech.prabhupackages.wallet.views.AlertDialog;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.ActivityCollectorLandingBinding;
import com.prabhutech.prabhupackages.wallet.core.utils.interfaces.BasicResponseCallback;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;
import com.prabhutech.prabhupackages.wallet.views.AppInfoClass;

public class CollectorLandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "CollectorLandingActivity";

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    private ActivityCollectorLandingBinding binding;

    private final NavigationRedirection navigationRedirection;

    //    private TextView notificationNumber;
    private boolean isShowed = true;
    private Object quitTimeOut;
    private String fragmentLabel;

    public CollectorLandingActivity() {
        navigationRedirection = new NavigationRedirection();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(AppInfoClass.AppTheme);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collector_landing);
        setSupportActionBar(binding.toolbarCollectorLanding);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.collectorExploreFragment)
                .setOpenableLayout(binding.drawerCollectorLanding)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_collector_landing);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navigationViewCollectorLanding, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            fragmentLabel = String.valueOf(destination.getLabel());
            if (fragmentLabel.equals("Collector Explore Fragment")) {
                binding.toolbarCollectorLanding.setTitle("Hi User!");
                binding.drawerCollectorLanding.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                isShowed = true;
            } else {
                binding.toolbarCollectorLanding.setTitle(fragmentLabel);
                binding.drawerCollectorLanding.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                isShowed = false;
            }
            supportInvalidateOptionsMenu();
        });

        binding.navigationViewCollectorLanding.setNavigationItemSelectedListener(this);
        ImageView view = binding.navigationViewCollectorLanding.getHeaderView(0).findViewById(R.id.logo);
        view.setImageDrawable(getResources().getDrawable(AppInfoClass.AppDrawableIcon));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        //Account Statement
        if (itemId == R.id.collectorAccountStatementFragment) {
            navigationRedirection.replaceFragmentContainerWithFragment(this, R.id.fragment_container_collector_landing, R.id.collectorAccountStatementFragment);
            binding.drawerCollectorLanding.closeDrawer(GravityCompat.START);
        }
        // Logout
        else if (itemId == R.id.logout) {
            showLogoutDialog();
            binding.drawerCollectorLanding.closeDrawer(GravityCompat.START);
        }
        // Default
        else {
            navigationRedirection.replaceFragmentContainerWithFragment(this, R.id.fragment_container_collector_landing, R.id.comingSoonCollectorFragment);
            binding.drawerCollectorLanding.closeDrawer(GravityCompat.START);
        }
        return true;
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
        NavController navController = navigationRedirection.setupNavController(this, R.id.fragment_container_collector_landing);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
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
                navigationRedirection.replaceFragmentContainerWithFragment(CollectorLandingActivity.this, R.id.fragment_container_collector_landing, R.id.notificationFragment)
        );
        itemNotification.setVisible(isShowed);
        itemQRScan.setVisible(isShowed);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_qr_scan) {
            PermissionManager.ask(CollectorLandingActivity.this, Manifest.permission.CAMERA, Permissions.CAMERA_REQUEST, null,
                    new BasicResponseCallback() {
                @Override
                public void onSuccess() {
                    navigationRedirection.replaceFragmentContainerWithFragment(CollectorLandingActivity.this, R.id.fragment_container_collector_landing, R.id.qrScanFragment);
                }

                @Override
                public void onFailure() {

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fragmentLabel.equals("Collector Explore Fragment")) {
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