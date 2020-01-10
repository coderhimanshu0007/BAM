package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesOrderModel {
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
    @SerializedName("No of Days")
    @Expose
    private String noofDays;
    @SerializedName("Coardinator Name")
    @Expose
    private String coardinatorName;
    @SerializedName("Purchaser Remark")
    @Expose
    private String purchaserRemark;
    @SerializedName("Purchase Bucket")
    @Expose
    private String purchaseBucket;

    public SalesOrderModel() {
    }

    public SalesOrderModel(String purchaserName, String soNo, String customerName, String count, String amount, String noofDays, String coardinatorName, String purchaserRemark, String purchaseBucket) {
        this.purchaserName = purchaserName;
        this.soNo = soNo;
        this.customerName = customerName;
        this.count = count;
        this.amount = amount;
        this.noofDays = noofDays;
        this.coardinatorName = coardinatorName;
        this.purchaserRemark = purchaserRemark;
        this.purchaseBucket = purchaseBucket;
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

    public String getNoofDays() {
        return noofDays;
    }

    public void setNoofDays(String noofDays) {
        this.noofDays = noofDays;
    }

    public String getCoardinatorName() {
        return coardinatorName;
    }

    public void setCoardinatorName(String coardinatorName) {
        this.coardinatorName = coardinatorName;
    }

    public String getPurchaserRemark() {
        return purchaserRemark;
    }

    public void setPurchaserRemark(String purchaserRemark) {
        this.purchaserRemark = purchaserRemark;
    }

    public String getPurchaseBucket() {
        return purchaseBucket;
    }

    public void setPurchaseBucket(String purchaseBucket) {
        this.purchaseBucket = purchaseBucket;
    }
}
