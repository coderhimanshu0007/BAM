package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

public class SRResponseModel {
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("TMC")
    @Expose
    private String tMC;
    @SerializedName("YTD")
    @Expose
    private String yTD;
    @SerializedName("MTD")
    @Expose
    private String mTD;
    @SerializedName("SO")
    @Expose
    private String sO;
    @SerializedName("linkedTreeMap")
    @Expose
    private ArrayList<LinkedTreeMap> linkedTreeMap;

    public SRResponseModel() {
    }

    public SRResponseModel(String name, String mtd, String ytd, String sO) {
        this.name = name;
        this.yTD = ytd;
        this.mTD = mtd;
        this.sO = sO;
    }

    public SRResponseModel(String name, String productName, String tMC, String yTD, String sO, String mTD) {
        this.name = name;
        this.productName = productName;
        this.tMC = tMC;
        this.yTD = yTD;
        this.mTD = mTD;
        this.sO = sO;
    }

    public SRResponseModel(String name, String tMC, String yTD, String mTD, String sO, ArrayList<LinkedTreeMap> linkedTreeMap) {
        this.name = name;
        this.tMC = tMC;
        this.yTD = yTD;
        this.mTD = mTD;
        this.sO = sO;
        this.linkedTreeMap = linkedTreeMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTMC() {
        return tMC;
    }

    public void setTMC(String tMC) {
        this.tMC = tMC;
    }

    public String getYTD() {
        return yTD;
    }

    public void setYTD(String yTD) {
        this.yTD = yTD;
    }

    public String getMTD() {
        return mTD;
    }

    public void setMTD(String mTD) {
        this.mTD = mTD;
    }

    public String getsO() {
        return sO;
    }

    public void setsO(String sO) {
        this.sO = sO;
    }

    public ArrayList<LinkedTreeMap> getLinkedTreeMap() {
        return linkedTreeMap;
    }

    public void setLinkedTreeMap(ArrayList<LinkedTreeMap> linkedTreeMap) {
        this.linkedTreeMap = linkedTreeMap;
    }
}
