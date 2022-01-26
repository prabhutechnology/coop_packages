package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.form;



import static com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.form.FormFragment.formProductId;
import static com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.form.FormFragment.formServiceName;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.DISH_HOME_FIBER_NET_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.LIFE_NET_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.PRIME_NET_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.SUBISU_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.TECH_MINDS_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.VIANET_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductIdAPI.WORLD_LINK_PRODUCT_ID;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ServiceName.SERVICE_INTERNET;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.prabhutech.prabhupackages.databinding.FragmentIndirectFormBinding;

public class IndirectFormFragment extends Fragment {
    private FragmentIndirectFormBinding binding;
    protected com.prabhutech.prabhupackages.wallet.utils.views.TextInputLayoutUtil textInputLayoutUtilUsername;
    protected com.prabhutech.prabhupackages.wallet.utils.views.TextInputLayoutUtil textInputLayoutUtilMobileNumber;
    protected com.prabhutech.prabhupackages.wallet.utils.views.TextInputLayoutUtil textInputLayoutUtilAmount;
    protected com.prabhutech.prabhupackages.wallet.utils.views.TextInputLayoutUtil textInputLayoutUtilCustomerId;
    protected com.prabhutech.prabhupackages.wallet.utils.views.DynamicAnimButton animButtonUtilSeePaymentDetail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentIndirectFormBinding.inflate(inflater, container, false);

        String serviceName = formServiceName;
        String productId = formProductId;

        //Text input layout for username
        textInputLayoutUtilUsername = new com.prabhutech.prabhupackages.wallet.utils.views.TextInputLayoutUtil(requireContext(), binding.dynamicTextInputLayoutUsername.dynamicTextInputNormalLayout, binding.dynamicTextInputLayoutUsername.dynamicTextInputNormalEditText);
        textInputLayoutInitialize(textInputLayoutUtilUsername, "Username", InputType.TYPE_CLASS_TEXT);

        //Text input layout for mobile number
        textInputLayoutUtilMobileNumber = new com.prabhutech.prabhupackages.wallet.utils.views.TextInputLayoutUtil(requireContext(), binding.dynamicTextInputLayoutMobileNumber.dynamicTextInputNormalLayout, binding.dynamicTextInputLayoutMobileNumber.dynamicTextInputNormalEditText);
        textInputLayoutInitialize(textInputLayoutUtilMobileNumber, "Mobile number", InputType.TYPE_CLASS_NUMBER);

        //Text input layout for amount
        textInputLayoutUtilAmount = new com.prabhutech.prabhupackages.wallet.utils.views.TextInputLayoutUtil(requireContext(), binding.dynamicTextInputLayoutAmount.dynamicTextInputNormalLayout, binding.dynamicTextInputLayoutAmount.dynamicTextInputNormalEditText);
        textInputLayoutInitialize(textInputLayoutUtilAmount, "Amount", InputType.TYPE_CLASS_NUMBER);

        //Text input layout for customer id
        textInputLayoutUtilCustomerId = new com.prabhutech.prabhupackages.wallet.utils.views.TextInputLayoutUtil(requireContext(), binding.dynamicTextInputLayoutCustomerId.dynamicTextInputNormalLayout, binding.dynamicTextInputLayoutCustomerId.dynamicTextInputNormalEditText);
        textInputLayoutInitialize(textInputLayoutUtilCustomerId, "Customer Id", InputType.TYPE_CLASS_NUMBER);

        //Button
        animButtonUtilSeePaymentDetail = new com.prabhutech.prabhupackages.wallet.utils.views.DynamicAnimButton(requireContext(), binding.dynamicBtnSeeDetails.animButton, binding.dynamicBtnSeeDetails.animProgressBar);
        animButtonUtilSeePaymentDetail.setBusy(false);
        animButtonUtilSeePaymentDetail.setButtonText("See payment details");

        if (serviceName.equals(SERVICE_INTERNET)) {
            switch (productId) {
                /*
                 * setViewVisibility
                 * 1st parameter - username text input layout
                 * 2nd parameter - mobile number text input layout
                 * 3rd parameter - amount text input layout
                 * 4th parameter - customer id text input layout
                 */
                case SUBISU_PRODUCT_ID:
                    setViewVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE);
                    break;
                case WORLD_LINK_PRODUCT_ID:
                case PRIME_NET_PRODUCT_ID:
                case LIFE_NET_PRODUCT_ID:
                case TECH_MINDS_PRODUCT_ID:
                    setViewVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE);
                    break;
                case VIANET_PRODUCT_ID:
                case DISH_HOME_FIBER_NET_PRODUCT_ID:
                    setViewVisibility(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
                    break;
                default:
                    setViewVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
                    break;
            }
        }
        return binding.getRoot();
    }

    /**
     * View visibility
     *
     * @param usernameVisibility     - Username Text Input Layout visibility
     * @param mobileNumberVisibility - Mobile Number Text Input Layout visibility
     * @param amountVisibility       - Amount Text Input Layout visibility
     * @param customerIdVisibility   - Customer ID Text Input Layout visibility
     */
    private void setViewVisibility(int usernameVisibility, int mobileNumberVisibility, int amountVisibility, int customerIdVisibility) {
        textInputLayoutUtilUsername.setVisibility(usernameVisibility);
        textInputLayoutUtilMobileNumber.setVisibility(mobileNumberVisibility);
        textInputLayoutUtilAmount.setVisibility(amountVisibility);
        textInputLayoutUtilCustomerId.setVisibility(customerIdVisibility);
    }

    private void textInputLayoutInitialize(com.prabhutech.prabhupackages.wallet.utils.views.TextInputLayoutUtil textInputLayoutUtil, String hint, int inputType) {
        textInputLayoutUtil.setHint(hint);
        textInputLayoutUtil.setVisibility(View.GONE);
        textInputLayoutUtil.setInputType(inputType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}