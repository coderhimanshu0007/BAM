package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CollectionWIPDetailModel {
    @SerializedName("Invoice")
    @Expose
    var invoice: Int? = null
    @SerializedName("Amount")
    @Expose
    var amount: Double? = null
    @SerializedName("Table")
    @Expose
    var table: List<Table>? = null

    inner class Table {

        @SerializedName("Name")
        @Expose
        var name: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null
        @SerializedName("Data")
        @Expose
        var data: List<Datum>? = null
        @SerializedName("Open")
        @Expose
        var open: Int = 0

    }

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

    }

}