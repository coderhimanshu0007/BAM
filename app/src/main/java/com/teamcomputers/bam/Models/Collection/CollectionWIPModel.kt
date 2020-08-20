package com.teamcomputers.bam.Models.Collection

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CollectionWIPModel {
    @SerializedName("Progress")
    @Expose
    private var progress: Progress? = null
    @SerializedName("Table")
    @Expose
    private var table: List<Table>? = null

    fun getProgress(): Progress? {
        return progress
    }

    fun setProgress(progress: Progress) {
        this.progress = progress
    }

    fun getTable(): List<Table>? {
        return table
    }

    fun setTable(table: List<Table>) {
        this.table = table
    }

    inner class Progress {

        @SerializedName("WIP015Days")
        @Expose
        var wiP015Days: List<ProgressData>? = null
        @SerializedName("WIP1630Days")
        @Expose
        var wiP1630Days: List<ProgressData>? = null
        @SerializedName("WIP30Days")
        @Expose
        var wiP30Days: List<ProgressData>? = null
        @SerializedName("WIPPendingDocLessThan2Days")
        @Expose
        var wipPendingDocLessThan2Days: List<ProgressData>? = null
        @SerializedName("WIPPendingDocGreaterThan2Days")
        @Expose
        var wipPendingDocGreaterThan2Days: List<ProgressData>? = null

    }

    inner class ProgressData {

        @SerializedName("BU")
        @Expose
        var bu: String? = null
        @SerializedName("Amount")
        @Expose
        var amount: Double? = null

    }

    @SuppressLint("ParcelCreator")
    inner class Table : Parcelable {
        @SuppressLint("NewApi")
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            this.wiP015DaysInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.wiP015DaysAmount?.let { dest?.writeDouble(it) }
            this.wiP1630DaysInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.wiP1630aysAmount?.let { dest?.writeDouble(it) }
            this.wiP30DaysInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.wiP30DaysAmount?.let { dest?.writeDouble(it) }
            this.wipPendingDocSubmissionLessThan2DaysInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.wipPendingDocSubmissionLessThan2DaysAmount?.let { dest?.writeDouble(it) }
            this.wipPendingDocSubmissionGreaterThan2DaysInvoice.let { it?.let { it1 -> dest?.writeInt(it1) } }
            this.wipPendingDocSubmissionGreaterThan2DaysAmount?.let { dest?.writeDouble(it) }

        }

        override fun describeContents(): Int {
            return 0
        }

        @SerializedName("WIP015DaysInvoice")
        @Expose
        var wiP015DaysInvoice: Int? = null
        @SerializedName("WIP015DaysAmount")
        @Expose
        var wiP015DaysAmount: Double? = null
        @SerializedName("WIP1630DaysInvoice")
        @Expose
        var wiP1630DaysInvoice: Int? = null
        @SerializedName("WIP1630aysAmount")
        @Expose
        var wiP1630aysAmount: Double? = null
        @SerializedName("WIP30DaysInvoice")
        @Expose
        var wiP30DaysInvoice: Int? = null
        @SerializedName("WIP30DaysAmount")
        @Expose
        var wiP30DaysAmount: Double? = null
        @SerializedName("WIPPendingDocSubmissionLessThan2DaysInvoice")
        @Expose
        var wipPendingDocSubmissionLessThan2DaysInvoice: Int? = null
        @SerializedName("WIPPendingDocSubmissionLessThan2DaysAmount")
        @Expose
        var wipPendingDocSubmissionLessThan2DaysAmount: Double? = null
        @SerializedName("WIPPendingDocSubmissionGreaterThan2DaysInvoice")
        @Expose
        var wipPendingDocSubmissionGreaterThan2DaysInvoice: Int? = null
        @SerializedName("WIPPendingDocSubmissionGreaterThan2DaysAmount")
        @Expose
        var wipPendingDocSubmissionGreaterThan2DaysAmount: Double? = null

    }

}