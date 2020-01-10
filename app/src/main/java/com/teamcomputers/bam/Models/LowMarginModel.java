package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LowMarginModel {
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("bu")
    @Expose
    private String bu;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("projectNo")
    @Expose
    private String projectNo;
    @SerializedName("hrsDays")
    @Expose
    private String hrsDays;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("Margin")
    @Expose
    private String margin;

    public LowMarginModel() {
    }

    public LowMarginModel(String customerName, String bu, String count, String projectNo, String hrsDays, String amount, String margin) {
        this.customerName = customerName;
        this.bu = bu;
        this.count = count;
        this.projectNo = projectNo;
        this.hrsDays = hrsDays;
        this.amount = amount;
        this.margin = margin;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getHrsDays() {
        return hrsDays;
    }

    public void setHrsDays(String hrsDays) {
        this.hrsDays = hrsDays;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }
}
