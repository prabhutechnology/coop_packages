package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.prabhutech.prabhupackages.databinding.FragmentMyAccountDetailsBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.adapter.TransactionAdapter;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class MyAccountDetailFragment extends Fragment {
    private static final String TAG = "MyAccountDetailFragment";

    private FragmentMyAccountDetailsBinding binding;

    //Variables
    private List<Transaction> transactions;
    private final List<Transaction> transactionList = new ArrayList<>();

    public MyAccountDetailFragment() {
    }

    public MyAccountDetailFragment(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountDetailsBinding.inflate(inflater, container, false);

        getLastSevenTransaction();

        return binding.getRoot();
    }

    private void getLastSevenTransaction() {
        if (transactions != null) {
            if (transactions.size() != 0) {
                for (int i = 0; i < transactions.size(); i++) {
                    binding.recyclerViewDetailTransaction.setVisibility(View.VISIBLE);
                    binding.textViewNoTransaction.setVisibility(View.GONE);

                    String date = transactions.get(i).getDate();
                    String name = transactions.get(i).getName();
                    String amount = transactions.get(i).getAmount();
                    String remarks = transactions.get(i).getRemarks();
                    String id = transactions.get(i).getId();

                    Transaction transaction = new Transaction(date, name, amount, remarks, id);
                    transactionList.add(transaction);
                    TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), transactionList);

                    binding.recyclerViewDetailTransaction.hasFixedSize();
                    binding.recyclerViewDetailTransaction.setAdapter(transactionAdapter);
                    binding.recyclerViewDetailTransaction.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            } else {
                binding.recyclerViewDetailTransaction.setVisibility(View.GONE);
                binding.textViewNoTransaction.setVisibility(View.VISIBLE);
            }
        } else {
            binding.recyclerViewDetailTransaction.setVisibility(View.GONE);
            binding.textViewNoTransaction.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}