package com.teamcomputers.bam.Models.WSModels.PSOModels

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class KPSOCustomerModel {
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
    inner class Datum : Parcelable {
        @SuppressLint("NewApi")
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.customerName?.let { dest?.writeString(it) }
            this.sOAmount?.let { dest?.writeDouble(it) }
            this.position?.let { dest?.writeInt(it) }
            this.open?.let { dest?.writeInt(it) }
            this.isSelected?.let { dest?.writeBoolean(it) }
        }

        override fun describeContents(): Int {
            return 0
        }

        @SerializedName("CustomerName")
        @Expose
        var customerName: String? = null
        @SerializedName("SOAmount")
        @Expose
        var sOAmount: Double? = null
        @SerializedName("Position")
        @Expose
        var position: Int? = 0
        @SerializedName("open")
        @Expose
        var open: Int = 0
        @SerializedName("StateCodeWise")
        @Expose
        var stateCodeWise: List<StateCodeWise>? = null
        @SerializedName("isSelected")
        @Expose
        var isSelected: Boolean = false
    }

    inner class StateCodeWise {

        @SerializedName("StateCode")
        @Expose
        var stateCode: String? = null
        @SerializedName("SOAmount")
        @Expose
        var soAmount: Double? = null

    }


}

