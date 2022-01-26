package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.topup;

import static android.app.Activity.RESULT_OK;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.FAILED;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.HIT_API;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.INVALID_AMOUNT;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.INVALID_PHONE;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.INVALID_PHONE_LENGTH;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.REQUIRED;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.SUCCESS;
import static com.prabhutech.coop.wallet.constants.ValidationMessage.VALIDATE;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.utils.PaymentWall;
import com.prabhutech.prabhupackages.databinding.FragmentTopUpBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.form.FormFragmentDirections;
import com.prabhutech.prabhupackages.wallet.core.UserCore;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;
import com.prabhutech.prabhupackages.wallet.utils.views.ContactInterface;
import com.prabhutech.prabhupackages.wallet.utils.views.DynamicAnimButton;
import com.prabhutech.prabhupackages.wallet.utils.views.DynamicTextInputPhoneLayout;

import java.util.HashMap;

import io.reactivex.observers.DisposableObserver;

public class TopUpFragment extends Fragment implements ContactInterface {
    private static final String TAG = "TopUpFragment";
    private FragmentTopUpBinding binding;
    private com.prabhutech.coop.wallet.activities.landingactivity.fragments.topup.TopUpViewModel topUpViewModel;

    //Top up check mobile API response
    private String productId = "";
    private String gateway = "";
    private String subscriptionType = "";
    private String validationMessageMobileNumber = REQUIRED;
    private String validationMessageAmount = REQUIRED;

    private View view;
    private final NavigationRedirection navigationRedirection;

    public TopUpFragment() {
        navigationRedirection = new NavigationRedirection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTopUpBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
        Fragment parentFragment = null;
        if (navHostFragment != null) {
            parentFragment = navHostFragment.getParentFragment();
        }
        if (parentFragment != null) {
            view = parentFragment.getView();
        }

        /*Dynamic Text Input Layout for phone number*/
        DynamicTextInputPhoneLayout dynamicTextInputPhoneLayout = new DynamicTextInputPhoneLayout(
                requireContext(),
                binding.textInputLayoutDynamicPhoneNumber.textInputLayoutDynamic,
                binding.textInputLayoutDynamicPhoneNumber.textInputEditTextDynamic,
                binding.textInputLayoutDynamicPhoneNumber.imageButtonOwnNumber,
                binding.textInputLayoutDynamicPhoneNumber.imageButtonShowContact
        );
        dynamicTextInputPhoneLayout.setOwnPhoneNumberInEditText();
        dynamicTextInputPhoneLayout.showPhoneContactList();
        dynamicTextInputPhoneLayout.onContactButtonClick(this);

        DynamicAnimButton dynamicAnimButton = new DynamicAnimButton(
                requireContext(),
                binding.btnTopUpNow.animButton,
                binding.btnTopUpNow.animProgressBar
        );
        dynamicAnimButton.setBusy(false);

        /*View model setup*/
        topUpViewModel = new ViewModelProvider(this).get(com.prabhutech.coop.wallet.activities.landingactivity.fragments.topup.TopUpViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setTopUpViewModel(topUpViewModel);

        /*Observer for phone number validation*/
        topUpViewModel.getPhoneNumberValidation().observe(getViewLifecycleOwner(), validationMessage -> {
            validationMessageMobileNumber = validationMessage;
            showMobileNumberErrorMessage(validationMessage);
        });

        /*Observer for amount validation*/
        topUpViewModel.getAmountValidation().observe(getViewLifecycleOwner(), validationMessage -> {
            validationMessageAmount = validationMessage;
            showAmountErrorMessage(validationMessage);
        });

        /*Observer for empty field*/
        topUpViewModel.getCheckEmptyField().observe(getViewLifecycleOwner(), emptyField -> {
            if (emptyField) {
                dynamicAnimButton.setBusy(true);
                binding.textInputLayoutDynamicPhoneNumber.textInputLayoutDynamic.setError(null);
                binding.textInputLayoutDynamicAmount.dynamicTextInputNormalLayout.setError(null);
                showPaymentConformation(dynamicAnimButton, topUpViewModel.getMobileNumber().getValue(), topUpViewModel.getAmount().getValue());
            } else {
                showMobileNumberErrorMessage(validationMessageMobileNumber);
                showAmountErrorMessage(validationMessageAmount);
            }
        });

        return binding.getRoot();
    }

    private void showPaymentConformation(DynamicAnimButton dynamicAnimButton, String mobileNumber, String amount) {
        new PaymentWall.Builder(requireContext())
                .setAmount(amount)
                .setDescription(String.format("Pay %s for the Phone Topup ?", amount))
                .build()
                .submit(new PaymentWall.Callback() {
                    @Override
                    public void onSuccess() {
                        hitTopUpAPI(mobileNumber, amount);
                    }

                    @Override
                    public void onComplete() {
                        dynamicAnimButton.setBusy(false);
                    }
                });
    }

    private void showAmountErrorMessage(String validationMessage) {
        switch (validationMessage) {
            case INVALID_AMOUNT:
            case INVALID_PHONE_LENGTH:
            case REQUIRED:
                binding.textInputLayoutDynamicAmount.dynamicTextInputNormalLayout.setError(validationMessage);
                break;
            case VALIDATE:
                binding.textInputLayoutDynamicAmount.dynamicTextInputNormalLayout.setError(null);
                break;
        }
    }

    private void showMobileNumberErrorMessage(String validationMessage) {
        switch (validationMessage) {
            case INVALID_PHONE:
            case INVALID_PHONE_LENGTH:
                binding.textInputLayoutDynamicPhoneNumber.textInputLayoutDynamic.setError(validationMessage);
                break;
            case REQUIRED:
                binding.cardViewSimType.setVisibility(View.GONE);
                binding.textInputLayoutDynamicPhoneNumber.textInputLayoutDynamic.setError(validationMessage);
                break;
            case VALIDATE:
                binding.textInputLayoutDynamicPhoneNumber.textInputLayoutDynamic.setError(null);
                break;
            case HIT_API:
                binding.textInputLayoutDynamicPhoneNumber.textInputLayoutDynamic.setError(null);
                checkMobileNumberSubscription(topUpViewModel.getMobileNumber().getValue());
                break;
        }
    }

    private void checkMobileNumberSubscription(String mobileNumber) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MobileNumber", mobileNumber);
        UserRepo.checkMobileNoType(requireContext(), jsonObject).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                JsonObject resultCommon = jsonObject.get("ResultCommon").getAsJsonObject();
                if (resultCommon != null) {
                    String subscription = resultCommon.get("Subscription").getAsString();
                    String operator = resultCommon.get("Operator").getAsString();
                    String simType = operator + " " + subscription;
                    binding.cardViewSimType.setVisibility(View.VISIBLE);
                    binding.textViewSimType.setText(simType);

                    productId = resultCommon.get("ProductId").getAsString();
                    gateway = resultCommon.get("Gateway").getAsString();
                    subscriptionType = resultCommon.get("SubscriptionType").getAsString();
                } else {
                    String serviceNotAvailable = "Service not available";
                    binding.cardViewSimType.setVisibility(View.VISIBLE);
                    binding.textViewSimType.setText(serviceNotAvailable);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
                binding.textInputLayoutDynamicPhoneNumber.textInputLayoutDynamic.setError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void hitTopUpAPI(String mobileNumber, String amount) {
        HashMap<String, String> accountDetail = UserPref.getAccountDetail(requireContext());
        String sharedPrefAccountNo = accountDetail.get(UserPref.ACCOUNT_NO_KEY);
        Log.e(TAG, "hitTopUpAPI: ProductId: " + productId + " Gateway: " + gateway +
                " SubscriptionType: " + subscriptionType + " Amount: " + amount +
                " Request ID: " + mobileNumber + " Account No: " + sharedPrefAccountNo + " CustomerID " + UserCore.customerId);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("CustomerId", UserCore.customerId);
        jsonObject.addProperty("AccountNo", sharedPrefAccountNo);
        jsonObject.addProperty("ProductId", productId);
        jsonObject.addProperty("Gateway", Integer.parseInt(gateway));
        jsonObject.addProperty("SubscriptionType", Integer.parseInt(subscriptionType));
        jsonObject.addProperty("Amount", amount);
        jsonObject.addProperty("Request_Id", mobileNumber);
        UserRepo.cbsMobileTopUp(requireContext(), jsonObject).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                Log.e(TAG, "onNext: " + jsonObject.toString());
                JsonObject result = jsonObject.get("Result").getAsJsonObject();
                String message = result.get("Message").getAsString();
                navigateToResultFragment(SUCCESS, message);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
                navigateToResultFragment(FAILED, e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        });
    }

    private void navigateToResultFragment(String resultStatus, String resultMessage) {
        FormFragmentDirections.ActionFormToShowResult actionFormToShowResult =
                FormFragmentDirections.actionFormToShowResult(resultStatus, resultMessage);
        navigationRedirection.navigateToFragmentWithAction(view, actionFormToShowResult);
    }

    @Override
    public void onContactClick() {
        Intent contactPicker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPicker, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            onContactFetch(requestCode, resultCode, data);
        }
    }

    private void onContactFetch(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Cursor cursor;
                try {
                    String phoneNo;
                    String name = null;

                    Uri uri = data.getData();
                    cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    phoneNo = cursor.getString(phoneIndex);
                    name = cursor.getString(nameIndex);
                    phoneNo = phoneNo.replaceAll("[\\D]", "");

                    if (phoneNo.length() > 10) {
                        switch (phoneNo.length()) {

                            case 13: //case for mobile with 977 infornt
                                String phoneCheck = phoneNo.substring(0, 3);
                                //Toast.makeText(this, phoneCheck, Toast.LENGTH_SHORT).show();
                                phoneNo = phoneNo.replace(phoneCheck, "");
                                //Toast.makeText(this, phoneNo, Toast.LENGTH_SHORT).show();
                                break;

                            case 11: //case for landline with 977 1 infront
                                String phoneCheckLandline = phoneNo.substring(0, 3);
                                phoneNo = phoneNo.replace(phoneCheckLandline, "0");
                                break;

                            default: //case when number is impossible to figure out
                                phoneNo = phoneNo;
                                break;
                        }
                    }
                    //Toast.makeText(this, phoneNo, Toast.LENGTH_SHORT).show();
                    binding.textInputLayoutDynamicPhoneNumber.textInputEditTextDynamic.setText(phoneNo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("Failed", "Not able to pick contact");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}