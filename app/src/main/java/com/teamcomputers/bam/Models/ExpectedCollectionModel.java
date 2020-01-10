package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpectedCollectionModel {
    @SerializedName("Invoice No")
    @Expose
    private String invoiceNo;
    @SerializedName("Customer")
    @Expose
    private String customer;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("Expected Date")
    @Expose
    private String expectedDate;

    public ExpectedCollectionModel() {
    }

    public ExpectedCollectionModel(String invoiceNo, String customer, String amount, String expectedDate) {
        this.invoiceNo = invoiceNo;
        this.customer = customer;
        this.amount = amount;
        this.expectedDate = expectedDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }
}
