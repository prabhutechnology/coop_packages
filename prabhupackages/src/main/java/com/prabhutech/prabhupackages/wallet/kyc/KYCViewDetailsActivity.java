package com.prabhutech.prabhupackages.wallet.kyc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.UriContracts;
import com.prabhutech.prabhupackages.wallet.core.api.utils.reflection.Reflect;
import com.prabhutech.prabhupackages.wallet.utils.ExceptionHandler;
import com.prabhutech.prabhupackages.wallet.views.QuickUI;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class KYCViewDetailsActivity extends AppCompatActivity implements KYCDetailsModelSaver {

    public static final String TAG = "KYCDetailsRegister";

    public String kycStatus, kycRemarks;

    KycDetails kycDetails = new KycDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycview);

        // TODO: 7/8/19 check on empty, exit activity

        Toolbar toolbar = findViewById(R.id.kycViewToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("View KYC");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        requestKYCDetails();
    }

    private void requestKYCDetails() {
        JsonObject req = new JsonObject();

        ((Observable<JsonObject>) Reflect.repoGet(UriContracts.URI_KYC_REPO, "getKycDetail", getApplicationContext(), req))
                .subscribe(new DisposableObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject res) {
                        KycDetails details = KycDetails.mapJsonToModel(res);

                        kycStatus = res.get("kycStatus").getAsString();
                        kycRemarks = res.get("remarks").toString();

                        saveModel(details);
                        setupTabs();

//                        QuickUI.showToast(KYCViewDetailsActivity.this, "Success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        ExceptionHandler.handleException(KYCViewDetailsActivity.this, e, action -> {
                            if (action == ExceptionHandler.RETRY) requestKYCDetails();
                            else if (action == ExceptionHandler.CANCEL) finish();
                            else QuickUI.errDialog(KYCViewDetailsActivity.this, e.getMessage());
                        });

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setupTabs() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Documents"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.kyc_view_viewpager);

        final com.prabhutech.coop.wallet.kyc.KYCDetailsViewPagerAdapter adapter = new com.prabhutech.coop.wallet.kyc.KYCDetailsViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void saveModel(KycDetails details) {

    }

    @Override
    public KycDetails getModel() {
        return null;
    }
}
