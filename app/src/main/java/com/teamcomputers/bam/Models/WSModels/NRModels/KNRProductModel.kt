package com.teamcomputers.bam.Models.WSModels.NRModels

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class KNRProductModel {
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
            this.code.let { dest?.writeString(it) }
            this.productName.let { dest?.writeString(it) }
            this.amount?.let { dest?.writeDouble(it) }
            this.position?.let { dest?.writeInt(it) }
        }

        override fun describeContents(): Int {
            return 0
        }

        @SerializedName("Code")
        @Expose
        var code: String? = null
        @SerializedName("ProductName")
        @Expose
        var productName: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null
        @SerializedName("Position")
        @Expose
        var position: Int? = 0
    }

}