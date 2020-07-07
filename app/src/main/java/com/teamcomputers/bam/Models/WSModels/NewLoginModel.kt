package com.teamcomputers.bam.Models.WSModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.teamcomputers.bam.Models.LoginModel


class NewLoginModel {

    @SerializedName("Login")
    @Expose
    private var login: LoginModel? = null
    @SerializedName("Device")
    @Expose
    private var device: Device? = null

    fun getLogin(): LoginModel? {
        return login
    }

    fun setLogin(login: LoginModel) {
        this.login = login
    }

    fun getDevice(): Device? {
        return device
    }

    fun setDevice(device: Device) {
        this.device = device
    }


    inner class Device {

        @SerializedName("Status")
        @Expose
        var status: String? = null
        @SerializedName("Message")
        @Expose
        var message: String? = null

    }
}