package com.teamcomputers.bam.Requesters.WSRequesters

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KSalesOpenOrderAprRequester(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, invoice: String, product: String) : KBaseRequester {
    private var userId: String = userId
    private var level: String = level
    private var type: String = type
    private var RSM: String = RSM
    private var sales: String = sales
    private var customer: String = customer
    private var stateCode: String = stateCode
    private var invoice: String = invoice
    private var product: String = product

    override fun run() {
        val apiResponse = KHTTPOperationController().salesOpenOrderApr(userId, level, type, RSM, sales, customer, stateCode, invoice, product)
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                if (apiResponse.response != null) {
                    if (type == "RSM") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_RSM_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Sales") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_SALES_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Customer") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_CUSTOMER_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    } else if (type == "Invoice") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_INVOICE_OSO_LIST_SUCCESSFULL, apiResponse.response))
                    }
                    //EventBus.getDefault().post(new EventObject(Events.GET_OPEN_SALES_ORDER_LIST_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    if (type == "RSM") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_RSM_OSO_LIST_UNSUCCESSFULL, null))
                    } else if (type == "Sales") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_SALES_OSO_LIST_UNSUCCESSFULL, null))
                    } else if (type == "Customer") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_CUSTOMER_OSO_LIST_UNSUCCESSFULL, null))
                    } else if (type == "Invoice") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_INVOICE_OSO_LIST_UNSUCCESSFULL, null))
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