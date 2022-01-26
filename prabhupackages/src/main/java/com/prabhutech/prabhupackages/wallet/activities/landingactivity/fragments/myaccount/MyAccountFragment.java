package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.AccountListBinding;
import com.prabhutech.prabhupackages.databinding.AccountListViewBinding;
import com.prabhutech.prabhupackages.databinding.FragmentMyAccountBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.adapter.MyAccountAdapter;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model.AccountType;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model.LastThirtyBalance;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model.Transaction;
import com.prabhutech.prabhupackages.wallet.core.api.utils.JsonUtils;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.core.utils.ProgressDialogUtils;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class MyAccountFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MyAccountFragment";

    //Binding
    private FragmentMyAccountBinding binding;

    //Dialog
    private ProgressDialog progressDialog;
    private Dialog accountDialog;
    private final ProgressDialogUtils progressDialogUtils;

    //Adapter
    private MyAccountAdapter myAccountAdapter;

    //Variables
    private List<LastThirtyBalance> lastThirtyBalances = new ArrayList<>();
    private List<AccountType> accountTypeList = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    private final NavigationRedirection navigationRedirection;
    private final DebugMode debugMode;

    public MyAccountFragment() {
        progressDialogUtils = new ProgressDialogUtils();
        navigationRedirection = new NavigationRedirection();
        debugMode = new DebugMode();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountBinding.inflate(inflater, container, false);

        binding.requestChequeContainer.setOnClickListener(view -> navigationRedirection.navigateToFragmentWithAction(view, R.id.action_my_account_to_request_cheque));
        binding.statementContainer.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("ACCOUNT_TYPE_LIST", (Serializable) accountTypeList);
            navigationRedirection.navigateWithBundle(view, R.id.action_my_account_to_statement, bundle);
        });

        return binding.getRoot();
    }

    private void getAccountTypeDetail(MyAccountFragment myAccountFragment) {
        //Shared pref for Account Details
        HashMap<String, String> accountDetail = UserPref.getAccountDetail(requireContext());

        //Variables
        String savedAccountNo = accountDetail.get(UserPref.ACCOUNT_NO_KEY);
        String savedAccountName = accountDetail.get(UserPref.ACCOUNT_NAME_KEY);
        String savedAccountType = accountDetail.get(UserPref.ACCOUNT_TYPE_KEY);
        /*String savedBankBranch = accountDetail.get(UserPref.BANK_BRANCH_KEY);
        String savedAccountInterestRate = accountDetail.get(UserPref.ACCOUNT_INTEREST_RATE_KEY);
        String savedAccountBalance = accountDetail.get(UserPref.ACCOUNT_BALANCE_KEY);*/

        binding.textViewAccountName.setText(savedAccountName);
        binding.textViewUsername.setText(savedAccountType);
        binding.textViewAccountNo.setText(savedAccountNo);

        UserRepo.getCustomerDetail(requireContext()).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                JsonArray cbsAccountInfo = jsonObject.get("CBSAccountInfo").getAsJsonArray();
                accountTypeList.clear();
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

                            AccountType accountType = new AccountType(bankBranchString, accountNoString, accountNameString, accountTypeString, accountInterestRate, accountBalanceString);
                            accountTypeList.add(accountType);
                        }
                    } else {
                        AccountType accountType = new AccountType("No branch", "N/A", "N/A", "No account", "0.00%", "0.00");
                        accountTypeList.add(accountType);
                    }
                } else {
                    AccountType accountType = new AccountType("No branch", "N/A", "N/A", "No account", "0.00%", "0.00");
                    accountTypeList.add(accountType);
                }

                /*Test Data*/
                /*AccountType accountTypeDemoData = new AccountType("TEST", "N/A", "N/A", "TEST ACCOUNT", "9%", "23.0");
                accountTypeList.add(accountTypeDemoData);*/

                binding.accountTypeContainer.setOnClickListener(v -> {
                    LayoutInflater layoutInflater = (LayoutInflater) requireContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    AccountListBinding binding = AccountListBinding.inflate(layoutInflater);
                    accountDialog = new Dialog(requireContext());

                    binding.closeImage.setOnClickListener(v2 -> accountDialog.dismiss());

                    binding.accountRecyclerView.hasFixedSize();
                    binding.accountRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    AccountTypeDialog accountTypeDialog = new AccountTypeDialog(accountTypeList);

                    binding.accountRecyclerView.setAdapter(accountTypeDialog);
                    accountDialog.setContentView(binding.getRoot());
                    accountDialog.setCancelable(false);
                    accountDialog.setCanceledOnTouchOutside(false);
                    accountDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    accountDialog.show();
                });

                getBalance(myAccountFragment, savedAccountNo);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                AccountType accountType = new AccountType("No branch", "N/A", "N/A", "No account", "0.00%", "0.00");
                accountTypeList.add(accountType);
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getBalance(MyAccountFragment myAccountFragment, String savedAccountNo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AccountNo", savedAccountNo);
        UserRepo.getAccountBalance(requireContext(), jsonObject).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                JsonArray resultCommonList = jsonObject.get("ResultCommon").getAsJsonArray();
                if (resultCommonList != null) {
                    if (resultCommonList.size() > 0) {
//                        Log.e(TAG, "getAccountBalance: " + resultCommonList.toString());
                        debugMode.showLog(TAG, "getAccountBalance: " + resultCommonList.toString());
                        String availableBalance = resultCommonList.get(0).getAsJsonObject().get("AvailableBalance").getAsString();
                        String actualBalance = resultCommonList.get(0).getAsJsonObject().get("ActualBalance").getAsString();
                        String accruedInterest = resultCommonList.get(0).getAsJsonObject().get("AccruedInterest").getAsString();
                        String interestRate = resultCommonList.get(0).getAsJsonObject().get("InterestRate").getAsString();

                        //For available balance
                        nullEmptyStringHandling(availableBalance, binding.textViewAmount, binding.textViewAccountNpr);

                        //For actual balance
                        nullEmptyStringHandling(actualBalance, binding.textViewActualBalanceAmount, binding.textViewActualBalanceNpr);

                        //For accrued interest
                        nullEmptyStringHandling(accruedInterest, binding.textViewAccruedInterestAmount, binding.textViewAccruedInterestNpr);

                        if (interestRate != null) {
                            if (interestRate.isEmpty()) {
                                binding.textViewInterestRate.setText("0%");
                            } else {
                                binding.textViewInterestRate.setText(String.format("%s%%", interestRate));
                            }
                        } else {
                            binding.textViewInterestRate.setText("0%");
                        }
                    } else {
                        setBalancesToNA();
                    }
                } else {
                    setBalancesToNA();
                }

                //After fetching data from GetCBSAccountBalance
                getLastThirtyDayBalance(myAccountFragment, savedAccountNo);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "onError: " + e.getMessage());
                debugMode.showLog(TAG, "onError: " + e.getMessage());
                setBalancesToNA();
                progressDialogUtils.dismissProgressDialog(progressDialog);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void getLastThirtyDayBalance(MyAccountFragment myAccountFragment, String
            savedAccountNo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AccountNo", savedAccountNo);
//        Log.e(TAG, "getLastThirtyDayBalance: savedAccountNo: " + savedAccountNo);
        debugMode.showLog(TAG, "getLastThirtyDayBalance: savedAccountNo: " + savedAccountNo);
        UserRepo.getLastThirtyDayBalance(requireContext(), jsonObject).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
//                Log.e(TAG, "onNext: getLastThirtyDayBalance" + jsonObject.toString());
                debugMode.showLog(TAG, "onNext: getLastThirtyDayBalance" + jsonObject.toString());
                if (!jsonObject.get("ResultCommon").isJsonNull()) {
                    JsonArray resultCommon = jsonObject.get("ResultCommon").getAsJsonArray();
                    if (resultCommon != null) {
//                        Log.e(TAG, "onNext: " + resultCommon.toString());
                        debugMode.showLog(TAG, "onNext: " + resultCommon.toString());
                        if (resultCommon.size() == 0) {
                            lastThirtyBalances.clear();
                        } else {
                            lastThirtyBalances.clear();
                            for (int i = 0; i < resultCommon.size(); i++) {
                                Gson gson = new Gson();
                                JsonObject jsonObject1 = resultCommon.get(i).getAsJsonObject();
                                LastThirtyBalance lastThirtyBalance = gson.fromJson(jsonObject1, LastThirtyBalance.class);
                                lastThirtyBalances.add(lastThirtyBalance);
                            }
                        }
                    } else {
                        lastThirtyBalances.clear();
                    }
                } else {
                    lastThirtyBalances.clear();
                }

                getLastSevenTransaction(myAccountFragment, savedAccountNo);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                progressDialogUtils.dismissProgressDialog(progressDialog);
                lastThirtyBalances.clear();
//                Log.e(TAG, "onError: " + e.getMessage());
                debugMode.showLog(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }


    private void getLastSevenTransaction(MyAccountFragment myAccountFragment, String
            savedAccountNumber) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AccountNo", savedAccountNumber);
        UserRepo.getLastSevenTransaction(requireContext(), jsonObject).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
                progressDialogUtils.dismissProgressDialog(progressDialog);
//                Log.e(TAG, "onNext: " + jsonObject.toString());
                debugMode.showLog(TAG, "onNext: " + jsonObject.toString());
                if (!jsonObject.get("ResultCommon").isJsonNull()) {
                    JsonArray resultCommon = jsonObject.get("ResultCommon").getAsJsonArray();
                    if (resultCommon != null) {
                        if (resultCommon.size() > 0) {
                            for (int i = 0; i < resultCommon.size(); i++) {
                                JsonObject resultCommonObject = resultCommon.get(i).getAsJsonObject();
                                String date = JsonUtils.safeString(resultCommonObject.get("TransactionDate"), " ");
                                String name = JsonUtils.safeString(resultCommonObject.get("TransactionName"), " ");
                                String amount = JsonUtils.safeString(resultCommonObject.get("Amount"), " ");
                                String remarks = JsonUtils.safeString(resultCommonObject.get("Remarks"), " ");
                                String id = JsonUtils.safeString(resultCommonObject.get("TransactionId"), " ");

                                Transaction transaction = new Transaction(date, name, amount, remarks, id);
                                transactions.add(transaction);
                            }
                        } else {
                            transactions.clear();
                        }
                    } else {
                        transactions.clear();
                    }
                } else
                    transactions.clear();

                myAccountAdapter = new MyAccountAdapter(getChildFragmentManager(), requireContext(), lastThirtyBalances, transactions);
                binding.viewPagerMyAccountChartDetails.setAdapter(myAccountAdapter);
                binding.viewPagerMyAccountChartDetails.addOnPageChangeListener(myAccountFragment);

                binding.radioGroupChartDetail.setOnCheckedChangeListener((radioGroup, i) -> {
                    if (i == binding.radioBtnChart.getId()) {
                        binding.viewPagerMyAccountChartDetails.setCurrentItem(0);
                    } else if (i == binding.radioBtnDetail.getId()) {
                        binding.viewPagerMyAccountChartDetails.setCurrentItem(1);
                    }
                });
            }

            @Override
            public void onError(@NonNull Throwable e) {
//                Log.e(TAG, "onError: " + e.getMessage());
                debugMode.showLog(TAG, "onError: " + e.getMessage());
                progressDialogUtils.dismissProgressDialog(progressDialog);
                transactions.clear();
            }

            @Override
            public void onComplete() {
                progressDialogUtils.dismissProgressDialog(progressDialog);
            }
        });
    }

    /**
     * Error handling
     * For null, empty string and 0
     *
     * @param balance     - includes account balance, actual balance and accrued balance
     * @param textView    - textview to set text accordingly
     * @param textViewNPR - textview to set visibility
     */
    private void nullEmptyStringHandling(String balance, TextView textView, TextView
            textViewNPR) {
        //For null
        if (balance != null) {
            //For empty string
            if (balance.isEmpty()) {
                textView.setText("N/A");
                textViewNPR.setVisibility(View.GONE);
            } else {
                textView.setText(balance);
                textViewNPR.setVisibility(View.VISIBLE);
            }
        } else {
            textView.setText("N/A");
            textViewNPR.setVisibility(View.GONE);
        }
    }

    /**
     * Set balances to N/A
     */
    private void setBalancesToNA() {
        //Account balance
        binding.textViewAmount.setText("N/A");
        binding.textViewAccountNpr.setVisibility(View.GONE);

        //Actual balance
        binding.textViewActualBalanceAmount.setText("N/A");
        binding.textViewActualBalanceNpr.setVisibility(View.GONE);

        //Accrued balance
        binding.textViewAccruedInterestAmount.setText("N/A");
        binding.textViewAccruedInterestNpr.setVisibility(View.GONE);

        //Interest Rate
        binding.textViewInterestRate.setText(String.format("%s%%", 0));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        binding.radioGroupChartDetail.check(binding.radioGroupChartDetail.getChildAt(position).getId());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class AccountTypeDialog extends RecyclerView.Adapter<AccountTypeDialog.AccountTypeViewHolder> {
        private final List<AccountType> accountTypeList;

        public AccountTypeDialog(List<AccountType> accountTypeList) {
            this.accountTypeList = accountTypeList;
        }

        @NonNull
        @Override
        public AccountTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AccountTypeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.account_list_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AccountTypeViewHolder holder, int position) {
            AccountType accountType = accountTypeList.get(position);
            holder.binding.accountType.setText(accountType.getAccountType());
            holder.binding.accountHolderName.setText(accountType.getAccountName());
            holder.binding.accountNumber.setText(accountType.getAccountNo());
            if ((accountTypeList.size() - 1) == position) {
                holder.binding.divider.setVisibility(View.GONE);
            }

            holder.binding.accountCardHolder.setOnClickListener(view -> {
                progressDialogUtils.showProgressDialog(progressDialog);

                accountDialog.dismiss();
//                Log.e(TAG, "onBindViewHolder: " + accountType.getAccountNo());
                debugMode.showLog(TAG, "onBindViewHolder: " + accountType.getAccountNo());

                UserPref.setAccountDetail(
                        requireContext(),
                        accountType.getBankBranch(),
                        accountType.getAccountNo(),
                        accountType.getAccountName(),
                        accountType.getAccountType(),
                        accountType.getInterestRate(),
                        accountType.getAccountBalance(),
                        String.valueOf(position)
                );

                getAccountTypeDetail(MyAccountFragment.this);
            });
        }

        @Override
        public int getItemCount() {
            return accountTypeList.size();
        }

        public class AccountTypeViewHolder extends RecyclerView.ViewHolder {
            private final AccountListViewBinding binding;

            public AccountTypeViewHolder(@NonNull AccountListViewBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Show progress dialog before API hit
        progressDialog = new ProgressDialog(getContext());
        progressDialogUtils.showProgressDialog(progressDialog);
        getAccountTypeDetail(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}