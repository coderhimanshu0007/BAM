package com.teamcomputers.bam.Requesters.WSRequesters

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KYTDQTDFiscalRequester(val userId: String, val fiscalYear: String) : KBaseRequester {
    override fun run() {
        val apiResponse = KHTTPOperationController().fiscalYTDQTD(userId, fiscalYear)
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                if (apiResponse.response != null) {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_YTDQTD_SUCCESSFULL, apiResponse.response))
                } else {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_YTDQTD_UNSUCCESSFULL, null))
                }
            } else {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
            }
        }
    }
}