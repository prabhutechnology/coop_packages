package com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.amountcollection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.prabhutech.coop.wallet.utils.TextViewValue;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentAmountCollectionBinding;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;

public class AmountCollectionFragment extends Fragment {
    private FragmentAmountCollectionBinding binding;

    private final NavigationRedirection navigationRedirection;

    public AmountCollectionFragment() {
        navigationRedirection = new NavigationRedirection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_amount_collection, container, false);

        String[] names = new String[]{"Anish", "Susin", "Gunjan", "Bishal"};
        /*MemberName*/
        TextViewValue.setText(binding.layoutMemberName.textViewNameHint, "Member Name");
        TextViewValue.setText(binding.layoutMemberName.textViewNameData, "ABC Shrestha");

        /*Account Number*/
        TextViewValue.setText(binding.layoutMemberAccountNumber.textViewNameHint, "Account Number");
        TextViewValue.setText(binding.layoutMemberAccountNumber.textViewNameData, "00100202020200323ABC");

        /*Account Scheme*/
        TextViewValue.setText(binding.layoutMemberAccountScheme.textViewNameHint, "Account Scheme");
        TextViewValue.setText(binding.layoutMemberAccountScheme.textViewNameData, "Bal Bachat");

        /*Total Balance*/
        TextViewValue.setText(binding.layoutMemberTotalBalance.textViewNameHint, "Total Balance");
        TextViewValue.setText(binding.layoutMemberTotalBalance.textViewNameData, "NPR. 10,00,000");

        /*Hold Balance*/
        TextViewValue.setText(binding.layoutMemberHoldBalance.textViewNameHint, "Hold Balance");
        TextViewValue.setText(binding.layoutMemberHoldBalance.textViewNameData, "NPR. 0.00");

        /*Minimum Balance*/
        TextViewValue.setText(binding.layoutMemberMinimumBalance.textViewNameHint, "Minimum Balance");
        TextViewValue.setText(binding.layoutMemberMinimumBalance.textViewNameData, "NPR. 1,000");

        /*Available Balance*/
        TextViewValue.setText(binding.layoutMemberAvailableBalance.textViewNameHint, "Available Balance");
        TextViewValue.setText(binding.layoutMemberAvailableBalance.textViewNameData, "NPR. 10,00,000");

        /*Status*/
        TextViewValue.setText(binding.layoutMemberStatus.textViewNameHint, "Status");
        TextViewValue.setText(binding.layoutMemberStatus.textViewNameData, "Deposit");

        TextViewValue.setText(binding.headerDataLayout.searchEditTextSaving.searchText, "Saving scheme");
        binding.headerDataLayout.searchEditTextSaving.containerSearchText.setOnClickListener(v -> {
            // TODO: Ask Anish dai before removing
            /*AmountCollectionFragmentDirections.ActionAmountCollectionFragmentToSearchFragment actionAmountCollectionFragmentToSearchFragment =
                    AmountCollectionFragmentDirections.actionAmountCollectionFragmentToSearchFragment(names, R.id.action_searchFragment_to_amountCollectionFragment);*/
            com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.amountcollection.AmountCollectionFragmentDirections.ActionAmountCollectionFragmentToSearchFragment actionAmountCollectionFragmentToSearchFragment =
                    com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.amountcollection.AmountCollectionFragmentDirections.actionAmountCollectionFragmentToSearchFragment(names);
            navigationRedirection.navigateToFragmentWithAction(v, actionAmountCollectionFragmentToSearchFragment);
        });

        TextViewValue.setText(binding.headerDataLayout.searchEditTextCollector.searchText, "Collection area");
        binding.headerDataLayout.searchEditTextCollector.containerSearchText.setOnClickListener(v -> {
            com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.amountcollection.AmountCollectionFragmentDirections.ActionAmountCollectionFragmentToSearchFragment actionAmountCollectionFragmentToSearchFragment =
                    com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.amountcollection.AmountCollectionFragmentDirections.actionAmountCollectionFragmentToSearchFragment(names);
            navigationRedirection.navigateToFragmentWithAction(v, actionAmountCollectionFragmentToSearchFragment);
        });

        binding.searchCardView.setOnClickListener(v -> {
            com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.amountcollection.AmountCollectionFragmentDirections.ActionAmountCollectionFragmentToSearchFragment actionAmountCollectionFragmentToSearchFragment =
                    com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.amountcollection.AmountCollectionFragmentDirections.actionAmountCollectionFragmentToSearchFragment(names);
            navigationRedirection.navigateToFragmentWithAction(v, actionAmountCollectionFragmentToSearchFragment);
        });

        binding.saveCollectionAmount.setOnClickListener(v -> navigationRedirection.navigateToFragmentWithAction(v, R.id.action_amountCollectionFragment_to_collectorExploreFragment));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}