package com.teamcomputers.bam.Requesters.SalesReceivable;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

public class FilterSalesListRequester implements BaseRequester {
    String userId;
    String level;
    String type;
    String RSM;
    String sales;
    String customer;
    String stateCode;
    String product;

    public FilterSalesListRequester(String userId, String level, String type, String RSM, String sales, String customer, String stateCode, String product) {
        this.userId = userId;
        this.level = level;
        this.type = type;
        this.RSM = RSM;
        this.sales = sales;
        this.customer = customer;
        this.stateCode = stateCode;
        this.product = product;
    }

    @Override
    public void run() {
        ApiResponse<Object> apiResponse = HTTPOperationController.filterSalesList(userId, level, type, RSM, sales, customer, stateCode, product);
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                //ArrayList<LogisticDispatchModel> dispatchModel = (ArrayList<LogisticDispatchModel>) apiResponse.getResponse();
                if (apiResponse.getResponse() != null) {
                    EventBus.getDefault().post(new EventObject(Events.GET_FILTER_SALES_LIST_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.GET_FILTER_SALES_LIST_UNSUCCESSFULL, null));
                }
            } else if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                EventBus.getDefault().post(new EventObject(Events.NOT_FOUND, null));
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
