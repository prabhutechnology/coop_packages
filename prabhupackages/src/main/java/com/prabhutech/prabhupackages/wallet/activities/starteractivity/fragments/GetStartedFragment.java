package com.prabhutech.prabhupackages.wallet.activities.starteractivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentGetStartedBinding;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;


public class GetStartedFragment extends Fragment {
    private static final String TAG = "GetStartedFragment";

    private FragmentGetStartedBinding binding;

    private final NavigationRedirection navigationRedirection;

    public GetStartedFragment() {
        navigationRedirection = new NavigationRedirection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGetStartedBinding.inflate(inflater, container, false);

//        binding.btnGetStarted.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.action_getStarted_to_login));
        binding.btnGetStarted.setOnClickListener(view1 -> navigationRedirection.navigateToFragmentWithAction(view1, R.id.action_getStarted_to_login));

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}