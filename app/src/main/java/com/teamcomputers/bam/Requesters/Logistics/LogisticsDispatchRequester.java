package com.teamcomputers.bam.Requesters.Logistics;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.LogisticDispatchModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class LogisticsDispatchRequester implements BaseRequester {

    //private String tmcId;
    //private String password;

    public LogisticsDispatchRequester() {
        //this.tmcId = tmcId;
        //this.password = password;
    }

    @Override
    public void run() {
        ApiResponse<Object> apiResponse = HTTPOperationController.logisticsDispatch();
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                ArrayList<LogisticDispatchModel> dispatchModel = (ArrayList<LogisticDispatchModel>) apiResponse.getResponse();
                if (dispatchModel != null) {
                    EventBus.getDefault().post(new EventObject(Events.GET_LOGISTICS_DISPATCH_SUCCESSFULL, dispatchModel));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.GET_LOGISTICS_DISPATCH_UNSUCCESSFULL, null));
                }
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
