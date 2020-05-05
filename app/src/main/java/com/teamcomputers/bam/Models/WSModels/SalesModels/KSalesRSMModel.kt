package com.teamcomputers.bam.Models.WSModels.SalesModels

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class KSalesRSMModel {
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


    @SuppressLint("ParcelCreator")
    inner class Data() : Parcelable {
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.name?.let { dest?.writeString(it) }
            this.tmc?.let { dest?.writeString(it) }
            this.soAmount?.let { dest?.writeDouble(it) }
            this.targetMTD?.let { dest?.writeDouble(it) }
            this.targetQTD?.let { dest?.writeDouble(it) }
            this.targetYTD?.let { dest?.writeDouble(it) }
            this.mtd?.let { dest?.writeDouble(it) }
            this.qtd?.let { dest?.writeDouble(it) }
            this.ytd?.let { dest?.writeDouble(it) }
            this.mtdPercentage?.let { dest?.writeDouble(it) }
            this.qtdPercentage?.let { dest?.writeDouble(it) }
            this.ytdPercentage?.let { dest?.writeDouble(it) }
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
        @SerializedName("Position")
        @Expose
        var position: Int = 0

        constructor(parcel: Parcel) : this() {
            name = parcel.readString()
            tmc = parcel.readString()
            soAmount = parcel.readValue(Double::class.java.classLoader) as? Double
            targetMTD = parcel.readValue(Double::class.java.classLoader) as? Double
            targetQTD = parcel.readValue(Double::class.java.classLoader) as? Double
            targetYTD = parcel.readValue(Double::class.java.classLoader) as? Double
            mtd = parcel.readValue(Double::class.java.classLoader) as? Double
            qtd = parcel.readValue(Double::class.java.classLoader) as? Double
            ytd = parcel.readValue(Double::class.java.classLoader) as? Double
            mtdPercentage = parcel.readValue(Double::class.java.classLoader) as? Double
            qtdPercentage = parcel.readValue(Double::class.java.classLoader) as? Double
            ytdPercentage = parcel.readValue(Double::class.java.classLoader) as? Double
            position = parcel.readInt()
        }

        /*companion object CREATOR : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }*/
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
}