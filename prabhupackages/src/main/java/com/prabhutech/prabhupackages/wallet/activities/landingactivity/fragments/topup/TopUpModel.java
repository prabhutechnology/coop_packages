package com.prabhutech.coop.wallet.activities.landingactivity.fragments.topup;

public class TopUpModel {
    private String mobileNumber;
    private String amount;

    public TopUpModel(String mobileNumber, String amount) {
        this.mobileNumber = mobileNumber;
        this.amount = amount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
