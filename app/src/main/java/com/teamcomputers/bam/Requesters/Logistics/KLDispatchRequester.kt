package com.teamcomputers.bam.Requesters.Logistics

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.Models.LogisticDispatchModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.HTTPOperationController
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection
import java.util.ArrayList

class KLDispatchRequester : KBaseRequester {
    override fun run() {
        val apiResponse = KHTTPOperationController().logisticsDispatch()
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                val dispatchModel = apiResponse.response as ArrayList<LogisticDispatchModel>
                if (dispatchModel != null) {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_LOGISTICS_DISPATCH_SUCCESSFULL, dispatchModel))
                } else {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_LOGISTICS_DISPATCH_UNSUCCESSFULL, null))
                }
            } else {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
            }
        }
    }
}