package com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.statements;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentCollectorAccountStatementBinding;
import com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.adapters.CollectorLandingStatementAdapter;


public class CollectorAccountStatementFragment extends Fragment {

    FragmentCollectorAccountStatementBinding binding;
    CollectorLandingStatementAdapter adapter;

    public CollectorAccountStatementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collector_account_statement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = DataBindingUtil.bind(view);

        assert binding != null;
        binding.recyclerStatementList.hasFixedSize();
        binding.recyclerStatementList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CollectorLandingStatementAdapter(requireContext());
        binding.recyclerStatementList.setAdapter(adapter);
        binding.containerSearchDate.buttonViewStatement.setOnClickListener(v -> loadingData());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadingData(){
        adapter.setLoading(true);
        adapter.notifyDataSetChanged();
        new CountDownTimer(5000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                adapter.setLoading(false);
                adapter.notifyDataSetChanged();
            }
        }.start();
    }
}