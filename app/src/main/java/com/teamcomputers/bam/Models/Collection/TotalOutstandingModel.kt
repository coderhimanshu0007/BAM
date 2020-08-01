package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class TotalOutstandingModel {
    @SerializedName("Invoice")
    @Expose
    var invoice: Int? = null
    @SerializedName("Amount")
    @Expose
    var amount: Double? = null
    @SerializedName("Table")
    @Expose
    var table: List<Table>? = null

    /*fun getInvoice(): Int? {
        return invoice
    }

    fun setInvoice(invoice: Int?) {
        this.invoice = invoice
    }

    fun getAmount(): Double? {
        return amount
    }

    fun setAmount(amount: Double?) {
        this.amount = amount
    }

    fun getTable(): List<Table>? {
        return table
    }

    fun setTable(table: List<Table>) {
        this.table = table
    }*/

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
        var open: Int? = 0

        /*fun getName(): String? {
            return name
        }

        fun setName(name: String) {
            this.name = name
        }

        fun getAmount(): Double? {
            return amount
        }

        fun setAmount(amount: Double?) {
            this.amount = amount
        }

        fun getData(): List<Datum>? {
            return data
        }

        fun setData(data: List<Datum>) {
            this.data = data
        }*/

    }

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