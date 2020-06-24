package com.teamcomputers.bam.Requesters

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.KBAMApplication
import com.teamcomputers.bam.Models.ActiveEmployeeAccessModel
import com.teamcomputers.bam.Models.LoginModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import com.teamcomputers.bam.controllers.SharedPreferencesController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KActiveEmployeeAccessRequester(val userId: String, val userName: String) : KBaseRequester {

    override fun run() {
        val apiResponse = KHTTPOperationController().activeEmployeeAccess(userId, userName)
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                if (apiResponse.response != null) {
                    val activeEmployeeAccessModel = apiResponse.response as ActiveEmployeeAccessModel
                    SharedPreferencesController.getInstance(KBAMApplication.ctx).setActiveEmployeeAccess(activeEmployeeAccessModel)
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_ACTIVE_EMPLOYEE_ACCESS_SUCCESSFUL, null))

                } else {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_ACTIVE_EMPLOYEE_ACCESS_UNSUCCESSFUL, null))
                }
            }
        } else {
            EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
        }
    }
}