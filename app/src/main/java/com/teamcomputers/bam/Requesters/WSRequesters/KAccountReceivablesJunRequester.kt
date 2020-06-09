package com.teamcomputers.bam.Requesters.WSRequesters

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KAccountReceivablesJunRequester(var userId: String, var level: String, var type: String, var RSM: String, var sales: String, var customer: String, var stateCode: String, var product: String, var invoice: String, var startIndex: String, var endIndex: String, var minAmount: String, var maxAmount: String, var minNOD: String, var maxNOD: String) : KBaseRequester {

    override fun run() {
        val apiResponse = KHTTPOperationController().accountReceivablesJun(userId, level, type, RSM, sales, customer, stateCode, product, invoice, startIndex, endIndex, minAmount, maxAmount, minNOD, maxNOD)
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
                    } else if (type == "Invoice") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_INVOICE_TOS_LIST_SUCCESSFULL, apiResponse.response))
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
                    } else if (type == "Invoice") {
                        EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_INVOICE_TOS_LIST_UNSUCCESSFULL, null))
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