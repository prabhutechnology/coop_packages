package com.prabhutech.prabhupackages.wallet.activities.collectorlandingactivity.fragments.statements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prabhutech.prabhupackages.R;

public class CollectorStatementAdapter extends RecyclerView.Adapter {
    public Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_DATA = 1;

    private boolean loading;

    public CollectorStatementAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            return new CollectorStatementHolder(LayoutInflater.from(context).inflate(R.layout.shimmer_landing_loader, parent, false));
        } else
            return new CollectorStatementHolder(LayoutInflater.from(context).inflate(R.layout.recent_transaction_card_ui, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        if (loading)
            return 10;
        else
            return 10;
    }

    public void setLoading(boolean loading){this.loading = loading;}

    @Override
    public int getItemViewType(int position) {
        if (loading) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_DATA;
        }
    }

    public class CollectorStatementHolder extends RecyclerView.ViewHolder {


        public CollectorStatementHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
