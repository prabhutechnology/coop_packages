package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.productlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.prabhutech.prabhupackages.databinding.FragmentProductListBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.LandingActivity;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.productlist.adapter.ProductListAdapter;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {
    private static final String TAG = "FormFragment";

    private FragmentProductListBinding binding;
    private static String formProductName;
    private static List<Product> productList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductListBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            formProductName = getArguments().getString("ProductFormName");
            productList = (List<Product>) getArguments().getSerializable("PRODUCT_LIST");
        }
        LandingActivity.landingToolbar.setTitle(formProductName);

        ProductListAdapter productListAdapter = new ProductListAdapter(requireContext(), productList);
        binding.recyclerViewProductList.setAdapter(productListAdapter);
        binding.recyclerViewProductList.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}