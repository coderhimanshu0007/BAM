package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SPCSubmissionModel {
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("projectNo")
    @Expose
    private String projectNo;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("hrsDays")
    @Expose
    private String hrsDays;
    @SerializedName("product Head Name")
    @Expose
    private String headName;
    @SerializedName("reason")
    @Expose
    private String reason;


    public SPCSubmissionModel() {
    }

    public SPCSubmissionModel(String customer, String projectNo, String count, String hrsDays, String headName, String reason) {
        this.customer = customer;
        this.projectNo = projectNo;
        this.count = count;
        this.hrsDays = hrsDays;
        this.headName = headName;
        this.reason = reason;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getHrsDays() {
        return hrsDays;
    }

    public void setHrsDays(String hrsDays) {
        this.hrsDays = hrsDays;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
