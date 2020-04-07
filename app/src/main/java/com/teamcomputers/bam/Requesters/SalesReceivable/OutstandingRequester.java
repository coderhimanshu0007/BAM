package com.teamcomputers.bam.Requesters.SalesReceivable;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

public class OutstandingRequester implements BaseRequester {
    String userId;
    String level;
    String type;
    String RSM;
    String sales;
    String customer;
    String stateCode;
    String product;

    public OutstandingRequester(String userId, String level, String type, String RSM, String sales, String customer, String stateCode, String product) {
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
        ApiResponse<Object> apiResponse = HTTPOperationController.outstandingList(userId, level, type, RSM, sales, customer, stateCode, product);
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                if (apiResponse.getResponse() != null) {
                    if (type.equals("RSM")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_RSM_TOS_LIST_SUCCESSFULL, apiResponse.getResponse()));
                    } else if (type.equals("Sales")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_SALES_TOS_LIST_SUCCESSFULL, apiResponse.getResponse()));
                    } else if (type.equals("Customer")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_CUSTOMER_TOS_LIST_SUCCESSFULL, apiResponse.getResponse()));
                    } else if (type.equals("Product")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_PRODUCT_TOS_LIST_SUCCESSFULL, apiResponse.getResponse()));
                    }
                    //EventBus.getDefault().post(new EventObject(Events.GET_OUTSTANDING_LIST_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    if (type.equals("RSM")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_RSM_TOS_LIST_UNSUCCESSFULL, null));
                    } else if (type.equals("Sales")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_SALES_TOS_LIST_UNSUCCESSFULL, null));
                    } else if (type.equals("Customer")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_CUSTOMER_TOS_LIST_UNSUCCESSFULL, null));
                    } else if (type.equals("Product")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_PRODUCT_TOS_LIST_UNSUCCESSFULL, null));
                    }
                    //EventBus.getDefault().post(new EventObject(Events.GET_OUTSTANDING_LIST_UNSUCCESSFULL, null));
                }
            } else if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                EventBus.getDefault().post(new EventObject(Events.NOT_FOUND, null));
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
