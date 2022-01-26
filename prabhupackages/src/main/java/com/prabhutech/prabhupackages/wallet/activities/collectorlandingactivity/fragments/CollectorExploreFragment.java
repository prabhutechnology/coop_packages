package com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentCollectorExploreBinding;
import com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.adapters.CollectorLandingStatementAdapter;


public class CollectorExploreFragment extends Fragment {
    private FragmentCollectorExploreBinding binding;

    private final com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection navigationRedirection;

    public CollectorExploreFragment() {
        navigationRedirection = new com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collector_explore, container, false);

        binding.recyclerLoadData.hasFixedSize();
        binding.recyclerLoadData.setLayoutManager(new LinearLayoutManager(requireContext()));

        CollectorLandingStatementAdapter adapter = new CollectorLandingStatementAdapter(requireContext());
        binding.recyclerLoadData.setAdapter(adapter);
        binding.layoutInfo.setOnClickListener(v -> {
            adapter.setLoading(true);
            adapter.notifyDataSetChanged();
            binding.textViewCbsSyncButton.setVisibility(View.GONE);
            binding.shimmerFrameText.setVisibility(View.VISIBLE);
            binding.textViewCbsSyncButtonShimmer.setText(R.string.sync_loading);
            binding.textViewCbsSyncButtonShimmer.setTextColor(getResources().getColor(R.color.colorPrimary));
            new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    adapter.setLoading(false);
                    adapter.notifyDataSetChanged();

                    binding.textViewCbsSyncButton.setVisibility(View.VISIBLE);
                    binding.shimmerFrameText.setVisibility(View.GONE);
                    binding.textViewCbsSyncButton.setText(R.string.cbs_sync);
                    binding.textViewCbsSyncButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sync_success, 0, 0, 0);
                    binding.textViewCbsSyncButton.setTextColor(getResources().getColor(R.color.green2));

                }
            }.start();
        });

        String[] names = new String[]{"Anish", "Susin", "Gunjan", "Bishal"};
//        binding.headerCollector.searchSavingScheme.searchText.setText("Saving Scheme");
        com.prabhutech.coop.wallet.utils.TextViewValue.setText(binding.headerCollector.searchSavingScheme.searchText, "Saving scheme");
        binding.headerCollector.searchSavingScheme.containerSearchText.setOnClickListener(v -> {
            // TODO: Ask Anish dai before removing
            /*CollectorExploreFragmentDirections.ActionCollectorExploreFragmentToSearchFragment check =
                    CollectorExploreFragmentDirections.actionCollectorExploreFragmentToSearchFragment(names, R.id.action_searchFragment_to_collectorExploreFragment);*/
            com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.CollectorExploreFragmentDirections.ActionCollectorExploreFragmentToSearchFragment check =
                    com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.CollectorExploreFragmentDirections.actionCollectorExploreFragmentToSearchFragment(names);
            navigationRedirection.navigateToFragmentWithAction(v, check);
        });

//        binding.headerCollector.searchCollectionArea.searchText.setText("Collection Area");
        com.prabhutech.coop.wallet.utils.TextViewValue.setText(binding.headerCollector.searchCollectionArea.searchText, "Collection area");
        binding.headerCollector.searchCollectionArea.containerSearchText.setOnClickListener(v -> {
            // TODO: Ask Anish dai before removing
            /*CollectorExploreFragmentDirections.ActionCollectorExploreFragmentToSearchFragment check =
                    CollectorExploreFragmentDirections.actionCollectorExploreFragmentToSearchFragment(names, R.id.action_searchFragment_to_collectorExploreFragment);*/
            com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.CollectorExploreFragmentDirections.ActionCollectorExploreFragmentToSearchFragment check =
                    com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.CollectorExploreFragmentDirections.actionCollectorExploreFragmentToSearchFragment(names);
            navigationRedirection.navigateToFragmentWithAction(v, check);
        });

        binding.layoutCollection.statementText.setOnClickListener(this::navigateStatement);
        binding.layoutCollection.icStatement.setOnClickListener(this::navigateStatement);

        binding.headerCollector.submitButton.setOnClickListener(view ->
                navigationRedirection.navigateToFragmentWithAction(view, R.id.action_collector_landing_to_amount_collection));

        return binding.getRoot();
    }

    public void navigateStatement(View view) {
        navigationRedirection.navigateToFragmentWithAction(view, R.id.action_collectorExploreFragment_to_collectorAccountStatementFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}