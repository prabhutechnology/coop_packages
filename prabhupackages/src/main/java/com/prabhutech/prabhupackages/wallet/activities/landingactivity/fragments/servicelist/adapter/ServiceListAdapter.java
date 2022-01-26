package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.adapter;

import static com.prabhutech.prabhupackages.wallet.constants.Product.ServiceName.SERVICE_INTERNET;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ServiceName.SERVICE_TOP_UP;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.model.Product;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.model.Service;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.ItemProductListBinding;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceListViewHolder> {
    private static final String TAG = "ProductListAdapter";

    private final Context context;
    private final List<Service> services;
    private final NavigationRedirection navigationRedirection;
    private List<Product> productList = new ArrayList<>();

    public ServiceListAdapter(Context context, List<Service> services) {
        this.context = context;
        this.services = services;
        navigationRedirection = new NavigationRedirection();
    }

    @NonNull
    @Override
    public ServiceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductListBinding binding = ItemProductListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ServiceListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListViewHolder holder, int position) {
        final Service service = services.get(position);
        holder.binding.textViewProductName.setText(service.getSubGroupLabel());
        try {
            switch (service.getSubGroupName()) {
                /*case "Internet":
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_internet)).into(holder.binding.imageProduct);
                    break;
                case "Electricity":
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_electricity)).into(holder.binding.imageProduct);
                    break;*/
                case "TopUp":
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_topup)).into(holder.binding.imageProduct);
                    break;
                case "Internet":
                case "Electricity":
                case "Antivirus":
                case "Water":
                case "Landline":
                    Glide.with(context).load(service.getSubGroupImageUrl()).into(holder.binding.imageProduct);
                    break;
                default:
                    Glide.with(context).load(service.getSubGroupImageUrl().isEmpty() ? R.drawable.img_prabhutv : service.getSubGroupImageUrl()).into(holder.binding.imageProduct);
                    break;
            }
        } catch (Exception e) {
            Glide.with(context).load(service.getSubGroupImageUrl().isEmpty() ? R.drawable.img_prabhutv : service.getSubGroupImageUrl()).into(holder.binding.imageProduct);
        }

        Bundle bundle = new Bundle();
        if (service.getSubGroupLabel().equals(SERVICE_TOP_UP)) {
            holder.binding.containerProduct.setOnClickListener(view -> {
                String serviceLabel = service.getSubGroupLabel();
                com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.ServiceListFragmentDirections.ActionServiceListToForm actionServiceListToForm =
                        com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.ServiceListFragmentDirections.actionServiceListToForm();
                actionServiceListToForm.setServiceLabel(serviceLabel);
                navigationRedirection.navigateToFragmentWithAction(view, actionServiceListToForm);
            });
        } else if (service.getSubGroupLabel().equals(SERVICE_INTERNET)) {
            holder.binding.containerProduct.setOnClickListener(view -> {
                productList = service.getProductList();
                bundle.putString("ProductFormName", service.getSubGroupLabel());
                bundle.putSerializable("PRODUCT_LIST", (Serializable) productList);
                navigationRedirection.navigateWithBundle(view, R.id.action_service_list_to_product_list, bundle);
            });
        } else {
            holder.binding.containerProduct.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.comingSoon));
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public static class ServiceListViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductListBinding binding;

        public ServiceListViewHolder(@NonNull ItemProductListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
