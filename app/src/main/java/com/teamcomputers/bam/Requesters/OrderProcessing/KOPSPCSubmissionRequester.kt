package com.teamcomputers.bam.Requesters.OrderProcessing

import com.teamcomputers.bam.Interface.BaseRequester
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KOPSPCSubmissionRequester : BaseRequester {
    override fun run() {
        val apiResponse = KHTTPOperationController().orderProessingSPCSubmission()
        if (apiResponse != null) {
            if (apiResponse!!.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (apiResponse!!.getResponse() != null) {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_ORDERPROCESING_SPCSUBMISSION_SUCCESSFULL, apiResponse!!.getResponse()))
                } else {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_ORDERPROCESING_SPCSUBMISSION_UNSUCCESSFULL, null))
                }
            } else {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
            }
        }
    }
}