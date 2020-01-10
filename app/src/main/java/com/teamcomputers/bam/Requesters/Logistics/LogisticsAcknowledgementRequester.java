package com.teamcomputers.bam.Requesters.Logistics;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.LogisticDispatchModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class LogisticsAcknowledgementRequester implements BaseRequester {

    public LogisticsAcknowledgementRequester() {
    }

    @Override
    public void run() {
        ApiResponse<Object> apiResponse = HTTPOperationController.logisticsAcknowledgement();
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                //ArrayList<LogisticDispatchModel> dispatchModel = (ArrayList<LogisticDispatchModel>) apiResponse.getResponse();
                if (apiResponse.getResponse() != null) {
                    EventBus.getDefault().post(new EventObject(Events.GET_LOGISTICS_ACKNOWLEDGEMENT_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.GET_LOGISTICS_ACKNOWLEDGEMENT_UNSUCCESSFULL, null));
                }
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
