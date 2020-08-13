package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CollectionCustomerDetailsModel {
    @SerializedName("Data")
    @Expose
    var data: List<Datum>? = null

    inner class Datum {

        @SerializedName("Invoice_No_")
        @Expose
        var invoiceNo: String? = null
        @SerializedName("Document_No_")
        @Expose
        var documentNo: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null
        @SerializedName("ExpectedDate")
        @Expose
        var expectedDate: String? = null
        @SerializedName("PostingDate")
        @Expose
        var postingDate: String? = null
        @SerializedName("BU")
        @Expose
        var bu: String? = null


    }

}