package com.teamcomputers.bam.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RSMModel {
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("TMC")
    @Expose
    private String tMC;
    @SerializedName("YTD")
    @Expose
    private Double yTD;
    @SerializedName("MTD")
    @Expose
    private Double mTD;
    @SerializedName("SOAmount")
    @Expose
    private Double sOAmount;
    @SerializedName("Account")
    @Expose
    private List<AccountModel> account = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTMC() {
        return tMC;
    }

    public void setTMC(String tMC) {
        this.tMC = tMC;
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

    public Double getSOAmount() {
        return sOAmount;
    }

    public void setSOAmount(Double sOAmount) {
        this.sOAmount = sOAmount;
    }

    public List<AccountModel> getAccount() {
        return account;
    }

    public void setAccount(List<AccountModel> account) {
        this.account = account;
    }

}
