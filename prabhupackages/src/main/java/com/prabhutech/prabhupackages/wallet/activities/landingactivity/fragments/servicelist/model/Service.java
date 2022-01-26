package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.servicelist.model;

import java.util.List;

public class Service {
    private String subGroupName;
    private String subGroupLabel;
    private String subGroupImageUrl;
    private List<Product> productList;

    public Service() {
    }

    public Service(String subGroupName, String subGroupLabel, String subGroupImageUrl, List<Product> productList) {
        this.subGroupName = subGroupName;
        this.subGroupLabel = subGroupLabel;
        this.subGroupImageUrl = subGroupImageUrl;
        this.productList = productList;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }

    public String getSubGroupLabel() {
        return subGroupLabel;
    }

    public void setSubGroupLabel(String subGroupLabel) {
        this.subGroupLabel = subGroupLabel;
    }

    public String getSubGroupImageUrl() {
        return subGroupImageUrl;
    }

    public void setSubGroupImageUrl(String subGroupImageUrl) {
        this.subGroupImageUrl = subGroupImageUrl;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
