package com.teamcomputers.bam.Requesters.SalesReceivable;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

public class FullSalesListRequester implements BaseRequester {
    String userId;
    String level;
    String type;

    public FullSalesListRequester(String userId, String level, String type) {
        this.userId = userId;
        this.level = level;
        this.type = type;
    }

    @Override
    public void run() {
        ApiResponse<Object> apiResponse = HTTPOperationController.fullSalesList(userId, level, type);
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                //ArrayList<LogisticDispatchModel> dispatchModel = (ArrayList<LogisticDispatchModel>) apiResponse.getResponse();
                if (apiResponse.getResponse() != null) {
                    EventBus.getDefault().post(new EventObject(Events.GET_FULL_SALES_LIST_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.GET_FULL_SALES_LIST_UNSUCCESSFULL, null));
                }
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
