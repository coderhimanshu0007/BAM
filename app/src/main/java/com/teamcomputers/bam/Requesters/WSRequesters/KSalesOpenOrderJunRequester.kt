package com.teamcomputers.bam.Requesters.WSRequesters

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KSalesOpenOrderJunRequester(val userId: String,val level: String, val type: String, val RSM: String, val sales: String, val customer: String, val stateCode: String, val product: String, val documnetNo: String, val startIndex: String, val endIndex: String, val minAmount: String, val maxAmount: String, val minNOD: String, val maxNOD: String) : KBaseRequester {

    override fun run() {
        val apiResponse = KHTTPOperationController().salesOpenOrderJun(userId, level, type, RSM, sales, customer, stateCode, product, documnetNo, startIndex, endIndex, minAmount, maxAmount, minNOD, maxNOD)
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                if (apiResponse.response != null) {
                    if (type == "RSM") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_RSM_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Sales") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_SALES_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Customer") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_CUSTOMER_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "SONumber") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_INVOICE_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Invoice") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_INVOICE_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Product") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_PRODUCT_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    }
                    //EventBus.getDefault().post(new EventObject(Events.GET_OPEN_SALES_ORDER_LIST_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    if (type == "RSM") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_RSM_OSO_LIST_UNSUCCESSFULL, null))
                    } else if (type == "Sales") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_SALES_OSO_LIST_UNSUCCESSFULL, null))
                    } else if (type == "Customer") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_CUSTOMER_OSO_LIST_UNSUCCESSFULL, null))
                    } else if (type == "SONumber") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_INVOICE_OSO_LIST_UNSUCCESSFULL, null))
                    } else if (type == "Product") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_PRODUCT_OSO_LIST_UNSUCCESSFULL, apiResponse.response))
                    }
                    //EventBus.getDefault().post(new EventObject(Events.GET_OPEN_SALES_ORDER_LIST_UNSUCCESSFULL, null));
                }
            } else if (apiResponse.responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NOT_FOUND, null))
            } else {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
            }
        }
    }
}