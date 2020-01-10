package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WIPModel {
    @SerializedName("Invoice No_")
    @Expose
    private String invoiceNo;
    @SerializedName("Customer Name")
    @Expose
    private String customer_Name;
    @SerializedName("Payment Status")
    @Expose
    private String payment_Status;
    @SerializedName("Count")
    @Expose
    private String count;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("NOD")
    @Expose
    private String nod;
    @SerializedName("Remaining Amount")
    @Expose
    private String remaining_Amount;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Zone Name")
    @Expose
    private String zone_Name;
    @SerializedName("City")
    @Expose
    private String city;

    public WIPModel() {
    }

    public WIPModel(String invoiceNo, String customer_Name, String payment_Status, String count, String amount, String nod, String remaining_Amount, String reason, String zone_Name, String city) {
        this.invoiceNo = invoiceNo;
        this.customer_Name = customer_Name;
        this.payment_Status = payment_Status;
        this.count = count;
        this.amount = amount;
        this.nod = nod;
        this.remaining_Amount = remaining_Amount;
        this.reason = reason;
        this.zone_Name = zone_Name;
        this.city = city;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomer_Name() {
        return customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
    }

    public String getPayment_Status() {
        return payment_Status;
    }

    public void setPayment_Status(String payment_Status) {
        this.payment_Status = payment_Status;
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

    public String getNod() {
        return nod;
    }

    public void setNod(String nod) {
        this.nod = nod;
    }

    public String getRemaining_Amount() {
        return remaining_Amount;
    }

    public void setRemaining_Amount(String remaining_Amount) {
        this.remaining_Amount = remaining_Amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getZone_Name() {
        return zone_Name;
    }

    public void setZone_Name(String zone_Name) {
        this.zone_Name = zone_Name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
