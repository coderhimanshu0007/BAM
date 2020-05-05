package com.teamcomputers.bam.Requesters.WSRequesters

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KAccountReceivablesAprRequester(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, product: String) : KBaseRequester {
    private var userId: String = userId
    private var level: String = level
    private var type: String = type
    private var RSM: String = RSM
    private var sales: String = sales
    private var customer: String = customer
    private var stateCode: String = stateCode
    private var product: String = product

    override fun run() {
        val apiResponse = KHTTPOperationController().accountReceivablesApr(userId, level, type, RSM, sales, customer, stateCode, product)
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                if (apiResponse.response != null) {
                    if (type == "RSM") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_RSM_TOS_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Sales") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_SALES_TOS_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Customer") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_CUSTOMER_TOS_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Product") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_PRODUCT_TOS_LIST_SUCCESSFULL, apiResponse.response))
                    }
                    //EventBus.getDefault().post(new EventObject(Events.GET_OUTSTANDING_LIST_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    if (type == "RSM") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_RSM_TOS_LIST_UNSUCCESSFULL, null))
                    } else if (type == "Sales") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_SALES_TOS_LIST_UNSUCCESSFULL, null))
                    } else if (type == "Customer") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_CUSTOMER_TOS_LIST_UNSUCCESSFULL, null))
                    } else if (type == "Product") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_PRODUCT_TOS_LIST_UNSUCCESSFULL, null))
                    }
                    //EventBus.getDefault().post(new EventObject(Events.GET_OUTSTANDING_LIST_UNSUCCESSFULL, null));
                }
            } else if (apiResponse.responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NOT_FOUND, null))
            } else {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
            }
        }
    }
}