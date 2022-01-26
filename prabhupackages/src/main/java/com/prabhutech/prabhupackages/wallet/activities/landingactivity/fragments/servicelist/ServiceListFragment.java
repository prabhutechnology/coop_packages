package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist;

import static com.prabhutech.prabhupackages.wallet.constants.Product.GroupName.GROUP_UTILITY_BILLS;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.databinding.FragmentServiceListBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.LandingActivity;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.adapter.ServiceListAdapter;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.model.Product;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.model.Service;
import com.prabhutech.prabhupackages.wallet.core.api.utils.JsonUtils;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.core.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.observers.DisposableObserver;

public class ServiceListFragment extends Fragment {
    private static final String TAG = "ProductListFragment";

    private FragmentServiceListBinding binding;
    private List<Service> utilityPayProductServices = new ArrayList<>();
    private static String productName;

    private ProgressDialog progressDialog;
    private final ProgressDialogUtils progressDialogUtils;

    public ServiceListFragment() {
        progressDialogUtils = new ProgressDialogUtils();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentServiceListBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(requireContext());
        progressDialogUtils.showProgressDialog(progressDialog);
        getProductServices();

        com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.ServiceListFragmentArgs productListFragmentArgs = com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.ServiceListFragmentArgs.fromBundle(getArguments());
        productName = productListFragmentArgs.getServiceListName();
        LandingActivity.landingToolbar.setTitle(productName);

        return binding.getRoot();
    }

    private void getProductServices() {
        UserRepo.getCBSProductServiceList(requireContext()).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                progressDialogUtils.dismissProgressDialog(progressDialog);
                if (!jsonObject.get("ResultCommon").isJsonNull()) {
                    JsonArray productServiceJsonArray = jsonObject.get("ResultCommon").getAsJsonArray();
                    if (productServiceJsonArray != null) {
                        if (productServiceJsonArray.size() > 0) {
                            utilityPayProductServices = productsConvertTo(productServiceJsonArray);
                        } else {
                            utilityPayProductServices.clear();
                        }
                    } else {
                        utilityPayProductServices.clear();
                    }
                } else {
                    utilityPayProductServices.clear();
                }
                ServiceListAdapter serviceListAdapter = new ServiceListAdapter(requireContext(), utilityPayProductServices);
                binding.recyclerViewProductList.setAdapter(serviceListAdapter);
                binding.recyclerViewProductList.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
                progressDialogUtils.dismissProgressDialog(progressDialog);
                utilityPayProductServices.clear();
            }

            @Override
            public void onComplete() {
                progressDialogUtils.dismissProgressDialog(progressDialog);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static List<Service> productsConvertTo(JsonArray data) {
        List<Service> services = new ArrayList<>();
        Set<String> subGroupNameSet = new HashSet<>();
        if (productName.equals(GROUP_UTILITY_BILLS)) {
            List<Product> staticProductList = new ArrayList<>();
            Service staticService = new Service("TopUp", "Top Up", "", staticProductList);
            services.add(staticService);
        }

        for (JsonElement datum : data) {
            JsonObject service = datum.getAsJsonObject();

            Product product = new Product(
                    JsonUtils.safeString(service.get("SubGroupLabel"), ""),
                    JsonUtils.safeString(service.get("ProductId"), ""),
                    JsonUtils.safeString(service.get("ProductName"), ""),
                    JsonUtils.safeString(service.get("ProductLabel"), ""),
                    JsonUtils.safeString(service.get("ProductImageUrl"), ""),
                    JsonUtils.safeString(service.get("MinTransactionAmount"), ""),
                    JsonUtils.safeString(service.get("MaxTransactionAmount"), "")
            );
            String groupLabel = JsonUtils.safeString(service.get("GroupLabel"), "");
            String subGroupName = JsonUtils.safeString(service.get("SubGroupName"), "");
            String subGroupLabel = JsonUtils.safeString(service.get("SubGroupLabel"), "");
            String subGroupImageUrl = JsonUtils.safeString(service.get("SubGroupImageUrl"), "");
            // check if product with category is already there
            if (subGroupNameSet.contains(subGroupName)) {
                // find that and add to it
                for (Service iService : services) {
                    if (iService.getSubGroupName().equals(subGroupName)) {
                        iService.getProductList().add(product);
                    }
                }
            } else { // if not then create a new product along with its service
                subGroupNameSet.add(subGroupName);
                // create new product
                Service iService = new Service();
                if (groupLabel.equals(productName)) {
                    iService.setSubGroupName(subGroupName);
                    iService.setSubGroupLabel(subGroupLabel);
                    iService.setSubGroupImageUrl(subGroupImageUrl);
                    iService.setProductList(new ArrayList<>());
                    iService.getProductList().add(product);
                    services.add(iService);
                }
            }
        }
        return services;
    }
}