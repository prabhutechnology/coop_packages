package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.model;

import io.reactivex.annotations.NonNull;

public class Product {
    private String serviceLabel;
    private String id;
    private String name;
    private String label;
    private String imageUrl;
    private String minTransactionAmount;
    private String maxTransactionAmount;

    public Product(String serviceLabel, String id, String name, String label, String imageUrl, String minTransactionAmount, String maxTransactionAmount) {
        this.serviceLabel = serviceLabel;
        this.id = id;
        this.name = name;
        this.label = label;
        this.imageUrl = imageUrl;
        this.minTransactionAmount = minTransactionAmount;
        this.maxTransactionAmount = maxTransactionAmount;
    }

    public String getServiceLabel() {
        return serviceLabel;
    }

    public void setServiceLabel(String serviceLabel) {
        this.serviceLabel = serviceLabel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMinTransactionAmount() {
        return minTransactionAmount;
    }

    public void setMinTransactionAmount(String minTransactionAmount) {
        this.minTransactionAmount = minTransactionAmount;
    }

    public String getMaxTransactionAmount() {
        return maxTransactionAmount;
    }

    public void setMaxTransactionAmount(String maxTransactionAmount) {
        this.maxTransactionAmount = maxTransactionAmount;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", minTransactionAmount='" + minTransactionAmount + '\'' +
                ", maxTransactionAmount='" + maxTransactionAmount + '\'' +
                '}';
    }
}
