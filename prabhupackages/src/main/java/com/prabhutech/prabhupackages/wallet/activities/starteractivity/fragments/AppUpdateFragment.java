package com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.prabhutech.prabhupackages.wallet.core.utils.AppUtils;
import com.prabhutech.prabhupackages.databinding.FragmentAppUpdateBinding;

public class AppUpdateFragment extends Fragment {
    private FragmentAppUpdateBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAppUpdateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        boolean isForced = com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments.AppUpdateFragmentArgs.fromBundle(getArguments()).getIsForced();
        String features = com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments.AppUpdateFragmentArgs.fromBundle(getArguments()).getFeatures();
        boolean noExit = com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments.AppUpdateFragmentArgs.fromBundle(getArguments()).getNoExit();

        binding.updateInfo.setText(Html.fromHtml(features));
        if (isForced) {
            binding.updateLater.setVisibility(View.GONE);
        } else {
            binding.updateLater.setOnClickListener(v -> AppUtils.startAppUpdate(requireActivity()));
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}