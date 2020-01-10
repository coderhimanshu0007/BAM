package com.teamcomputers.bam.Requesters.Purchase;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

public class PurchaseEDDRequester implements BaseRequester {

    public PurchaseEDDRequester() {
    }

    @Override
    public void run() {
        ApiResponse<Object> apiResponse = HTTPOperationController.purchaseEDD();
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                //ArrayList<LogisticDispatchModel> dispatchModel = (ArrayList<LogisticDispatchModel>) apiResponse.getResponse();
                if (apiResponse.getResponse() != null) {
                    EventBus.getDefault().post(new EventObject(Events.GET_PURCHASE_EDD_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.GET_PURCHASE_EDD_UNSUCCESSFULL, null));
                }
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
