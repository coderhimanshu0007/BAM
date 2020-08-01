package com.teamcomputers.bam.Models.Collection

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OutstandingModel {
    @SerializedName("Progress")
    @Expose
    private var progress: Progress? = null
    @SerializedName("Table")
    @Expose
    private var table: List<Table>? = null

    fun getProgress(): Progress? {
        return progress
    }

    fun setProgress(progress: Progress) {
        this.progress = progress
    }

    fun getTable(): List<Table>? {
        return table
    }

    fun setTable(table: List<Table>) {
        this.table = table
    }

    inner class Progress {
        @SerializedName("CurrentMonth")
        @Expose
        private var currentMonth: List<ProgressInfo>? = null
        @SerializedName("SubsequentMonth")
        @Expose
        private var subsequentMonth: List<ProgressInfo>? = null
        @SerializedName("CollectibleOutstanding")
        @Expose
        private var collectibleOutstanding: List<ProgressInfo>? = null

        fun getCurrentMonth(): List<ProgressInfo>? {
            return currentMonth
        }

        fun setCurrentMonth(currentMonth: List<ProgressInfo>) {
            this.currentMonth = currentMonth
        }

        fun getSubsequentMonth(): List<ProgressInfo>? {
            return subsequentMonth
        }

        fun setSubsequentMonth(subsequentMonth: List<ProgressInfo>) {
            this.subsequentMonth = subsequentMonth
        }

        fun getCollectibleOutstanding(): List<ProgressInfo>? {
            return collectibleOutstanding
        }

        fun setCollectibleOutstanding(collectibleOutstanding: List<ProgressInfo>) {
            this.collectibleOutstanding = collectibleOutstanding
        }
    }

    inner class ProgressInfo {

        @SerializedName("BU")
        @Expose
        private var bU: String? = null
        @SerializedName("Amount")
        @Expose
        private var amount: Double? = null

        fun getBU(): String? {
            return bU
        }

        fun setBU(bU: String) {
            this.bU = bU
        }

        fun getAmount(): Double? {
            return amount
        }

        fun setAmount(amount: Double?) {
            this.amount = amount
        }
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