package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesReceivableModel {
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("YTD")
    @Expose
    private Double yTD;
    @SerializedName("MTD")
    @Expose
    private Double mTD;
    @SerializedName("QTD")
    @Expose
    private Double qTD;
    @SerializedName("DSO")
    @Expose
    private Double dSO;
    @SerializedName("OpenSalesOrder")
    @Expose
    private Double openSalesOrder;
    @SerializedName("OutStanding")
    @Expose
    private Double outStanding;
    @SerializedName("Level")
    @Expose
    private String level;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getYTD() {
        return yTD;
    }

    public void setYTD(Double yTD) {
        this.yTD = yTD;
    }

    public Double getMTD() {
        return mTD;
    }

    public void setMTD(Double mTD) {
        this.mTD = mTD;
    }

    public Double getQTD() {
        return qTD;
    }

    public void setQTD(Double qTD) {
        this.qTD = qTD;
    }

    public Double getDSO() {
        return dSO;
    }

    public void setDSO(Double dSO) {
        this.dSO = dSO;
    }

    public Double getOpenSalesOrder() {
        return openSalesOrder;
    }

    public void setOpenSalesOrder(Double openSalesOrder) {
        this.openSalesOrder = openSalesOrder;
    }

    public Double getOutStanding() {
        return outStanding;
    }

    public void setOutStanding(Double outStanding) {
        this.outStanding = outStanding;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
