package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.HistoryTransactionListNewBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionAdapterViewHolder> {
    private static final String TAG = "TransactionAdapter";
    private final Context context;
    private final List<Transaction> transactions;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HistoryTransactionListNewBinding binding = HistoryTransactionListNewBinding.inflate(inflater, parent, false);
        return new TransactionAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapterViewHolder holder, int position) {
        Transaction transaction = transactions.get(holder.getAdapterPosition());
        holder.binding.transactionTitle.setText(transaction.getRemarks());

        String date = transaction.getDate();
        String formattedDate = date.replace("T", " ");

        holder.binding.transactionTxnID.setText(formattedDate);
        if (transaction.getName().equals("Deposit")) {
            holder.binding.transactionAmount.setText(transaction.getAmount());
            holder.binding.status.setText(transaction.getName());
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.txn_success_text_color));
            holder.binding.iconHistory.setBackgroundColor(context.getResources().getColor(R.color.txn_success_text_color));
        } else {
            holder.binding.transactionAmount.setText(transaction.getAmount());
            holder.binding.status.setText(transaction.getName());
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.txn_failed_text_color));
            holder.binding.iconHistory.setBackgroundColor(context.getResources().getColor(R.color.txn_failed_text_color));
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(transactions.size(), 7);
    }

    public static class TransactionAdapterViewHolder extends RecyclerView.ViewHolder {
        private final HistoryTransactionListNewBinding binding;

        public TransactionAdapterViewHolder(@NonNull HistoryTransactionListNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
