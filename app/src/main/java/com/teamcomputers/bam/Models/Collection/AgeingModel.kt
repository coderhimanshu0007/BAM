package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class AgeingModel {
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

        @SerializedName("Interval")
        @Expose
        var interval: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null
        @SerializedName("Invoices")
        @Expose
        var invoices: Int? = null

    }

    inner class Table {

        @SerializedName("CustomerName")
        @Expose
        var customerName: String? = null
        @SerializedName("C0_30")
        @Expose
        var c030: Double? = null
        @SerializedName("C31_60")
        @Expose
        var c3160: Double? = null
        @SerializedName("C61_90")
        @Expose
        var c6190: Double? = null
        @SerializedName("C91_120")
        @Expose
        var c91120: Double? = null
        @SerializedName("C121_180")
        @Expose
        var c121180: Double? = null
        @SerializedName("C181_270")
        @Expose
        var c181270: Double? = null
        @SerializedName("C270_")
        @Expose
        var c270: Double? = null
        @SerializedName("NOTDUE")
        @Expose
        val nOTDUE: Double? = null
        @SerializedName("Total")
        @Expose
        val total: Double? = null
        @SerializedName("open")
        @Expose
        var open: Int = 0

    }
}