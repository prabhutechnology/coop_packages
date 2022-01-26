package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.myaccount.model;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("TransactionDate")
    private String date;

    @SerializedName("TransactionName")
    private String name;

    @SerializedName("Amount")
    private String amount;

    @SerializedName("Remarks")
    private String remarks;

    @SerializedName("TransactionId")
    private String id;

    public Transaction(String date, String name, String amount, String remarks, String id) {
        this.date = date;
        this.name = name;
        this.amount = amount;
        this.remarks = remarks;
        this.id = id;
    }

    public Transaction() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
