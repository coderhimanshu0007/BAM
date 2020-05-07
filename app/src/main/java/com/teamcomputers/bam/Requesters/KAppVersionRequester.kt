package com.teamcomputers.bam.Requesters

import com.teamcomputers.bam.Interface.BaseRequester
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.AppVersionResponse
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection
import java.util.*

class KAppVersionRequester : BaseRequester {
    override fun run() {
        val apiResponse = KHTTPOperationController().getCurrentAppVersion()
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                val appVersionResponse = apiResponse.response as ArrayList<AppVersionResponse>
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.YOU_ARE_USING_OLDER_VERSION_OF_APP, appVersionResponse[0]))
                /*if (java.lang.Float.parseFloat(appVersionResponse[0].appVersionID) > KBAMUtils().getVersionCode()) {
                    EventBus.getDefault().post(EventObject(BAMConstant.Events.YOU_ARE_USING_OLDER_VERSION_OF_APP, appVersionResponse[0]))
                } else {
                    EventBus.getDefault().post(EventObject(BAMConstant.Events.YOU_ARE_USING_CURRENT_VERSION_OF_APP, null))
                }*/
            } else {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.OOPS_MESSAGE, null))
            }
        } else {
            EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
        }
    }
}