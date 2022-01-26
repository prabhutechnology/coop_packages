package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.productlist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.ItemProductListBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.productlist.ProductListFragmentDirections;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.model.Product;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {
    private static final String TAG = "ProductListAdapter";

    private final Context context;
    private final List<Product> products;
    private final NavigationRedirection navigationRedirection;

    public ProductListAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        navigationRedirection = new NavigationRedirection();
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductListBinding binding = ItemProductListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        final Product product = products.get(position);
        holder.binding.textViewProductName.setText(product.getLabel());
        try {
            Glide.with(context).load(product.getImageUrl()).into(holder.binding.imageProduct);
        } catch (Exception e) {
            Glide.with(context).load(product.getImageUrl().isEmpty() ? R.drawable.img_prabhutv : product.getImageUrl()).into(holder.binding.imageProduct);
        }
        holder.binding.containerProduct.setOnClickListener(view -> {
            String serviceLabel = product.getServiceLabel();
            String productId = product.getId();
            String productLabel = product.getLabel();
            ProductListFragmentDirections.ActionProductListToForm actionProductListToForm =
                    ProductListFragmentDirections.actionProductListToForm();
            actionProductListToForm.setServiceLabel(serviceLabel);
            actionProductListToForm.setProductId(productId);
            actionProductListToForm.setProductLabel(productLabel);
            navigationRedirection.navigateToFragmentWithAction(view, actionProductListToForm);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductListViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductListBinding binding;

        public ProductListViewHolder(@NonNull ItemProductListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
