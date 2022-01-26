package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.qrscan.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.qrscan.MainQRScanFragment;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.qrscan.QRViewFragment;

public class QRSlidePagerAdapter extends FragmentStatePagerAdapter {

    public QRSlidePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MainQRScanFragment();
        } else {
            return new QRViewFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
