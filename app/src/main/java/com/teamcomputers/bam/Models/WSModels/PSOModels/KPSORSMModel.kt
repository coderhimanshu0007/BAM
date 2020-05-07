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

    /*@SuppressLint("ParcelCreator")
    inner class Datum :Parcelable{
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.name?.let { dest?.writeString(it) }
            this.tmc?.let { dest?.writeString(it) }
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
        var position: Int = 0
    }*/

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

    inner class Filter {

        @SerializedName("BUHead")
        @Expose
        var buHead: String? = null
        @SerializedName("BUHead_TMC")
        @Expose
        var buHeadTMC: String? = null
        @SerializedName("RSM")
        @Expose
        var rsm: String? = null
        @SerializedName("RSM_TMC")
        @Expose
        var rsmtmc: String? = null
        @SerializedName("RSM_DSO")
        @Expose
        var rsmdso: Double? = null
        @SerializedName("Account_Manager")
        @Expose
        var accountManager: String? = null
        @SerializedName("AM_TMC")
        @Expose
        var amtmc: String? = null
        @SerializedName("AM_DSO")
        @Expose
        var amdso: Double? = null
        @SerializedName("Sales_Person")
        @Expose
        var salesPerson: String? = null
        @SerializedName("SP_TMC")
        @Expose
        var sptmc: String? = null
        @SerializedName("SP_DSO")
        @Expose
        var spdso: Double? = null
        @SerializedName("CustomerGroupName")
        @Expose
        var customerGroupName: String? = null
        @SerializedName("DocumentNo")
        @Expose
        var documentNo: String? = null
        @SerializedName("Link_TARGETPROD")
        @Expose
        var linkTARGETPROD: String? = null
        @SerializedName("ProductName")
        @Expose
        var productName: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null
    }
}