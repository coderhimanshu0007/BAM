package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnHoldModel {
    @SerializedName("Invoice No")
    @Expose
    private String invoice_No;
    @SerializedName("Customer Name")
    @Expose
    private String customer_Name;
    @SerializedName("On Hold Amount")
    @Expose
    private String on_Hold_Amount;
    @SerializedName("Zone Name")
    @Expose
    private String zone_Name;
    @SerializedName("NOD")
    @Expose
    private String nod;
    @SerializedName("Payment Status")
    @Expose
    private String payment_Status;
    @SerializedName("Remaining Amount")
    @Expose
    private String remaining_Amount;
    @SerializedName("Reason")
    @Expose
    private String reason;

    public OnHoldModel() {
    }

    public OnHoldModel(String invoice_No, String customer_Name, String on_Hold_Amount, String zone_Name, String nod, String payment_Status, String remaining_Amount, String reason) {
        this.invoice_No = invoice_No;
        this.customer_Name = customer_Name;
        this.on_Hold_Amount = on_Hold_Amount;
        this.zone_Name = zone_Name;
        this.nod = nod;
        this.payment_Status = payment_Status;
        this.remaining_Amount = remaining_Amount;
        this.reason = reason;
    }

    public String getInvoice_No() {
        return invoice_No;
    }

    public void setInvoice_No(String invoice_No) {
        this.invoice_No = invoice_No;
    }

    public String getCustomer_Name() {
        return customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
    }

    public String getOn_Hold_Amount() {
        return on_Hold_Amount;
    }

    public void setOn_Hold_Amount(String on_Hold_Amount) {
        this.on_Hold_Amount = on_Hold_Amount;
    }

    public String getZone_Name() {
        return zone_Name;
    }

    public void setZone_Name(String zone_Name) {
        this.zone_Name = zone_Name;
    }

    public String getNod() {
        return nod;
    }

    public void setNod(String nod) {
        this.nod = nod;
    }

    public String getPayment_Status() {
        return payment_Status;
    }

    public void setPayment_Status(String payment_Status) {
        this.payment_Status = payment_Status;
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
}
