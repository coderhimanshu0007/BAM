package com.teamcomputers.bam.Models.Collection

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CollectionCollectionDetailModel {
    @SerializedName("Data")
    @Expose
    var data: List<Datum>? = null

    @SuppressLint("ParcelCreator")
    inner class Datum : Parcelable {
        @SuppressLint("NewApi")
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.customerName.let { dest?.writeString(it) }
            this.invoiceNo?.let { dest?.writeInt(it) }
            this.amount?.let { dest?.writeDouble(it) }
        }

        override fun describeContents(): Int {
            return 0
        }

        @SerializedName("CustomerName")
        @Expose
        var customerName: String? = null
        @SerializedName("InvoiceNo")
        @Expose
        private val invoiceNo: Int? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null

    }
}