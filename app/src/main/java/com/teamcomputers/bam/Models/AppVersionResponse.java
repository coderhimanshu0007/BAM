package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppVersionResponse {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("AppVersionID")
    @Expose
    private String appVersionID;
    @SerializedName("AppVersionUrl")
    @Expose
    private String appVersionUrl;
    @SerializedName("Status")
    @Expose
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppVersionID() {
        return appVersionID;
    }

    public void setAppVersionID(String appVersionID) {
        this.appVersionID = appVersionID;
    }

    public String getAppVersionUrl() {
        return appVersionUrl;
    }

    public void setAppVersionUrl(String appVersionUrl) {
        this.appVersionUrl = appVersionUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
