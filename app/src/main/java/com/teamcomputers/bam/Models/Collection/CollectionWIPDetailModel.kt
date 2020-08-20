package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CollectionWIPDetailModel {
    @SerializedName("Data")
    @Expose
    var data: List<Datum>? = null

    inner class Datum {

        @SerializedName("NOD")
        @Expose
        var nod: Int? = null
        @SerializedName("Invoice_Date")
        @Expose
        var invoiceDate: String? = null
        @SerializedName("InstallationDate")
        @Expose
        var installationDate: String? = null
        @SerializedName("DeliveryDate")
        @Expose
        var deliveryDate: String? = null
        @SerializedName("Flag")
        @Expose
        var flag: String? = null
        @SerializedName("CustomerPONo")
        @Expose
        var customerPONo: String? = null
        @SerializedName("BU")
        @Expose
        var bu: String? = null
        @SerializedName("Invoice_No_")
        @Expose
        var invoiceNo: String? = null
        @SerializedName("Percentage")
        @Expose
        var percentage: Double? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null

    }

}