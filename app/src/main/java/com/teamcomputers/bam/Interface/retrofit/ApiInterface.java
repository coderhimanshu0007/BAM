package com.teamcomputers.bam.Interface.retrofit;

import com.teamcomputers.bam.Models.AppVersionResponse;
import com.teamcomputers.bam.Models.LoginModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

//Interface Used by Retrofit for API interaction With App
public interface ApiInterface {

    String TEAM_WORK_BASE_URL = "http://bam.teamcomputers.com:5558/api/";//"http://192.10.15.57:5558/api/";

    String TEAM_WORK_ECR_URL = "http://192.10.15.169:5556//api/";

    // Login User
    @FormUrlEncoded
    @POST("Login")
    Call<LoginModel> loginUser(@Field("LoginId") String id,
                               @Field("Password") String password);

    // App Version Check
    @POST("GetAppVersion")
    Call<ArrayList<AppVersionResponse>> getCurrentAppVersion();

    // Order Processing Refresh
    @POST("LastRefreshedOrderProcessing")
    Call<Object> orderProcessingRefresh();

    // Order Processing Finance Approval
    @POST("OrderProcessingFinanceApproval")
    Call<Object> orderProcessingFinanceApproval();

    // Order Processing Low Margin
    @POST("OrderProcessingLowMargin")
    Call<Object> orderProcessingLowMargin();

    // Order Processing SO Authorization
    @POST("OrderProcessingSOAuthorization")
    Call<Object> orderProcessingSOAuthorization();

    // Order Processing SPC Submission
    @POST("OrderProcessingSPCSubmission")
    Call<Object> orderProcessingSPCSubmission();

    // Purchase Refresh
    @POST("LastRefreshedOrderProcessing")
    Call<Object> purchaseRefresh();

    // Purchase Sales Order
    @POST("OrderProcessingFinanceApproval")
    Call<Object> purchaseSalesOrder();

    // Purchase Billing
    @POST("OrderProcessingLowMargin")
    Call<Object> purchaseBilling();

    // Purchase Stock
    @POST("OrderProcessingSOAuthorization")
    Call<Object> purchaseStock();

    // Purchase EDD
    @POST("OrderProcessingSPCSubmission")
    Call<Object> purchaseEDD();

    // Logistic Refresh
    @POST("LastRefreshedLogistics")
    Call<Object> logisticsRefresh();

    // Logistic Dispatch
    @POST("LogisticDispatch")
    Call<Object> logisticsDispatch();

    // Logistic In Transit
    @POST("LogisticInTransit")
    Call<Object> logisticsInTransit();

    // Logistic Hold Delivery
    @POST("LogisticHoldDelivery")
    Call<Object> logisticsHoldDelivery();

    // Logistic Acknowledgment
    @POST("LogisticAcknowledgment")
    Call<Object> logisticsAcknowledgement();

    // installation Refresh
    @POST("LastRefreshedInstallation")
    Call<Object> installationRefresh();

    // installation Open Calls
    @POST("InstallationOpenCalls")
    Call<Object> installationOpenCalls();

    // installation WIP
    @POST("InstallationWIP")
    Call<Object> installationWIP();

    // installation DOAIR
    @POST("InstallationDOAIR")
    Call<Object> installationDOAIR();

    // Oinstallation Hold
    @POST("InstallationHold")
    Call<Object> installationHold();

    // Collection Refresh
    @POST("LastRefreshedOrderProcessing")
    Call<Object> collectionRefresh();

    // Collection Outstanding
    @POST("OrderProcessingFinanceApproval")
    Call<Object> collectionOutstanding();

    // Collection Collection
    @POST("OrderProcessingLowMargin")
    Call<Object> collectionCollection();

    // Collection OS Ageing
    @POST("OrderProcessingSOAuthorization")
    Call<Object> collectionOSAgeing();

    // Collection Delivery/Installation
    @POST("OrderProcessingSPCSubmission")
    Call<Object> collectionDeliveryInstallation();

    // Sales Refresh
    @POST("LastRefreshedSales")
    Call<Object> salesRefresh();

    // Receivable Refresh
    @POST("LastRefreshedAccountReceiveables")
    Call<Object> receivableRefresh();

    // Sales Sales
    @POST("Sales")
    Call<Object> salesReceivableSales();

    // Receivable Outstanding
    @POST("AccountReceiveables")
    Call<Object> salesReceivableOutstanding();

    // Sales Receivable
    @FormUrlEncoded
    @POST("SalesReceiveables")
    Call<Object> salesReceivable(@Field("UserId") String userId);

    // Full Sales List
    @FormUrlEncoded
    @POST("FullSalesList")
    Call<Object> fullSalesList(@Field("UserId") String userId,
                               @Field("Level") String level,
                               @Field("Type") String type,
                               @Field("Customer") String customer,
                               @Field("StateCode") String stateCode);

    // Full Sales List
    @FormUrlEncoded
    @POST("FilterSalesList")
    Call<Object> filterSalesList(@Field("UserId") String userId,
                                 @Field("Level") String level,
                                 @Field("Type") String type,
                                 @Field("RSM") String RSM,
                                 @Field("Sales") String sales,
                                 @Field("Customer") String customer,
                                 @Field("StateCode") String stateCode,
                                 @Field("Product") String product);

    // YTD QTD
    @FormUrlEncoded
    @POST("YTDQTD")
    Call<Object> yTDQTD(@Field("UserId") String userId);
}
