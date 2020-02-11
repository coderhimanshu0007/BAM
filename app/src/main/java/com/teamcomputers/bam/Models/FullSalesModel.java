package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FullSalesModel {
    @SerializedName("TMC")
    @Expose
    private String tMC;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("YTD")
    @Expose
    private Double yTD;
    @SerializedName("MTD")
    @Expose
    private Double mTD;
    @SerializedName("QTD")
    @Expose
    private Double qTD;

    public String getTMC() {
        return tMC;
    }

    public void setTMC(String tMC) {
        this.tMC = tMC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
