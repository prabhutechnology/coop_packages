package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.statement;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.AccountListViewBinding;
import com.prabhutech.prabhupackages.databinding.FragmentStatementBinding;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model.AccountType;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.statement.adapter.StatementAdapter;
import com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.statement.model.AccountDetailsModel;
import com.prabhutech.prabhupackages.wallet.core.api.utils.JsonUtils;
import com.prabhutech.prabhupackages.wallet.core.prefs.UserPref;
import com.prabhutech.prabhupackages.wallet.core.repo.UserRepo;
import com.prabhutech.prabhupackages.wallet.core.utils.ProgressDialogUtils;
import com.prabhutech.prabhupackages.wallet.utils.DebugMode;
import com.prabhutech.prabhupackages.wallet.utils.NavigationRedirection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

public class StatementFragment extends Fragment {
    private static final String TAG = "StatementFragment";

    private FragmentStatementBinding binding;

    private String savedAccountName, savedAccountType, savedAccountNumber, savedBalance;
    private List<AccountDetailsModel> detailsModels = new ArrayList<>();

    private ProgressDialog progressDialog;
    private Dialog accountDialog;

    private final ProgressDialogUtils progressDialogUtils;
    private final NavigationRedirection navigationRedirection;
    private final DebugMode debugMode;

    public StatementFragment() {
        progressDialogUtils = new ProgressDialogUtils();
        navigationRedirection = new NavigationRedirection();
        debugMode = new DebugMode();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatementBinding.inflate(inflater, container, false);

        //Shared pref for account detail
        HashMap<String, String> accountDetail = UserPref.getAccountDetail(requireContext());

        savedAccountName = accountDetail.get(UserPref.ACCOUNT_NAME_KEY);
        savedAccountType = accountDetail.get(UserPref.ACCOUNT_TYPE_KEY);
        savedAccountNumber = accountDetail.get(UserPref.ACCOUNT_NO_KEY);
        savedBalance = accountDetail.get(UserPref.ACCOUNT_BALANCE_KEY);

        progressDialog = new ProgressDialog(requireContext());
        progressDialogUtils.showProgressDialog(progressDialog);

        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String toDate = simpleDateFormat.format(currentDate);
        binding.textInputLayoutDateTo.setText(toDate);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date newDate = calendar.getTime();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String fromDate = simpleDateFormat2.format(newDate);
        binding.textInputLayoutDateFrom.setText(fromDate);

        getStatement(toDate, fromDate);

//        List<AccountType> accountTypeList = (List<AccountType>) intent.getSerializableExtra("ACCOUNT_TYPE_LIST");
        List<AccountType> accountTypeList = (List<AccountType>) getArguments().getSerializable("ACCOUNT_TYPE_LIST");

        binding.containerAccountDetail.setOnClickListener(v -> {
            LayoutInflater layoutInflater = (LayoutInflater) requireContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View view1 = layoutInflater.inflate(R.layout.account_list, null);
            accountDialog = new Dialog(requireContext());
            RecyclerView recyclerView = view1.findViewById(R.id.accountRecyclerView);
            ImageView closeImage = view1.findViewById(R.id.closeImage);

            closeImage.setOnClickListener(v2 -> accountDialog.dismiss());

            recyclerView.hasFixedSize();
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            AccountTypeDialog accountTypeDialog = new AccountTypeDialog(accountTypeList);

            recyclerView.setAdapter(accountTypeDialog);
            accountDialog.setContentView(view1);
            accountDialog.setCancelable(false);
            accountDialog.setCanceledOnTouchOutside(false);
            accountDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            accountDialog.show();
        });

        binding.textViewAccountHolderName.setText(savedAccountName);
        binding.textViewAccountType.setText(savedAccountType);
        binding.textViewAccountNo.setText(savedAccountNumber);

        binding.recyclerViewStatement.hasFixedSize();
        binding.recyclerViewStatement.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.textInputLayoutDateFrom.setDrawableRight(R.drawable.ic_calendar);
        binding.textInputLayoutDateTo.setDrawableRight(R.drawable.ic_calendar);

        binding.btnSubmit.setOnClickListener(v -> {
            if (binding.textInputLayoutDateTo.getText().isEmpty() && binding.textInputLayoutDateFrom.getText().isEmpty()) {
                Toast.makeText(requireContext(), "DATE NOT SELECTED", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialogUtils.showProgressDialog(progressDialog);
            getStatement(binding.textInputLayoutDateTo.getText(), binding.textInputLayoutDateFrom.getText());
        });

        binding.closingBalanceAmount.setOnClickListener(v -> {
            if (binding.viewSearchAccountDetails.getVisibility() == View.VISIBLE) {
                binding.viewSearchAccountDetails.setVisibility(View.GONE);
                binding.cardViewSearchFilter.setVisibility(View.GONE);
            } else {
                binding.viewSearchAccountDetails.setVisibility(View.VISIBLE);
                binding.cardViewSearchFilter.setVisibility(View.VISIBLE);
            }
        });

        binding.goBack.setOnClickListener(navigationRedirection::navigateBack);

        return binding.getRoot();
    }

    private void getStatement(String toDate, String fromDate) {
        detailsModels.clear();

        String closingDate = fromDate + " To " + toDate;
        binding.textForDate.setText(closingDate);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AccountNo", savedAccountNumber);
        jsonObject.addProperty("FromDate", fromDate);
        jsonObject.addProperty("ToDate", toDate);

        UserRepo.getAccountStatement(requireContext(), jsonObject).subscribe(new DisposableObserver<JsonObject>() {
            @Override
            public void onNext(@NonNull JsonObject jsonObject) {
//                Log.e(TAG, "onNext: getAccountStatement: " + jsonObject);
                debugMode.showLog(TAG, "onNext: getAccountStatement: " + jsonObject);
                if (!jsonObject.get("AccountInfo").isJsonNull()) {
                    JsonObject accountInfo = jsonObject.get("AccountInfo").getAsJsonObject();
                    if (!accountInfo.isJsonNull()) {
                        String balance = accountInfo.get("Balance").getAsString();
                        if (balance != null) {
                            if (!balance.isEmpty()) {
                                binding.closingBalanceAmount.setText(String.format("NPR %s", balance));
                            } else {
                                binding.closingBalanceAmount.setText(String.format("NPR %s", "0.00"));
                            }
                        } else {
                            binding.closingBalanceAmount.setText(String.format("NPR %s", "0.00"));
                        }
                    } else {
                        binding.closingBalanceAmount.setText(String.format("NPR %s", "0.00"));
                    }
                } else binding.closingBalanceAmount.setText(String.format("NPR %s", "0.00"));

                if (!jsonObject.get("ResultCommon").isJsonNull()) {
                    JsonArray resultCommon = jsonObject.get("ResultCommon").getAsJsonArray();
                    if (resultCommon != null) {
                        if (resultCommon.size() > 0) {
                            binding.emptylist.setVisibility(View.GONE);
                            binding.recyclerViewStatement.setVisibility(View.VISIBLE);
                            binding.cardViewSearchFilter.setVisibility(View.VISIBLE);

//                        JsonObject balance = jsonObject.get("AccountInfo").getAsJsonObject();
                            for (JsonElement element : resultCommon) {
                                JsonObject obj = element.getAsJsonObject();
                                AccountDetailsModel digital;
                              /*  AccountDetailsModel digital = new AccountDetailsModel();
                                digital.Date = JsonUtils.safeString(obj.get("Date"),"");
                                digital.VoucherNo = JsonUtils.safeString(obj.get("VoucherNo"),"");
                                digital.Type = JsonUtils.safeString(obj.get("Type"),"");
                                digital.Particular = JsonUtils.safeString(obj.get("Particular"),"");
                                digital.Debit = JsonUtils.safeString(obj.get("Debit"),"");
                                digital.Credit = JsonUtils.safeString(obj.get("Credit"),"");*/
                                try {
                                    digital = JsonUtils.gson.fromJson(obj, new TypeToken<AccountDetailsModel>() {
                                    }.getType());
                                    detailsModels.add(digital);
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            progressDialogUtils.dismissProgressDialog(progressDialog);
                            StatementAdapter statementAdapter = new StatementAdapter(requireContext(), detailsModels);
                            binding.recyclerViewStatement.setAdapter(statementAdapter);
                        } else {
                            binding.emptylist.setVisibility(View.VISIBLE);
                            binding.recyclerViewStatement.setVisibility(View.GONE);
                            binding.cardViewSearchFilter.setVisibility(View.GONE);
                        }
                    } else {
                        binding.emptylist.setVisibility(View.VISIBLE);
                        binding.recyclerViewStatement.setVisibility(View.GONE);
                        binding.cardViewSearchFilter.setVisibility(View.GONE);
                    }
                } else {
                    binding.emptylist.setVisibility(View.VISIBLE);
                    binding.recyclerViewStatement.setVisibility(View.GONE);
                    binding.cardViewSearchFilter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                progressDialogUtils.dismissProgressDialog(progressDialog);
//                Log.e(TAG, "onError: " + e.getMessage());
                debugMode.showLog(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                progressDialogUtils.dismissProgressDialog(progressDialog);
            }
        });
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
                accountDialog.dismiss();
                detailsModels.clear();
                progressDialogUtils.showProgressDialog(progressDialog);
                savedAccountNumber = accountType.getAccountNo();
                getStatement(binding.textInputLayoutDateTo.getText(), binding.textInputLayoutDateFrom.getText());

                binding.textViewAccountHolderName.setText(accountType.getAccountName());
                binding.textViewAccountType.setText(accountType.getAccountType());
                binding.textViewAccountNo.setText(accountType.getAccountNo());

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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}