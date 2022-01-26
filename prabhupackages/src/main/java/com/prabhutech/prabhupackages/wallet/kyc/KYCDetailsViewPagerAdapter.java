package com.prabhutech.coop.wallet.kyc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class KYCDetailsViewPagerAdapter extends FragmentPagerAdapter {

    int noOfTabs;

    String title;

    private Fragment[] childFragments;

    public KYCDetailsViewPagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.noOfTabs = NumberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            title = "Details";
        } else if (position == 1) {
            title = "Document";
        }
        return title;
    }
}
