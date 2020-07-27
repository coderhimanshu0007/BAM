package com.teamcomputers.bam.Requesters.Collection

import com.teamcomputers.bam.Interface.BaseRequester
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.AppVersionResponse
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection
import java.util.*

class KCollectionRefreshRequester : BaseRequester {
    override fun run() {
        val apiResponse = KHTTPOperationController().collectionRefresh()
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                if (apiResponse.response != null) {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_COLLECTION_REFRESH_SUCCESSFULL, apiResponse.response))
                } else {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_COLLECTION_REFRESH_UNSUCCESSFULL, null))
                }
            } else if (apiResponse.responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.INTERNAL_SERVER_ERROR, null))
            } else {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.OOPS_MESSAGE, null))
            }
        } else {
            EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
        }
    }
}