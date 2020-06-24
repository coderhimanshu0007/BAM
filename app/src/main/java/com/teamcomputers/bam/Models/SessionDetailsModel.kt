package com.teamcomputers.bam.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SessionDetailsModel {
    @SerializedName("SessionId")
    @Expose
    private var sessionId: Int? = null
    @SerializedName("Data")
    @Expose
    private var data: List<SessionDataModel>? = null

    fun getSessionId(): Int? {
        return sessionId
    }

    fun setSessionId(sessionId: Int?) {
        this.sessionId = sessionId
    }

    fun getData(): List<SessionDataModel>? {
        return data
    }

    fun setData(data: List<SessionDataModel>) {
        this.data = data
    }

}