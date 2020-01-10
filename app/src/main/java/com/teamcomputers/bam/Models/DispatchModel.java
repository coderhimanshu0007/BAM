package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DispatchModel {
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("from_City")
    @Expose
    private String from_City;
    @SerializedName("to_City")
    @Expose
    private String to_City;
    @SerializedName("hrsDays")
    @Expose
    private String hrsDays;
    @SerializedName("dispatchPending")
    @Expose
    private String dispatchPending;
    @SerializedName("dispatchPendingValue")
    @Expose
    private String dispatchPendingValue;
    @SerializedName("invoice")
    @Expose
    private String invoice;

    public DispatchModel() {
    }

    public DispatchModel(String customer, String from_City, String to_City, String hrsDays, String dispatchPending, String dispatchPendingValue, String invoice) {
        this.customer = customer;
        this.from_City = from_City;
        this.to_City = to_City;
        this.hrsDays = hrsDays;
        this.dispatchPending = dispatchPending;
        this.dispatchPendingValue = dispatchPendingValue;
        this.invoice = invoice;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getFrom_City() {
        return from_City;
    }

    public void setFrom_City(String from_City) {
        this.from_City = from_City;
    }

    public String getTo_City() {
        return to_City;
    }

    public void setTo_City(String to_City) {
        this.to_City = to_City;
    }

    public String getHrsDays() {
        return hrsDays;
    }

    public void setHrsDays(String hrsDays) {
        this.hrsDays = hrsDays;
    }

    public String getDispatchPending() {
        return dispatchPending;
    }

    public void setDispatchPending(String dispatchPending) {
        this.dispatchPending = dispatchPending;
    }

    public String getDispatchPendingValue() {
        return dispatchPendingValue;
    }

    public void setDispatchPendingValue(String dispatchPendingValue) {
        this.dispatchPendingValue = dispatchPendingValue;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}
