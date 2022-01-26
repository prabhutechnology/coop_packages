package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.form;



import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.DISH_HOME_FIBER_NET_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.LIFE_NET_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.PRIME_NET_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.SUBISU_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.TECH_MINDS_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.VIANET_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.WORLD_LINK_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ServiceName.SERVICE_INTERNET;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ServiceName.SERVICE_TOP_UP;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentFormBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.LandingActivity;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;

public class FormFragment extends Fragment {
    private static final String TAG = "FormFragment";
    private FragmentFormBinding binding;
    protected static String formServiceName;
    protected static String formProductId;
    protected static String formProductLabel;

    private NavController navController;
    private final NavigationRedirection navigationRedirection;

    public FormFragment() {
        navigationRedirection = new NavigationRedirection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFormBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.fragment_container_form);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        if (getArguments() != null) {
            com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.form.FormFragmentArgs formFragmentArgs = com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.form.FormFragmentArgs.fromBundle(getArguments());
            formServiceName = formFragmentArgs.getServiceLabel();
            formProductId = formFragmentArgs.getProductId();
            formProductLabel = formFragmentArgs.getProductLabel();
        }

        LandingActivity.landingToolbar.setTitle(formServiceName);
        //Top Up
        if (formServiceName.equals(SERVICE_TOP_UP)) {
            navigationRedirection.navigateWithNavController(navController, R.id.topUpFragment);
        }
        //Internet
        else if (formServiceName.equals(SERVICE_INTERNET)) {
            Log.e(TAG, "onCreateView: Id: " + formProductId);
            switch (formProductId) {
                //Direct Form
                //Internet
                case SUBISU_PRODUCT_ID:
                case WORLD_LINK_PRODUCT_ID:
                case VIANET_PRODUCT_ID:
                case DISH_HOME_FIBER_NET_PRODUCT_ID:
                case TECH_MINDS_PRODUCT_ID:
                case PRIME_NET_PRODUCT_ID:
                case LIFE_NET_PRODUCT_ID:
                    LandingActivity.landingToolbar.setTitle(formProductLabel);
                    navigationRedirection.navigateWithNavController(navController, R.id.indirectFormFragment);
                    break;
                //Indirect Form
                default:
                    //Default
                    LandingActivity.landingToolbar.setTitle("Coming Soon");
                    navigationRedirection.navigateWithNavController(navController, R.id.comingSoonFragment);
                    break;
            }
        } else {
            LandingActivity.landingToolbar.setTitle("Coming Soon");
            navigationRedirection.navigateWithNavController(navController, R.id.comingSoonFragment);
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}