package com.teamcomputers.bam.Models.WSModels.NRModels

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KNRInvoiceModel {
    @SerializedName("Data")
    @Expose
    private var data: List<Datum>? = null
    @SerializedName("Filter")
    @Expose
    private var filter: Filter? = null

    fun getData(): List<Datum>? {
        return data
    }

    fun setData(data: List<Datum>) {
        this.data = data
    }

    fun getFilter(): Filter? {
        return filter
    }

    fun setFilter(filter: Filter) {
        this.filter = filter
    }

    @SuppressLint("ParcelCreator")
    inner class Datum : Parcelable {
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.documentNo?.let { dest?.writeString(it) }
            this.amount?.let { dest?.writeDouble(it) }
            this.nod?.let { dest?.writeInt(it) }
            this.open?.let { dest?.writeInt(it) }
            this.position?.let { dest?.writeInt(it) }
        }

        override fun describeContents(): Int {
            return 0
        }

        @SerializedName("Document_No")
        @Expose
        var documentNo: String? = null
        @SerializedName("NOD")
        @Expose
        var nod: Int? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null
        @SerializedName("open")
        @Expose
        var open: Int = 0
        @SerializedName("Position")
        @Expose
        var position: Int? = 0

    }
}