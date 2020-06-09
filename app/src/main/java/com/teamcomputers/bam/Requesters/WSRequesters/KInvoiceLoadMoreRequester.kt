package com.teamcomputers.bam.Requesters.WSRequesters

import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.KBaseRequester
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.controllers.KHTTPOperationController
import org.greenrobot.eventbus.EventBus
import java.net.HttpURLConnection

class KInvoiceLoadMoreRequester(var userId: String, var level: String, var type: String, var RSM: String, var sales: String, var customer: String, var stateCode: String, var product: String, var invoice: String, var startIndex: String, var endIndex: String, var minAmount: String, var maxAmount: String, var minNOD: String, var maxNOD: String) : KBaseRequester {

    override fun run() {
        val apiResponse = KHTTPOperationController().accountReceivablesJun(userId, level, type, RSM, sales, customer, stateCode, product, invoice, startIndex, endIndex, minAmount, maxAmount, minNOD, maxNOD)
        if (apiResponse != null) {
            if (apiResponse.responseCode == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                if (apiResponse.response != null) {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_INVOICE_LOAD_MORE_SUCCESSFULL, apiResponse.response))
                } else {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.GET_INVOICE_LOAD_MORE_UNSUCCESSFULL, null))
                }
            } else if (apiResponse.responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NOT_FOUND, null))
            } else {
                EventBus.getDefault().post(EventObject(KBAMConstant.Events.NO_INTERNET_CONNECTION, null))
            }
        }
    }
}