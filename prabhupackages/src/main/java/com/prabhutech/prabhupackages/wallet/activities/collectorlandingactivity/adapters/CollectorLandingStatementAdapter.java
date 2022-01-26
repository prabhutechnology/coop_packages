package com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.RecentTransactionCardUiBinding;
import com.prabhutech.prabhupackages.databinding.ShimmerLandingLoaderBinding;


public class CollectorLandingStatementAdapter extends RecyclerView.Adapter<CollectorLandingStatementAdapter.CollectorLandingStatementViewHolder> {
    public Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_DATA = 1;
    private boolean loading;

    public CollectorLandingStatementAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CollectorLandingStatementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            ShimmerLandingLoaderBinding shimmerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.shimmer_landing_loader, parent, false);
            return new CollectorLandingStatementViewHolder(shimmerBinding);
        } else {
            RecentTransactionCardUiBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recent_transaction_card_ui, parent, false);
            return new CollectorLandingStatementViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CollectorLandingStatementViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_DATA) {
            CollectorLandingStatementViewHolder holder1 = (CollectorLandingStatementViewHolder) holder;
            holder1.binding.editAmounts.setOnClickListener(v -> holder1.binding.editConstraints.setVisibility(View.VISIBLE));
            holder1.binding.closeImageCard.setOnClickListener(v -> holder1.binding.editConstraints.setVisibility(View.GONE));
        }
    }

    @Override
    public int getItemCount() {
        if (loading)
            return 5;
        else
            return 10;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public int getItemViewType(int position) {
        if (loading) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_DATA;
        }
    }

    public static class CollectorLandingStatementViewHolder extends RecyclerView.ViewHolder {
        private RecentTransactionCardUiBinding binding;

        public CollectorLandingStatementViewHolder(@NonNull RecentTransactionCardUiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public CollectorLandingStatementViewHolder(@NonNull ShimmerLandingLoaderBinding shimmerBinding) {
            super(shimmerBinding.getRoot());
        }
    }
}
