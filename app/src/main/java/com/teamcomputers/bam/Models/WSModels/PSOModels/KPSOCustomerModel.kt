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
    inner class Datum :Parcelable{
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.customerName?.let { dest?.writeString(it) }
            this.sOAmount?.let { dest?.writeDouble(it) }
            this.position?.let { dest?.writeInt(it) }
            this.open?.let { dest?.writeInt(it) }
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
    }

    inner class StateCodeWise {

        @SerializedName("StateCode")
        @Expose
        var stateCode: String? = null
        @SerializedName("SOAmount")
        @Expose
        var soAmount: Double? = null

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
        @SerializedName("Account_Manager")
        @Expose
        var accountManager: String? = null
        @SerializedName("AM_TMC")
        @Expose
        var amtmc: String? = null
        @SerializedName("Sales_Person")
        @Expose
        var salesPerson: String? = null
        @SerializedName("SP_TMC")
        @Expose
        var sptmc: String? = null
        @SerializedName("CustomerGroupName")
        @Expose
        var customerGroupName: String? = null
        @SerializedName("StateCode")
        @Expose
        var stateCode: String? = null
        @SerializedName("SONumber")
        @Expose
        var soNumber: String? = null
        @SerializedName("Link_TARGETPROD")
        @Expose
        var linkTARGETPROD: String? = null
        @SerializedName("ProductName")
        @Expose
        var productName: String? = null
        @SerializedName("SOAmount")
        @Expose
        var soAmount: Double? = null
    }

}

