package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutstandingModel {
    @SerializedName("Invoice No")
    @Expose
    private String invoiceNo;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("Finance Person")
    @Expose
    private String financePerson;
    @SerializedName("NOD")
    @Expose
    private String nod;
    @SerializedName("Due Date")
    @Expose
    private String dueDate;
    @SerializedName("Expected Date")
    @Expose
    private String expectedDate;
    @SerializedName("Payment Stage")
    @Expose
    private String paymetStage;
    @SerializedName("Remarks")
    @Expose
    private String remarks;

    public OutstandingModel() {
    }

    public OutstandingModel(String invoiceNo, String name, String amount, String financePerson, String nod, String dueDate, String expectedDate, String paymetStage, String remarks) {
        this.invoiceNo = invoiceNo;
        this.name = name;
        this.amount = amount;
        this.financePerson = financePerson;
        this.nod = nod;
        this.dueDate = dueDate;
        this.expectedDate = expectedDate;
        this.paymetStage = paymetStage;
        this.remarks = remarks;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFinancePerson() {
        return financePerson;
    }

    public void setFinancePerson(String financePerson) {
        this.financePerson = financePerson;
    }

    public String getNod() {
        return nod;
    }

    public void setNod(String nod) {
        this.nod = nod;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getPaymetStage() {
        return paymetStage;
    }

    public void setPaymetStage(String paymetStage) {
        this.paymetStage = paymetStage;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
