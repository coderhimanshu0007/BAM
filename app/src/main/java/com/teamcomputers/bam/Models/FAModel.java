package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FAModel {
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("bu")
    @Expose
    private String bu;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("projectNo")
    @Expose
    private String projectNo;
    @SerializedName("hrsDays")
    @Expose
    private String hrsDays;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("financeDelay")
    @Expose
    private String financeDelay;

    public FAModel() {
    }

    public FAModel(String customerName, String bu, String count, String value, String projectNo, String hrsDays, String name, String financeDelay) {
        this.customerName = customerName;
        this.bu = bu;
        this.count = count;
        this.value = value;
        this.projectNo = projectNo;
        this.hrsDays = hrsDays;
        this.name = name;
        this.financeDelay = financeDelay;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFinanceDelay() {
        return financeDelay;
    }

    public void setFinanceDelay(String financeDelay) {
        this.financeDelay = financeDelay;
    }
}
