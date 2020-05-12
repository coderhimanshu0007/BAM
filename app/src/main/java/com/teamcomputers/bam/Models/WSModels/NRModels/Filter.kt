package com.teamcomputers.bam.Models.WSModels.NRModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Filter {

    @SerializedName("BUHead")
    @Expose
    var buHead: String? = null
    @SerializedName("BUHead_TMC")
    @Expose
    var buHeadTMC: String? = null
    @SerializedName("RSM")
    @Expose
    var rsm: String? = null
    @SerializedName("RSM_TMC")
    @Expose
    var rsmtmc: String? = null
    @SerializedName("Account_Manager")
    @Expose
    var accountManager: String? = null
    @SerializedName("AM_TMC")
    @Expose
    var amtmc: String? = null
    @SerializedName("Sales_Person")
    @Expose
    var salesPerson: String? = null
    @SerializedName("SP_TMC")
    @Expose
    var sptmc: String? = null
    @SerializedName("CustomerGroupName")
    @Expose
    var customerGroupName: String? = null
    @SerializedName("DocumentNo")
    @Expose
    var documentNo: String? = null
    @SerializedName("Link_TARGETPROD")
    @Expose
    var linkTARGETPROD: String? = null
    @SerializedName("ProductName")
    @Expose
    var productName: String? = null
    @SerializedName("Amount")
    @Expose
    var amount: Double? = null
    @SerializedName("DSO")
    @Expose
    var dso: Double? = null

}