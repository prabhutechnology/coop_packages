package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.qrscan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentQRScanBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.qrscan.adapter.QRSlidePagerAdapter;


public class QRScanFragment extends Fragment {
    private static final String TAG = "QRScanFragment";

    private FragmentQRScanBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_q_r_scan, container, false);

        QRSlidePagerAdapter slidePagerAdapter = new QRSlidePagerAdapter(getChildFragmentManager());
        binding.viewPagerQRCode.setAdapter(slidePagerAdapter);

        binding.viewPagerQRCode.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateActivePageTab(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.textViewQrScan.setOnClickListener(v -> setActivePage(0));
        binding.textViewMyQr.setOnClickListener(v -> setActivePage(1));

        return binding.getRoot();
    }

    private void setActivePage(int page) {
        binding.viewPagerQRCode.setCurrentItem(page);
        updateActivePageTab(page);
    }

    private void updateActivePageTab(int position) {
        binding.textViewQrScan.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.textViewQrScan.setBackgroundColor(getResources().getColor(R.color.white));
        binding.textViewMyQr.setTextColor(getResources().getColor(R.color.colorBlack));
        binding.textViewMyQr.setBackgroundColor(getResources().getColor(R.color.white));
        if (position == 0) {
            binding.textViewQrScan.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.textViewQrScan.setBackgroundColor(getResources().getColor(R.color.primaryLight));
        } else {
            binding.textViewMyQr.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.textViewMyQr.setBackgroundColor(getResources().getColor(R.color.primaryLight));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}