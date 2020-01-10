package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LogisticsDispatchModel {

    @SerializedName("Pending")
    @Expose
    private Pending pending;
    @SerializedName("PendingEightHours")
    @Expose
    private PendingEightHours pendingEightHours;

    public Pending getPending() {
        return pending;
    }

    public void setPending(Pending pending) {
        this.pending = pending;
    }

    public PendingEightHours getPendingEightHours() {
        return pendingEightHours;
    }

    public void setPendingEightHours(PendingEightHours pendingEightHours) {
        this.pendingEightHours = pendingEightHours;
    }

}

class PendingEightHours {

    @SerializedName("Invoices")
    @Expose
    private Integer invoices;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("Table")
    @Expose
    private List<Object> table = null;

    public Integer getInvoices() {
        return invoices;
    }

    public void setInvoices(Integer invoices) {
        this.invoices = invoices;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<Object> getTable() {
        return table;
    }

    public void setTable(List<Object> table) {
        this.table = table;
    }

}

class Table {

    @SerializedName("InvoiceNo")
    @Expose
    private String invoiceNo;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("FromCity")
    @Expose
    private String fromCity;
    @SerializedName("HoursDays")
    @Expose
    private String hoursDays;
    @SerializedName("DispatchPending")
    @Expose
    private Integer dispatchPending;
    @SerializedName("DispatchPendingValue")
    @Expose
    private Double dispatchPendingValue;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getHoursDays() {
        return hoursDays;
    }

    public void setHoursDays(String hoursDays) {
        this.hoursDays = hoursDays;
    }

    public Integer getDispatchPending() {
        return dispatchPending;
    }

    public void setDispatchPending(Integer dispatchPending) {
        this.dispatchPending = dispatchPending;
    }

    public Double getDispatchPendingValue() {
        return dispatchPendingValue;
    }

    public void setDispatchPendingValue(Double dispatchPendingValue) {
        this.dispatchPendingValue = dispatchPendingValue;
    }

}
