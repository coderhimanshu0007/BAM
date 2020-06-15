package com.teamcomputers.bam.Requesters

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.KBAMApplication
import com.teamcomputers.bam.Models.LoginModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import com.teamcomputers.bam.controllers.SharedPreferencesController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KLoginRequester(val tmcId: String, val password: String) : KBaseRequester {

    override fun run() {
        val apiResponse = KHTTPOperationController().loginUser(tmcId, password)
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                //EventBus.getDefault().post(new EventObject(Events.USER_LOGIN_SUCCESSFUL, null));
                if (apiResponse.response != null) {
                    val loginModel = apiResponse.response as LoginModel
                    SharedPreferencesController.getInstance(KBAMApplication.ctx).isUserLoggedIn = true
                    SharedPreferencesController.getInstance(KBAMApplication.ctx).setUserProfile(loginModel)
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.LOGIN_SUCCESSFUL, null))

                } else {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.LOGIN_UN_SUCCESSFUL, null))
                }
            } else if (apiResponse.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.WRONG_LOGIN_CREDENTIALS_USED, null))
            }
        } else {
            EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
        }
    }
}