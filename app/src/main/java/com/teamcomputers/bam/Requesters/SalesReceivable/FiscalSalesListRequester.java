package com.teamcomputers.bam.Requesters.SalesReceivable;

import com.teamcomputers.bam.Interface.BaseRequester;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.controllers.HTTPOperationController;
import com.teamcomputers.bam.webservice.ApiResponse;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

public class FiscalSalesListRequester implements BaseRequester {
    String userId;
    String level;
    String type;
    String RSM;
    String sales;
    String customer;
    String stateCode;
    String product;
    String fiscalYear;

    public FiscalSalesListRequester(String userId, String level, String type, String RSM, String sales, String customer, String stateCode, String product, String fiscalYear) {
        this.userId = userId;
        this.level = level;
        this.type = type;
        this.RSM = RSM;
        this.sales = sales;
        this.customer = customer;
        this.stateCode = stateCode;
        this.product = product;
        this.fiscalYear = fiscalYear;
    }

    @Override
    public void run() {
        ApiResponse<Object> apiResponse = HTTPOperationController.fiscalSalesList(userId, level, type, RSM, sales, customer, stateCode, product, fiscalYear);
        if (apiResponse != null) {
            if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Log.e("Customer url",""+apiResponse.getResponseCode());
                //ArrayList<LogisticDispatchModel> dispatchModel = (ArrayList<LogisticDispatchModel>) apiResponse.getResponse();
                if (apiResponse.getResponse() != null) {
                    if (type.equals("RSM")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_RSM_LIST_SUCCESSFULL, apiResponse.getResponse()));
                    } else if (type.equals("Sales")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_SALES_LIST_SUCCESSFULL, apiResponse.getResponse()));
                    } else if (type.equals("Customer")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_CUSTOMER_LIST_SUCCESSFULL, apiResponse.getResponse()));
                    } else if (type.equals("Product")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_PRODUCT_LIST_SUCCESSFULL, apiResponse.getResponse()));
                    }
                    //EventBus.getDefault().post(new EventObject(Events.GET_FILTER_SALES_LIST_SUCCESSFULL, apiResponse.getResponse()));
                } else {
                    if (type.equals("RSM")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_RSM_LIST_UNSUCCESSFULL, null));
                    } else if (type.equals("Sales")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_SALES_LIST_UNSUCCESSFULL, null));
                    } else if (type.equals("Customer")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_CUSTOMER_LIST_UNSUCCESSFULL, null));
                    } else if (type.equals("Product")) {
                        EventBus.getDefault().post(new EventObject(Events.GET_PRODUCT_LIST_UNSUCCESSFULL, null));
                    }
                    //EventBus.getDefault().post(new EventObject(Events.GET_FILTER_SALES_LIST_UNSUCCESSFULL, null));
                }
            } else if (apiResponse.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                EventBus.getDefault().post(new EventObject(Events.NOT_FOUND, null));
            } else {
                EventBus.getDefault().post(new EventObject(Events.NO_INTERNET_CONNECTION, null));
            }
        }
    }
}
