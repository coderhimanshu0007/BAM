package com.teamcomputers.bam.Models.WSModels.NRModels

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class KNRRSMModel {
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
    inner class Datum : Parcelable{
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.name.let { dest?.writeString(it) }
            this.tmc.let { dest?.writeString(it) }
            this.dso?.let { dest?.writeDouble(it) }
            this.amount?.let { dest?.writeDouble(it) }
            this.position?.let { dest?.writeInt(it) }
        }

        override fun describeContents(): Int {
            return 0
        }

        @SerializedName("Name")
        @Expose
        var name: String? = null
        @SerializedName("TMC")
        @Expose
        var tmc: String? = null
        @SerializedName("DSO")
        @Expose
        var dso: Double? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null
        @SerializedName("Position")
        @Expose
        var position: Int? = 0
    }

}