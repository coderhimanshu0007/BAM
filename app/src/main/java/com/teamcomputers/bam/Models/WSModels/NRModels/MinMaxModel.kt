package com.teamcomputers.bam.Models.WSModels.NRModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MinMaxModel {

    @SerializedName("MaxAmount")
    @Expose
    private var maxAmount: Float? = null
    @SerializedName("MinAmount")
    @Expose
    private var minAmount: Float? = null
    @SerializedName("Amount")
    @Expose
    private var amount: Float? = null

    fun getMaxAmount(): Float? {
        return maxAmount
    }

    fun setMaxAmount(maxAmount: Float?) {
        this.maxAmount = maxAmount
    }

    fun getMinAmount(): Float? {
        return minAmount
    }

    fun setMinAmount(minAmount: Float?) {
        this.minAmount = minAmount
    }

    fun getAmount(): Float? {
        return amount
    }

    fun setAmount(amount: Float?) {
        this.amount = amount
    }

}