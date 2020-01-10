package com.teamcomputers.bam.controllers;


import com.teamcomputers.bam.BAMApplication;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Interface.retrofit.ApiInterface;
import com.teamcomputers.bam.webservice.ApiResponse;
import com.teamcomputers.bam.webservice.ConnectionUtil;


//Controller for API interfacing
public class HTTPOperationController implements BAMConstant {
    private static ApiInterface getApiInterface() {
        return BAMApplication.getInstance().getApiInterface();
    }

    public static ApiResponse loginUser(String tmcId, String password) {
        return ConnectionUtil.execute(getApiInterface().loginUser(tmcId, password));
    }
    public static ApiResponse getCurrentAppVersion() {
        return ConnectionUtil.execute(getApiInterface().getCurrentAppVersion());
    }

    // Order Processing
    public static ApiResponse orderProcessingRefresh() {
        return ConnectionUtil.execute(getApiInterface().orderProcessingRefresh());
    }
    public static ApiResponse orderProessingFinaneApproval() {
        return ConnectionUtil.execute(getApiInterface().orderProcessingFinanceApproval());
    }
    public static ApiResponse orderProessingLowMargin() {
        return ConnectionUtil.execute(getApiInterface().orderProcessingLowMargin());
    }
    public static ApiResponse orderProessingSOAuthorization() {
        return ConnectionUtil.execute(getApiInterface().orderProcessingSOAuthorization());
    }
    public static ApiResponse orderProessingSPCSubmission() {
        return ConnectionUtil.execute(getApiInterface().orderProcessingSPCSubmission());
    }

    // Purchase
    public static ApiResponse purchaseRefresh() {
        return ConnectionUtil.execute(getApiInterface().purchaseRefresh());
    }
    public static ApiResponse purchaseSalesOrder() {
        return ConnectionUtil.execute(getApiInterface().purchaseSalesOrder());
    }
    public static ApiResponse purchaseBilling() {
        return ConnectionUtil.execute(getApiInterface().purchaseBilling());
    }
    public static ApiResponse purchaseStock() {
        return ConnectionUtil.execute(getApiInterface().purchaseStock());
    }
    public static ApiResponse purchaseEDD() {
        return ConnectionUtil.execute(getApiInterface().purchaseEDD());
    }

    // Logistics
    public static ApiResponse logisticsRefresh() {
        return ConnectionUtil.execute(getApiInterface().logisticsRefresh());
    }
    public static ApiResponse logisticsDispatch() {
        return ConnectionUtil.execute(getApiInterface().logisticsDispatch());
    }
    public static ApiResponse logisticsInTransit() {
        return ConnectionUtil.execute(getApiInterface().logisticsInTransit());
    }
    public static ApiResponse logisticsHoldDelivery() {
        return ConnectionUtil.execute(getApiInterface().logisticsHoldDelivery());
    }
    public static ApiResponse logisticsAcknowledgement() {
        return ConnectionUtil.execute(getApiInterface().logisticsAcknowledgement());
    }

    // Installation
    public static ApiResponse installationRefresh() {
        return ConnectionUtil.execute(getApiInterface().installationRefresh());
    }
    public static ApiResponse installationOpenCalls() {
        return ConnectionUtil.execute(getApiInterface().installationOpenCalls());
    }
    public static ApiResponse installationWIP() {
        return ConnectionUtil.execute(getApiInterface().installationWIP());
    }
    public static ApiResponse installationDOAIR() {
        return ConnectionUtil.execute(getApiInterface().installationDOAIR());
    }
    public static ApiResponse installationHold() {
        return ConnectionUtil.execute(getApiInterface().installationHold());
    }

    // Collection
    public static ApiResponse collectionRefresh() {
        return ConnectionUtil.execute(getApiInterface().collectionRefresh());
    }
    public static ApiResponse collectionOutstanding() {
        return ConnectionUtil.execute(getApiInterface().collectionOutstanding());
    }
    public static ApiResponse collectionCollection() {
        return ConnectionUtil.execute(getApiInterface().collectionCollection());
    }
    public static ApiResponse collectionOSAgeing() {
        return ConnectionUtil.execute(getApiInterface().collectionOSAgeing());
    }
    public static ApiResponse collectionDeliveryInstallation() {
        return ConnectionUtil.execute(getApiInterface().collectionDeliveryInstallation());
    }

    /*private static ApiInterface getApiInterface() {
        return TeamWorksApplication.getInstance().getApiInterface();
    }


    public static ApiResponse xanaduRoomType(String input) {
        return ConnectionUtil.execute(getApiInterface().xanaduRoomType(input));
    }

    public static ApiResponse sendConferenceData(ConferenceMeeting conferenceMeeting) {
        return ConnectionUtil.execute(getApiInterface().sendConferenceData(conferenceMeeting));
    }*/

}