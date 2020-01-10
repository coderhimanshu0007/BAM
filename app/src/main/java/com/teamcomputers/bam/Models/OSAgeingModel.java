package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OSAgeingModel {
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("DUO")
    @Expose
    private String DUO;
    @SerializedName("0-30")
    @Expose
    private String upto30;
    @SerializedName("31-60")
    @Expose
    private String upto60;
    @SerializedName("61-90")
    @Expose
    private String upto90;
    @SerializedName("91-120")
    @Expose
    private String upto120;
    @SerializedName("120+")
    @Expose
    private String moreThan120;
    @SerializedName("Total")
    @Expose
    private String total;

    public OSAgeingModel() {
    }

    public OSAgeingModel(String name, String DUO, String upto30, String upto60, String upto90, String upto120, String moreThan120, String total) {
        this.name = name;
        this.DUO = DUO;
        this.upto30 = upto30;
        this.upto60 = upto60;
        this.upto90 = upto90;
        this.upto120 = upto120;
        this.moreThan120 = moreThan120;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDUO() {
        return DUO;
    }

    public void setDUO(String DUO) {
        this.DUO = DUO;
    }

    public String getUpto30() {
        return upto30;
    }

    public void setUpto30(String upto30) {
        this.upto30 = upto30;
    }

    public String getUpto60() {
        return upto60;
    }

    public void setUpto60(String upto60) {
        this.upto60 = upto60;
    }

    public String getUpto90() {
        return upto90;
    }

    public void setUpto90(String upto90) {
        this.upto90 = upto90;
    }

    public String getUpto120() {
        return upto120;
    }

    public void setUpto120(String upto120) {
        this.upto120 = upto120;
    }

    public String getMoreThan120() {
        return moreThan120;
    }

    public void setMoreThan120(String moreThan120) {
        this.moreThan120 = moreThan120;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
