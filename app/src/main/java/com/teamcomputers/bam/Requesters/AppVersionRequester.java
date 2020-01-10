package com.teamcomputers.bam.Requesters;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.AppVersionResponse;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.Utils.BAMUtil;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class AppVersionRequester implements BaseRequester {

    @Override
    public void run() {
        ApiResponse<ArrayList<AppVersionResponse>> apiResponse = HTTPOperationController.getCurrentAppVersion();
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (Float.parseFloat(apiResponse.getResponse().get(0).getAppVersionID()) > BAMUtil.getVersionInfo()) {
                    EventBus.getDefault().post(new EventObject(Events.YOU_ARE_USING_OLDER_VERSION_OF_APP, apiResponse.getResponse().get(0)));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.YOU_ARE_USING_CURRENT_VERSION_OF_APP, null));
                }
            } else {
                EventBus.getDefault().post(new EventObject(Events.OOPS_MESSAGE, null));
            }
        } else {
            EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
        }
    }
}
