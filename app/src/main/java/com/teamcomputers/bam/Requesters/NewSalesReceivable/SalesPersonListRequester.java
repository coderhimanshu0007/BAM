package com.teamcomputers.bam.Requesters.NewSalesReceivable;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

public class SalesPersonListRequester implements BaseRequester {
    String userId;
    String level;
    String type;
    String customer;
    String stateCode;

    public SalesPersonListRequester(String userId, String level, String type, String customer, String stateCode) {
        this.userId = userId;
        this.level = level;
        this.type = type;
        this.customer = customer;
        this.stateCode = stateCode;
    }

    @Override
    public void run() {
        ApiResponse<Object> apiResponse = HTTPOperationController.fullSalesList(userId, level, type, customer, stateCode);
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                //ArrayList<LogisticDispatchModel> dispatchModel = (ArrayList<LogisticDispatchModel>) apiResponse.getResponse();
                if (apiResponse.getResponse() != null) {
                    EventBus.getDefault().post(new EventObject(Events.GET_SALES_PERSON_LIST_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.GET_SALES_PERSON_LIST_UNSUCCESSFULL, null));
                }
            } else if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                EventBus.getDefault().post(new EventObject(Events.NOT_FOUND, null));
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
