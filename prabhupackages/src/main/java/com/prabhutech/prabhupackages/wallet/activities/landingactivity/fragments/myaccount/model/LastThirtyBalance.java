package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model;

import com.google.gson.annotations.SerializedName;

public class LastThirtyBalance {
    @SerializedName("Balance")
    private String balance;
    @SerializedName("Date")
    private String date;

    public LastThirtyBalance(String date, String balance) {
        this.date = date;
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
