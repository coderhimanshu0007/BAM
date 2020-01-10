package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillingModel {
    @SerializedName("PurchaserName")
    @Expose
    private String purchaserName;
    @SerializedName("SO No")
    @Expose
    private String soNo;
    @SerializedName("Customer Name")
    @Expose
    private String customerName;
    @SerializedName("Count")
    @Expose
    private String count;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("EDD Date")
    @Expose
    private String eddDate;
    @SerializedName("Sales Coardinator Name")
    @Expose
    private String salesCoardinatorName;

    public BillingModel() {
    }

    public BillingModel(String purchaserName, String soNo, String customerName, String count, String amount, String eddDate, String salesCoardinatorName) {
        this.purchaserName = purchaserName;
        this.soNo = soNo;
        this.customerName = customerName;
        this.count = count;
        this.amount = amount;
        this.eddDate = eddDate;
        this.salesCoardinatorName = salesCoardinatorName;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEddDate() {
        return eddDate;
    }

    public void setEddDate(String eddDate) {
        this.eddDate = eddDate;
    }

    public String getSalesCoardinatorName() {
        return salesCoardinatorName;
    }

    public void setSalesCoardinatorName(String salesCoardinatorName) {
        this.salesCoardinatorName = salesCoardinatorName;
    }
}
