package com.teamcomputers.bam.Requesters.SalesReceivable;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.FiscalYearModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

public class SalesReceivablesFiscalRequester implements BaseRequester {
    String userId;
    String fiscalYear;

    public SalesReceivablesFiscalRequester(String userId, String fiscalYear) {
        this.userId = userId;
        this.fiscalYear = fiscalYear;
    }

    @Override
    public void run() {
        ApiResponse<FiscalYearModel> apiResponse = HTTPOperationController.salesReceiveablesFiscal(userId, fiscalYear);
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (apiResponse.getResponse() != null) {
                    EventBus.getDefault().post(new EventObject(Events.GET_SALES_RECEIVABLE_FISCAL_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.GET_SALES_RECEIVABLE_FISCAL_UNSUCCESSFULL, null));
                }
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
