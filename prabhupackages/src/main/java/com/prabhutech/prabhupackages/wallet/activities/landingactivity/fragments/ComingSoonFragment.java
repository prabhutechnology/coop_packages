package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.prabhutech.prabhupackages.databinding.FragmentComingSoonBinding;


public class ComingSoonFragment extends Fragment {
    private FragmentComingSoonBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentComingSoonBinding.inflate(inflater, container, false);

        binding.btnBack.setOnClickListener(view -> requireActivity().onBackPressed());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}