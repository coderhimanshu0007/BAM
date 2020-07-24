package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class TotalOutstandingModel {
    @SerializedName("Invoice")
    @Expose
    private var invoice: Int? = null
    @SerializedName("Amount")
    @Expose
    private var amount: Double? = null
    @SerializedName("Table")
    @Expose
    private var table: List<Table>? = null

    fun getInvoice(): Int? {
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
    }

    inner class Table {

        @SerializedName("Invoice_No_")
        @Expose
        var invoiceNo: String? = null
        @SerializedName("CustomerName")
        @Expose
        var customerName: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null
        @SerializedName("NOD")
        @Expose
        var nod: Int? = null
        @SerializedName("Actual_Delivery_Date")
        @Expose
        var actualDeliveryDate: String? = null
        @SerializedName("Payment_Terms_Code")
        @Expose
        var paymentTermsCode: String? = null
        @SerializedName("Doc_Submission_Date")
        @Expose
        var docSubmissionDate: String? = null
        @SerializedName("Expected_Collection_Date")
        @Expose
        var expectedCollectionDate: String? = null
        @SerializedName("Payment_Stage_Name")
        @Expose
        var paymentStageName: String? = null
        @SerializedName("Remarks")
        @Expose
        var remarks: String? = null
        @SerializedName("Remarks_By_Collection_Team")
        @Expose
        var remarksByCollectionTeam: String? = null
        @SerializedName("Ext__Doc__No_")
        @Expose
        var extDocNo: String? = null
        @SerializedName("Percentage")
        @Expose
        var percentage: Double? = null
        @SerializedName("BU")
        @Expose
        var bu: String? = null
        @SerializedName("Finance_Person")
        @Expose
        var financePerson: String? = null
        @SerializedName("Due_Date")
        @Expose
        var dueDate: String? = null
        @SerializedName("open")
        @Expose
        var open: Int = 0

    }
}