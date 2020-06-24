package com.teamcomputers.bam.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SessionDataModel {

    @SerializedName("Module")
    @Expose
    var module: String? = null
    @SerializedName("Page")
    @Expose
    var page: String? = null
    @SerializedName("LogInTimeStamp")
    @Expose
    var logInTimeStamp: String? = null
    @SerializedName("LogOutTimeStamp")
    @Expose
    var logOutTimeStamp: String? = null
    @SerializedName("OS")
    @Expose
    var os: String? = null
    @SerializedName("Sharing")
    @Expose
    var sharing: Int? = null

}