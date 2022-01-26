package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.MyAccountChartFragment;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.MyAccountDetailFragment;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model.LastThirtyBalance;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model.Transaction;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;

import java.util.List;

public class MyAccountAdapter extends FragmentPagerAdapter {
    private static final String TAG = "FragmentPagerAdapter";
    private final MyAccountChartFragment myAccountChartFragment;
    private final MyAccountDetailFragment myAccountDetailFragment;

    public MyAccountAdapter(FragmentManager fm, Context context, List<LastThirtyBalance> lastThirtyBalance, List<Transaction> transactions) {
        super(fm);
        DebugMode.log(TAG, "MyAccountAdapter: " + lastThirtyBalance.size());
        myAccountChartFragment = new MyAccountChartFragment(lastThirtyBalance);
        myAccountDetailFragment = new MyAccountDetailFragment(transactions);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return myAccountChartFragment;
        } else {
            return myAccountDetailFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
