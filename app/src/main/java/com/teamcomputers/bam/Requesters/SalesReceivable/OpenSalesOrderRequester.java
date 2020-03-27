package com.teamcomputers.bam.Requesters.SalesReceivable;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

public class OpenSalesOrderRequester implements BaseRequester {
    String userId;
    String level;
    String type;
    String RSM;
    String sales;
    String customer;
    String stateCode;
    String invoice;
    String product;

    public OpenSalesOrderRequester(String userId, String level, String type, String RSM, String sales, String customer, String stateCode, String invoice, String product) {
        this.userId = userId;
        this.level = level;
        this.type = type;
        this.RSM = RSM;
        this.sales = sales;
        this.customer = customer;
        this.stateCode = stateCode;
        this.invoice = invoice;
        this.product = product;
    }

    @Override
    public void run() {
        ApiResponse<Object> apiResponse = HTTPOperationController.openSalesOrderList(userId, level, type, RSM, sales, customer, stateCode, invoice, product);
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                if (apiResponse.getResponse() != null) {
                    EventBus.getDefault().post(new EventObject(Events.GET_OPEN_SALES_ORDER_LIST_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    EventBus.getDefault().post(new EventObject(Events.GET_OPEN_SALES_ORDER_LIST_UNSUCCESSFULL, null));
                }
            } else if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                EventBus.getDefault().post(new EventObject(Events.NOT_FOUND, null));
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
