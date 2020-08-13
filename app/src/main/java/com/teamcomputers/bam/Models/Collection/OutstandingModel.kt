package com.teamcomputers.bam.Models.Collection

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
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
        @SerializedName("TotalOutStanding")
        @Expose
        var totalOutStanding: List<ProgressInfo>? = null
        @SerializedName("CurrentMonth")
        @Expose
        var currentMonth: List<ProgressInfo>? = null
        @SerializedName("SubsequentMonth")
        @Expose
        var subsequentMonth: List<ProgressInfo>? = null
        @SerializedName("CollectibleOutstanding")
        @Expose
        var collectibleOutstanding: List<ProgressInfo>? = null

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

    @SuppressLint("ParcelCreator")
    inner class Table : Parcelable {
        @SuppressLint("NewApi")
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.totalOutStandingInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.totalOutStandingAmount.let { it?.let { it1 -> dest?.writeDouble(it1) } }
            this.collectibleOutStandingInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.collectibleOutStandingAmount.let { it?.let { it1 -> dest?.writeDouble(it1) } }
            this.collectibleOutStandingCurrentMonthInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.collectibleOutStandingCurrentMonthAmount.let { it?.let { it1 -> dest?.writeDouble(it1) } }
            this.collectibleOutStandingSubsequentMonthInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.collectibleOutStandingSubsequentMonthAmount.let { it?.let { it1 -> dest?.writeDouble(it1) } }
        }

        override fun describeContents(): Int {
            return 0
        }

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