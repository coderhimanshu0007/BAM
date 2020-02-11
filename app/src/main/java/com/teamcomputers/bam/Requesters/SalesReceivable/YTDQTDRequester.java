package com.teamcomputers.bam.Requesters.SalesReceivable;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

public class YTDQTDRequester implements BaseRequester {
    String userId;

    public YTDQTDRequester(String userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        ApiResponse<Object> apiResponse = HTTPOperationController.yTDQTD(userId);
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (apiResponse.getResponse() != null) {
                    EventBus.getDefault().post(new EventObject(Events.GET_YTDQTD_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.GET_YTDQTD_UNSUCCESSFULL, null));
                }
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
