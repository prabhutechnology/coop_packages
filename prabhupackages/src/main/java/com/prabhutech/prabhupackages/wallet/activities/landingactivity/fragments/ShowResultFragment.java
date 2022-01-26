package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments;

import static com.prabhutech.coop.wallet.constants.ValidationMessage.FAILED;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.SUCCESS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentShowResultBinding;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;


public class ShowResultFragment extends Fragment {
    private FragmentShowResultBinding binding;

    private final NavigationRedirection navigationRedirection;

    public ShowResultFragment() {
        navigationRedirection = new NavigationRedirection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowResultBinding.inflate(inflater, container, false);

        ShowResultFragmentArgs showResultFragmentArgs = ShowResultFragmentArgs.fromBundle(getArguments());
        String message = showResultFragmentArgs.getResultMessage();
        String detail = showResultFragmentArgs.getResultDetail();

        if (message.equals(SUCCESS)) {
            binding.textViewResultMessage.setText(message);
            binding.imageViewResult.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.success_logo));
        } else if (message.equals(FAILED)) {
            binding.textViewResultMessage.setText(message);
            binding.imageViewResult.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.failed_logo));
        }
        binding.textViewResultDetail.setText(detail);
        binding.btnGoBackHome.setOnClickListener(navigationRedirection::navigateBack);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}