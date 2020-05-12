package com.teamcomputers.bam.Models.WSModels.PSOModels

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class KPSORSMModel {
    @SerializedName("Data")
    @Expose
    private var data: List<Datum>? = null
    @SerializedName("Filter")
    @Expose
    private var filter: PSOFilter? = null

    fun getData(): List<Datum>? {
        return data
    }

    fun setData(data: List<Datum>) {
        this.data = data
    }

    fun getFilter(): PSOFilter? {
        return filter
    }

    fun setFilter(filter: PSOFilter) {
        this.filter = filter
    }

    @SuppressLint("ParcelCreator")
    inner class Datum :Parcelable{
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.name?.let { dest?.writeString(it) }
            this.tmc?.let { dest?.writeString(it) }
            this.soAmount?.let { dest?.writeDouble(it) }
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
        @SerializedName("SOAmount")
        @Expose
        var soAmount: Double? = null
        @SerializedName("Position")
        @Expose
        var position: Int = 0
    }

}