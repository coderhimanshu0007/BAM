package com.teamcomputers.bam.Requesters.Collection

import com.teamcomputers.bam.Interface.BaseRequester
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KWIP015DaysCustomerInvoiceRequester(var customer: String, var start: String, var end: String, var loadMore: String) : BaseRequester {
    override fun run() {
        val apiResponse = KHTTPOperationController().WIP015DaysCustomerInvoice(customer, start, end)
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                if (apiResponse.response != null) {
                    if (loadMore.equals("0")) {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_SUCCESSFULL, apiResponse.response))
                    } else if (loadMore.equals("1")) {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_LOAD_MORE_SUCCESSFULL, apiResponse.response))
                    }
                } else {
                    if (loadMore.equals("0")) {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_UNSUCCESSFULL, null))
                    } else if (loadMore.equals("1")) {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_COLLECTION_WIP_CUSTOMER_INVOICE_LOAD_MORE_UNSUCCESSFULL, null))
                    }
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