package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class CollectionCustomerDetailModel {
    @SerializedName("Data")
    @Expose
    var data: List<Datum>? = null

    inner class Datum {

        @SerializedName("CustomerName")
        @Expose
        var customerName: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null

    }
}