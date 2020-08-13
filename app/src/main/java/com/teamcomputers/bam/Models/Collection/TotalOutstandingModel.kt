package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class TotalOutstandingModel {
    @SerializedName("Data")
    @Expose
    var data: List<Datum>? = null
    @SerializedName("Open")
    @Expose
    var open: Int = 0


    inner class Datum {

        @SerializedName("Invoice_No_")
        @Expose
        var invoiceNo: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null
        @SerializedName("NOD")
        @Expose
        var nod: Int? = null
        @SerializedName("DeliveryDate")
        @Expose
        var deliveryDate: String? = null
        @SerializedName("Payment_Terms_Code")
        @Expose
        var paymentTermsCode: String? = null
        @SerializedName("SubmissionDate")
        @Expose
        var submissionDate: String? = null
        @SerializedName("ExpectedDate")
        @Expose
        var expectedDate: String? = null
        @SerializedName("PaymentStage")
        @Expose
        var paymentStage: String? = null
        @SerializedName("Remarks1")
        @Expose
        var remarks1: String? = null
        @SerializedName("Remarks2")
        @Expose
        var remarks2: String? = null
        @SerializedName("CustomerPONo")
        @Expose
        var customerPONo: String? = null
        @SerializedName("Percentage")
        @Expose
        var percentage: Double? = null
        @SerializedName("BU")
        @Expose
        var bu: String? = null
        @SerializedName("FinancePerson")
        @Expose
        var financePerson: String? = null
        @SerializedName("Due_Date")
        @Expose
        var dueDate: String? = null

    }
}