package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model;

import java.io.Serializable;

public class AccountType implements Serializable {
    private String bankBranch;
    private String accountNo;
    private String accountName;
    private String accountType;
    private String interestRate;
    private String accountBalance;

    public AccountType(String bankBranch, String accountNo, String accountName, String accountType, String interestRate, String accountBalance) {
        this.bankBranch = bankBranch;
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.accountBalance = accountBalance;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }
}
