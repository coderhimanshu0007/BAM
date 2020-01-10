package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EDDModel {
    @SerializedName("Purchaser Name")
    @Expose
    private String purchaserName;
    @SerializedName("PO No")
    @Expose
    private String poNo;
    @SerializedName("Customer Name")
    @Expose
    private String customerName;
    @SerializedName("Vendor Name")
    @Expose
    private String vendorName;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("NOD")
    @Expose
    private String nod;
    @SerializedName("DR")
    @Expose
    private String dr;

    public EDDModel() {
    }

    public EDDModel(String purchaserName, String poNo, String customerName, String vendorName, String amount, String nod, String dr) {
        this.purchaserName = purchaserName;
        this.poNo = poNo;
        this.customerName = customerName;
        this.vendorName = vendorName;
        this.amount = amount;
        this.nod = nod;
        this.dr = dr;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }
}
