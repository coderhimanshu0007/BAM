package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OutstandingModel {
    @SerializedName("Progress")
    @Expose
    private var progress: List<Progress>? = null
    @SerializedName("Table")
    @Expose
    private var table: List<Table>? = null

    fun getProgress(): List<Progress>? {
        return progress
    }

    fun setProgress(progress: List<Progress>) {
        this.progress = progress
    }

    fun getTable(): List<Table>? {
        return table
    }

    fun setTable(table: List<Table>) {
        this.table = table
    }

    inner class Progress {

        @SerializedName("BU")
        @Expose
        var bu: String? = null
        @SerializedName("Invoice")
        @Expose
        var invoice: Int? = null

    }

    inner class Table {

        @SerializedName("TotalOutStandingInvoice")
        @Expose
        var totalOutStandingInvoice: Int? = null
        @SerializedName("TotalOutStandingAmount")
        @Expose
        var totalOutStandingAmount: Double? = null
        @SerializedName("CollectibleOutStandingInvoice")
        @Expose
        var collectibleOutStandingInvoice: Int? = null
        @SerializedName("CollectibleOutStandingAmount")
        @Expose
        var collectibleOutStandingAmount: Double? = null
        @SerializedName("CollectibleOutStandingCurrentMonthAmount")
        @Expose
        var collectibleOutStandingCurrentMonthAmount: Double? = null
        @SerializedName("CollectibleOutStandingCurrentMonthInvoice")
        @Expose
        var collectibleOutStandingCurrentMonthInvoice: Int? = null
        @SerializedName("CollectibleOutStandingSubsequentMonthInvoice")
        @Expose
        var collectibleOutStandingSubsequentMonthInvoice: Int? = null
        @SerializedName("CollectibleOutStandingSubsequentMonthAmount")
        @Expose
        var collectibleOutStandingSubsequentMonthAmount: Double? = null

    }
}