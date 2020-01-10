package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DispatchTableModel {
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
