package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.statement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.HistoryTransactionListNewBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.statement.model.AccountDetailsModel;

import java.util.List;

public class StatementAdapter extends RecyclerView.Adapter<StatementAdapter.StatementAdapterViewHolder> {
    Context context;
    List<AccountDetailsModel> models;

    public StatementAdapter(Context context, List<AccountDetailsModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public StatementAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HistoryTransactionListNewBinding binding = HistoryTransactionListNewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StatementAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StatementAdapterViewHolder holder, int position) {
        AccountDetailsModel ad = models.get(holder.getAdapterPosition());
        holder.binding.transactionTitle.setText(ad.Particular);
        holder.binding.transactionTxnID.setText(ad.Date);
        if (ad.Debit == null) {
            holder.binding.transactionAmount.setText(ad.Credit);
            holder.binding.status.setText(R.string.credited);
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.txn_success_text_color));
            holder.binding.iconHistory.setBackgroundColor(context.getResources().getColor(R.color.txn_success_text_color));
        } else {
            holder.binding.transactionAmount.setText(ad.Debit);
            holder.binding.status.setText(R.string.debited);
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.txn_failed_text_color));
            holder.binding.iconHistory.setBackgroundColor(context.getResources().getColor(R.color.txn_failed_text_color));
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class StatementAdapterViewHolder extends RecyclerView.ViewHolder {
        private final HistoryTransactionListNewBinding binding;

        public StatementAdapterViewHolder(@NonNull HistoryTransactionListNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
