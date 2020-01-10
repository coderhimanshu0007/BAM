package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockModel {
    @SerializedName("Customer Name")
    @Expose
    private String customerName;
    @SerializedName("Item No")
    @Expose
    private String itemNo;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Purchaser Name")
    @Expose
    private String purchaserName;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("NOD")
    @Expose
    private String nod;

    public StockModel() {
    }

    public StockModel(String customerName, String itemNo, String description, String purchaserName, String amount, String nod) {
        this.customerName = customerName;
        this.itemNo = itemNo;
        this.description = description;
        this.purchaserName = purchaserName;
        this.amount = amount;
        this.nod = nod;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNod() {
        return nod;
    }

    public void setNod(String nod) {
        this.nod = nod;
    }
}
