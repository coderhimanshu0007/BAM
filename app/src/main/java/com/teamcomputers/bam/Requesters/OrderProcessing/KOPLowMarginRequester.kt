package com.teamcomputers.bam.Requesters.OrderProcessing

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KOPLowMarginRequester : KBaseRequester {
    override fun run() {
        val apiResponse = KHTTPOperationController().orderProessingLowMargin()
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                if (apiResponse.response != null) {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_ORDERPROCESING_LOWMARGIN_SUCCESSFULL, apiResponse.response))
                } else {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_ORDERPROCESING_LOWMARGIN_UNSUCCESSFULL, null))
                }
            } else {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
            }
        }
    }
}