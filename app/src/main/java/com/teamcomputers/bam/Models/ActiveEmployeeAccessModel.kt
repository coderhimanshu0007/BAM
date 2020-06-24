package com.teamcomputers.bam.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ActiveEmployeeAccessModel {

    @SerializedName("Data")
    @Expose
    private var data: Data? = null

    fun getData(): Data? {
        return data
    }

    fun setData(data: Data) {
        this.data = data
    }

    inner class Data {

        @SerializedName("TMC")
        @Expose
        var tmc: String? = null
        @SerializedName("Name")
        @Expose
        var name: String? = null
        @SerializedName("SBU")
        @Expose
        var sbu: Int? = null
        @SerializedName("Sales_Module")
        @Expose
        var salesModule: Int? = null
        @SerializedName("Logistics_Module")
        @Expose
        var logisticsModule: Int? = null
        @SerializedName("OrderProcessing_Module")
        @Expose
        var orderProcessingModule: Int? = null
        @SerializedName("Installation_Module")
        @Expose
        var installationModule: Int? = null
        @SerializedName("Purchase_Module")
        @Expose
        var purchaseModule: Int? = null
        @SerializedName("Collection_Module")
        @Expose
        var collectionModule: Int? = null
        @SerializedName("SessionId")
        @Expose
        var sessionId: Int? = null

    }
}