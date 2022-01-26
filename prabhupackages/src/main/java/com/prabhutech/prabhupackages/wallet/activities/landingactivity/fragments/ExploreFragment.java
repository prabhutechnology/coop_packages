package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments;

import static com.prabhutech.prabhupackages.wallet.constants.Product.GroupName.GROUP_UTILITY_BILLS;
import static com.prabhutech.prabhupackages.wallet.constants.Product.ProductType.STATIC_PRODUCT;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentExploreBinding;
import com.prabhutech.prabhupackages.wallet.core.api.utils.JsonUtils;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.core.utils.ProgressDialogUtils;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class ExploreFragment extends Fragment {
    private static final String TAG = "ExploreFragment";
    private FragmentExploreBinding binding;

    //Variables
    private final int[] sampleImage = {R.drawable.banner_test, R.drawable.banner_test};
    private static boolean isVisible;

    //Views
    private ProgressDialog progressDialog;

    //CBS Account Info
    private final List<String> bankBranchList = new ArrayList<>();
    private final List<String> accountNoList = new ArrayList<>();
    private final List<String> accountNameList = new ArrayList<>();
    private final List<String> accountTypeList = new ArrayList<>();
    private final List<String> accountInterestRateList = new ArrayList<>();
    private final List<String> accountBalanceList = new ArrayList<>();

    //Shared preference
    private String sharedPrefBankBranch, sharedPrefAccountNo, sharedPrefAccountName, sharedPrefAccountType, sharedPrefInterestRate, sharedPrefBalance, sharedPrefPosition;

    private final ProgressDialogUtils progressDialogUtils;
    private final NavigationRedirection navigationRedirection;
    private final DebugMode debugMode;

    public ExploreFragment() {
        progressDialogUtils = new ProgressDialogUtils();
        navigationRedirection = new NavigationRedirection();
        debugMode = new DebugMode();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(requireContext());
        progressDialogUtils.showProgressDialog(progressDialog);

        HashMap<String, String> accountDetail = UserPref.getAccountDetail(requireContext());
        sharedPrefBankBranch = accountDetail.get(UserPref.BANK_BRANCH_KEY);
        sharedPrefAccountNo = accountDetail.get(UserPref.ACCOUNT_NO_KEY);
        sharedPrefAccountName = accountDetail.get(UserPref.ACCOUNT_NAME_KEY);
        sharedPrefAccountType = accountDetail.get(UserPref.ACCOUNT_TYPE_KEY);
        sharedPrefInterestRate = accountDetail.get(UserPref.ACCOUNT_INTEREST_RATE_KEY);
        sharedPrefBalance = accountDetail.get(UserPref.ACCOUNT_BALANCE_KEY);
        sharedPrefPosition = accountDetail.get(UserPref.ACCOUNT_POSITION_KEY);

        try {
            isVisible = UserPref.isBalanceVisible(requireContext());
        } catch (Exception e) {
            isVisible = false;
            e.printStackTrace();
        }

        if (isVisible) {
            binding.imageBtnVisibility.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_visibility_off_24));
        } else {
            binding.imageBtnVisibility.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_visibility_24));
        }

        getCBSCustomerDetail();

        binding.carouselView.setImageListener(((position, imageView) -> Glide.with(requireActivity()).load(sampleImage[position])
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(imageView)));

        binding.carouselView.setPageCount(sampleImage.length);

        /*Container Redirection*/
        binding.containerLoadFund.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.action_explore_to_coming_soon));
        binding.containerMyAccount.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.action_explore_to_my_account));
        binding.containerRemittance.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.action_explore_to_coming_soon));
        binding.containerWalletTransfer.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.action_explore_to_coming_soon));
        binding.containerFundTransfer.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.action_explore_to_coming_soon));
        binding.containerUtilityPay.setOnClickListener(view -> navigateToProductListFragment(view, GROUP_UTILITY_BILLS));
        binding.containerApplyLoan.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.action_explore_to_coming_soon));
        binding.containerOnlineStore.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.action_explore_to_coming_soon));
        binding.containerEvent.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.action_explore_to_coming_soon));

        return binding.getRoot();
    }

    private void navigateToProductListFragment(View view, String serviceListName) {
        com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.ExploreFragmentDirections.ActionExploreToServiceList actionExploreToProductList =
                com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.ExploreFragmentDirections.actionExploreToServiceList(serviceListName);
        navigationRedirection.navigateToFragmentWithAction(view, actionExploreToProductList);
    }

    private void clearAvailableList() {
        bankBranchList.clear();
        accountNoList.clear();
        accountNameList.clear();
        accountTypeList.clear();
        accountInterestRateList.clear();
        accountBalanceList.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        clearAvailableList();
    }

    private void getCBSCustomerDetail() {
        UserRepo.getCustomerDetail(requireContext()).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                debugMode.showLog(TAG, "onNext: get customer detail " + jsonObject);
                JsonArray cbsAccountInfo = jsonObject.get("CBSAccountInfo").getAsJsonArray();
                if (cbsAccountInfo != null) {
                    if (cbsAccountInfo.size() > 0) {
                        for (int i = 0; i < cbsAccountInfo.size(); i++) {
                            JsonObject jsonObject1 = cbsAccountInfo.get(i).getAsJsonObject();
                            String bankBranchString = JsonUtils.safeString(jsonObject1.get("BankBranch"), "");
                            String accountNoString = JsonUtils.safeString(jsonObject1.get("AccountNo"), "");
                            String accountNameString = JsonUtils.safeString(jsonObject1.get("AccountName"), "");
                            String accountTypeString = JsonUtils.safeString(jsonObject1.get("AccountType"), "");
                            String accountInterestRate = JsonUtils.safeString(jsonObject1.get("InterestRate"), "");
                            String accountBalanceString = JsonUtils.safeString(jsonObject1.get("Balance"), "");

                            bankBranchList.add(bankBranchString);
                            accountNoList.add(accountNoString);
                            accountNameList.add(accountNameString);
                            accountTypeList.add(accountTypeString);
                            accountInterestRateList.add(accountInterestRate);
                            accountBalanceList.add(accountBalanceString);

                            /*Test Data*/
                            /*bankBranchList.add("TEST");
                            accountNoList.add("N/A");
                            accountNameList.add("N/A");
                            accountTypeList.add("TEST ACCOUNT");
                            accountInterestRateList.add("9%");
                            accountBalanceList.add("23.0");*/
                        }
                    } else {
                        bankBranchList.add("No branch");
                        accountNoList.add("N/A");
                        accountNameList.add("N/A");
                        accountTypeList.add("No account");
                        accountInterestRateList.add("0%");
                        accountBalanceList.add("0.00");
                    }
                } else {
                    bankBranchList.add("No branch");
                    accountNoList.add("N/A");
                    accountNameList.add("N/A");
                    accountTypeList.add("No account");
                    accountInterestRateList.add("0%");
                    accountBalanceList.add("0.00");
                }

                debugMode.showLog(TAG, "onNext: Bank Branch: " + bankBranchList);
                debugMode.showLog(TAG, "onNext: Account No: " + accountNoList);
                debugMode.showLog(TAG, "onNext: Account Name: " + accountNameList);
                debugMode.showLog(TAG, "onNext: Account Type: " + accountTypeList);
                debugMode.showLog(TAG, "onNext: Account Interest Rate: " + accountInterestRateList);
                debugMode.showLog(TAG, "onNext: Account balance: " + accountBalanceList);

                /*Log.e(TAG, "onNext: Bank Branch: " + bankBranchList);
                Log.e(TAG, "onNext: Account No: " + accountNoList);
                Log.e(TAG, "onNext: Account Name: " + accountNameList);
                Log.e(TAG, "onNext: Account Type: " + accountTypeList);
                Log.e(TAG, "onNext: Account Interest Rate: " + accountInterestRateList);
                Log.e(TAG, "onNext: Account balance: " + accountBalanceList);*/

                ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.spinner_item_layout, accountTypeList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spAccountNo.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

                /**
                 * Check shared preference data.
                 * If not null then set selection to saved position
                 * if null then position is set to 0
                 */
                if (sharedPrefBankBranch != null && sharedPrefAccountNo != null && sharedPrefAccountName != null &&
                        sharedPrefAccountType != null && sharedPrefInterestRate != null && sharedPrefBalance != null && sharedPrefPosition != null) {
                    int position = Integer.parseInt(sharedPrefPosition);
                    binding.spAccountNo.setSelection(position);
                    binding.spAccountNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selectedAccountType = adapterView.getItemAtPosition(i).toString();
                            String selectedBankBranch = bankBranchList.get(i);
                            String selectedAccountNo = accountNoList.get(i);
                            String selectedAccountName = accountNameList.get(i);
                            String selectedInterestRate = accountInterestRateList.get(i);
                            String selectedBalance = accountBalanceList.get(i);

                            binding.textViewAccountNo.setText(selectedAccountNo);

                            debugMode.showLog(TAG, "onItemSelected: " +
                                    "\n type: " + selectedAccountType +
                                    "\n bank branch: " + selectedBankBranch + " " +
                                    "\n account no: " + selectedAccountNo +
                                    "\n account name: " + selectedAccountName +
                                    "\n interest rate: " + selectedInterestRate +
                                    "\n balance: " + selectedBalance);

                            /*Log.e(TAG, "onItemSelected: " +
                                    "\n type: " + selectedAccountType +
                                    "\n bank branch: " + selectedBankBranch + " " +
                                    "\n account no: " + selectedAccountNo +
                                    "\n account name: " + selectedAccountName +
                                    "\n interest rate: " + selectedInterestRate +
                                    "\n balance: " + selectedBalance);*/

                            UserPref.setAccountDetail(requireContext(), selectedBankBranch, selectedAccountNo, selectedAccountName, selectedAccountType, selectedInterestRate, selectedBalance, String.valueOf(i));

                            getCBSBalance(selectedAccountNo);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
//                            Log.e(TAG, "onNothingSelected: ");
                            debugMode.showLog(TAG, "onNothingSelected: ");
                        }
                    });
                } else {
                    binding.spAccountNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selectedAccountType = adapterView.getItemAtPosition(i).toString();
                            String selectedBankBranch = bankBranchList.get(i);
                            String selectedAccountNo = accountNoList.get(i);
                            String selectedAccountName = accountNameList.get(i);
                            String selectedInterestRate = accountInterestRateList.get(i);
                            String selectedBalance = accountBalanceList.get(i);

                            binding.textViewAccountNo.setText(selectedAccountNo);

                            debugMode.showLog(TAG, "onItemSelected: " +
                                    "\n type: " + selectedAccountType +
                                    "\n bank branch: " + selectedBankBranch + " " +
                                    "\n account no: " + selectedAccountNo +
                                    "\n account name: " + selectedAccountName +
                                    "\n interest rate: " + selectedInterestRate +
                                    "\n balance: " + selectedBalance);

                            /*Log.e(TAG, "onItemSelected: " +
                                    "\n type: " + selectedAccountType +
                                    "\n bank branch: " + selectedBankBranch + " " +
                                    "\n account no: " + selectedAccountNo +
                                    "\n account name: " + selectedAccountName +
                                    "\n interest rate: " + selectedInterestRate +
                                    "\n balance: " + selectedBalance);*/

                            UserPref.setAccountDetail(requireContext(), selectedBankBranch, selectedAccountNo, selectedAccountName, selectedAccountType, selectedInterestRate, selectedBalance, String.valueOf(i));

                            getCBSBalance(selectedAccountNo);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
//                            Log.e(TAG, "onNothingSelected: ");
                            debugMode.showLog(TAG, "onNothingSelected: ");
                        }
                    });
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                try {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception es) {
//                    Log.e(TAG, "onError: " + es.getMessage());
                    debugMode.showLog(TAG, "onError: " + es.getMessage());
                }
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void getCBSBalance(String selectedAccountNo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AccountNo", selectedAccountNo);
        UserRepo.getCBSBalance(requireContext(), jsonObject).subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {
//                Log.e(TAG, "onNext: GET BALANCE " + s);
                debugMode.showLog(TAG, "onNext: GET BALANCE " + s);
                progressDialogUtils.dismissProgressDialog(progressDialog);
                if (!isVisible) {
                    if (s.isEmpty()) {
                        binding.textViewAccountBalanceNpr.setText("0.00");
                    } else {
                        binding.textViewAccountBalanceNpr.setText(s);
                    }
                } else {
                    binding.textViewAccountBalanceNpr.setText(R.string.xxx_xx);
                }

                binding.imageBtnVisibility.setOnClickListener(btnView -> {
                    if (isVisible) {
                        isVisible = false;
                        if (s.isEmpty()) {
                            binding.textViewAccountBalanceNpr.setText("0.00");
                        } else {
                            binding.textViewAccountBalanceNpr.setText(s);
                        }
                        binding.imageBtnVisibility.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_visibility_24));
                    } else {
                        isVisible = true;
                        binding.textViewAccountBalanceNpr.setText(R.string.xxx_xx);
                        binding.imageBtnVisibility.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_visibility_off_24));
                    }
                    UserPref.setBalanceVisibility(requireContext(), isVisible);
                });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialogUtils.dismissProgressDialog(progressDialog);
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
}