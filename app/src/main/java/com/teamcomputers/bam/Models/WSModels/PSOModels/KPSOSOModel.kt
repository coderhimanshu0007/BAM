package com.teamcomputers.bam.Models.WSModels.PSOModels

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.teamcomputers.bam.Models.WSModels.NRModels.MinMaxModel
import java.io.Serializable


class KPSOSOModel {
    @SerializedName("Data")
    @Expose
    private var data: List<Datum>? = null
    @SerializedName("Filter")
    @Expose
    private var filter: PSOFilter? = null
    @SerializedName("MinMax")
    @Expose
    private var minMaxModel: MinMaxModel? = null

    fun getData(): List<Datum>? {
        return data
    }

    fun setData(data: List<Datum>) {
        this.data = data
    }

    fun getFilter(): PSOFilter? {
        return filter
    }

    fun getMinMax(): MinMaxModel? {
        return minMaxModel
    }

    fun setMinMax(minMax: MinMaxModel) {
        this.minMaxModel = minMax
    }

    @SuppressLint("ParcelCreator")
    inner class Datum : Parcelable {
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.soNumber?.let { dest?.writeString(it) }
            this.nod?.let { dest?.writeInt(it) }
            this.soAmount?.let { dest?.writeDouble(it) }
            this.position?.let { dest?.writeInt(it) }
        }

        override fun describeContents(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        @SerializedName("SONumber")
        @Expose
        var soNumber: String? = null
        @SerializedName("NOD")
        @Expose
        var nod: Int? = null
        @SerializedName("SOAmount")
        @Expose
        var soAmount: Double? = null
        @SerializedName("Position")
        @Expose
        var position: Int? = 0
    }

    fun setFilter(filter: PSOFilter) {
        this.filter = filter
    }

}