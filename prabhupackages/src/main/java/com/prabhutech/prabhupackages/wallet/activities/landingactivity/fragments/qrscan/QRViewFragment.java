package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.qrscan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentQRViewBinding;
import com.prabhutech.prabhupackages.wallet.core.UserCore;

public class QRViewFragment extends Fragment {
    private static final String TAG = "QRViewFragment";

    private FragmentQRViewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_q_r_view, container, false);

        if (TextUtils.isEmpty(UserCore.fullName)) {
            binding.fullName.setVisibility(View.GONE);
        } else
            binding.fullName.setText(UserCore.fullName);

        binding.mobileNumber.setText(UserCore.mobileNumber);

        Glide.with(requireContext()).load(UserCore.qrImageUrl).into(binding.myQrCode);
        Glide.with(requireContext()).load(UserCore.customerImgUrl).into(binding.profileImage);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}