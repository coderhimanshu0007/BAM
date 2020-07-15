package com.teamcomputers.bam.Models.WSModels.SalesModels

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class KSalesCustomerModel {
    @SerializedName("Data")
    @Expose
    private var data: List<Data>? = null
    @SerializedName("Filter")
    @Expose
    private var filter: Filter? = null

    fun getData(): List<Data>? {
        return data
    }

    fun setData(data: List<Data>) {
        this.data = data
    }

    fun getFilter(): Filter? {
        return filter
    }

    fun setFilter(filter: Filter) {
        this.filter = filter
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
        @SerializedName("Link_TARGETPROD")
        @Expose
        var linkTARGETPROD: String? = null
        @SerializedName("ProductName")
        @Expose
        var productName: String? = null
        @SerializedName("SOAmount")
        @Expose
        var soAmount: Double? = null
        @SerializedName("Target_MTD")
        @Expose
        var targetMTD: Double? = null
        @SerializedName("Target_QTD")
        @Expose
        var targetQTD: Double? = null
        @SerializedName("Target_YTD")
        @Expose
        var targetYTD: Double? = null
        @SerializedName("MTD")
        @Expose
        var mtd: Double? = null
        @SerializedName("QTD")
        @Expose
        var qtd: Double? = null
        @SerializedName("YTD")
        @Expose
        var ytd: Double? = null
        @SerializedName("MTD_Percentage")
        @Expose
        var mtdPercentage: Double? = null
        @SerializedName("QTD_Percentage")
        @Expose
        var qtdPercentage: Double? = null
        @SerializedName("YTD_Percentage")
        @Expose
        var ytdPercentage: Double? = null
    }

    @SuppressLint("ParcelCreator")
    inner class Data :Parcelable{
        @SuppressLint("NewApi")
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.userId?.let { dest?.writeString(it) }
            this.customerName?.let { dest?.writeString(it) }
            this.ytd?.let { dest?.writeDouble(it) }
            this.qtd?.let { dest?.writeDouble(it) }
            this.mtd?.let { dest?.writeDouble(it) }
            this.soAmount?.let { dest?.writeDouble(it) }
            this.open?.let { dest?.writeInt(it) }
            this.position?.let { dest?.writeInt(it) }
            this.stateCodeWise?.let { dest?.writeArray(arrayOf(it)) }
            this.isSelected?.let { dest?.writeBoolean(it) }
        }

        override fun describeContents(): Int {
            return 0
        }

        @SerializedName("UserId")
        @Expose
        var userId: String? = null
        @SerializedName("CustomerName")
        @Expose
        var customerName: String? = null
        @SerializedName("YTD")
        @Expose
        var ytd: Double? = null
        @SerializedName("QTD")
        @Expose
        var qtd: Double? = null
        @SerializedName("MTD")
        @Expose
        var mtd: Double? = null
        @SerializedName("SOAmount")
        @Expose
        var soAmount: Double? = null
        @SerializedName("open")
        @Expose
        var open: Int = 0
        @SerializedName("Position")
        @Expose
        var position: Int = 0
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
        @SerializedName("YTD")
        @Expose
        var ytd: Double? = null
        @SerializedName("QTD")
        @Expose
        var qtd: Double? = null
        @SerializedName("MTD")
        @Expose
        var mtd: Double? = null
        @SerializedName("SOAmount")
        @Expose
        var soAmount: Double? = null

    }
}