package com.teamcomputers.bam.Models.Collection

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CollectionCollectionModel {
    @SerializedName("Progress")
    @Expose
    var progress: Progress? = null
    @SerializedName("Table")
    @Expose
    var table: List<Table>? = null


    inner class Progress {

        @SerializedName("ExpectedCollectionThisWeek")
        @Expose
        var expectedCollectionThisWeek: List<CollectedData>? = null
        @SerializedName("ExpectedCollectionThisMonth")
        @Expose
        var expectedCollectionThisMonth: List<CollectedData>? = null
        @SerializedName("PaymentCollectedThisWeek")
        @Expose
        var paymentCollectedThisWeek: List<CollectedData>? = null
        @SerializedName("PaymentCollectedThisMonth")
        @Expose
        var paymentCollectedThisMonth: List<CollectedData>? = null

    }

    @SuppressLint("ParcelCreator")
    inner class CollectedData : Parcelable {
        @SuppressLint("NewApi")
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.bu.let { dest?.writeString(it) }
            this.amount?.let { dest?.writeDouble(it) }
        }

        override fun describeContents(): Int {
            return 0
        }

        @SerializedName("BU")
        @Expose
        var bu: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null

    }

    @SuppressLint("ParcelCreator")
    inner class Table  : Parcelable {
        @SuppressLint("NewApi")
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.expectedCollectionThisWeekInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.expectedCollectionThisWeekAmount.let { it?.let { it1 -> dest?.writeDouble(it1) } }
            this.expectedCollectionThisMonthInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.expectedCollectionThisMonthAmount.let { it?.let { it1 -> dest?.writeDouble(it1) } }
            this.paymentCollectedThisMonthInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.paymentCollectedThisMonthAmount.let { it?.let { it1 -> dest?.writeDouble(it1) } }
            this.paymentCollectedThisWeekhInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.paymentCollectedThisWeekAmount.let { it?.let { it1 -> dest?.writeDouble(it1) } }
        }

        override fun describeContents(): Int {
            return 0
        }

        @SerializedName("ExpectedCollectionThisWeekInvoice")
        @Expose
        var expectedCollectionThisWeekInvoice: Int? = null
        @SerializedName("ExpectedCollectionThisWeekAmount")
        @Expose
        var expectedCollectionThisWeekAmount: Double? = null
        @SerializedName("ExpectedCollectionThisMonthInvoice")
        @Expose
        var expectedCollectionThisMonthInvoice: Int? = null
        @SerializedName("ExpectedCollectionThisMonthAmount")
        @Expose
        var expectedCollectionThisMonthAmount: Double? = null
        @SerializedName("PaymentCollectedThisMonthAmount")
        @Expose
        var paymentCollectedThisMonthAmount: Double? = null
        @SerializedName("PaymentCollectedThisMonthInvoice")
        @Expose
        var paymentCollectedThisMonthInvoice: Int? = null
        @SerializedName("PaymentCollectedThisWeekhInvoice")
        @Expose
        var paymentCollectedThisWeekhInvoice: Int? = null
        @SerializedName("PaymentCollectedThisWeekAmount")
        @Expose
        var paymentCollectedThisWeekAmount: Double? = null

    }
}