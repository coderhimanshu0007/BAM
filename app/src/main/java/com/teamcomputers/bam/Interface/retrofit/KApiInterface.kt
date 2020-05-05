package com.teamcomputers.bam.Interface.retrofit

import com.teamcomputers.bam.Models.AppVersionResponse
import com.teamcomputers.bam.Models.FiscalYearModel
import com.teamcomputers.bam.Models.LoginModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.ArrayList

interface KApiInterface {

    // App Version Check
    @POST("GetAppVersion")
    abstract fun getCurrentAppVersion(): Call<ArrayList<AppVersionResponse>>

    // Login User
    @FormUrlEncoded
    @POST("Login")
    fun loginUser(@Field("LoginId") id: String,
                           @Field("Password") password: String): Call<LoginModel>


    // Order Processing Refresh
    @POST("LastRefreshedOrderProcessing")
    abstract fun orderProcessingRefresh(): Call<Any>

    // Order Processing Finance Approval
    @POST("OrderProcessingFinanceApproval")
    abstract fun orderProcessingFinanceApproval(): Call<Any>

    // Order Processing Low Margin
    @POST("OrderProcessingLowMargin")
    abstract fun orderProcessingLowMargin(): Call<Any>

    // Order Processing SO Authorization
    @POST("OrderProcessingSOAuthorization")
    abstract fun orderProcessingSOAuthorization(): Call<Any>

    // Order Processing SPC Submission
    @POST("OrderProcessingSPCSubmission")
    abstract fun orderProcessingSPCSubmission(): Call<Any>

    // Purchase Refresh
    @POST("LastRefreshedOrderProcessing")
    abstract fun purchaseRefresh(): Call<Any>

    // Purchase Sales Order
    @POST("OrderProcessingFinanceApproval")
    abstract fun purchaseSalesOrder(): Call<Any>

    // Purchase Billing
    @POST("OrderProcessingLowMargin")
    abstract fun purchaseBilling(): Call<Any>

    // Purchase Stock
    @POST("OrderProcessingSOAuthorization")
    abstract fun purchaseStock(): Call<Any>

    // Purchase EDD
    @POST("OrderProcessingSPCSubmission")
    abstract fun purchaseEDD(): Call<Any>

    // Logistic Refresh
    @POST("LastRefreshedLogistics")
    abstract fun logisticsRefresh(): Call<Any>

    // Logistic Dispatch
    @POST("LogisticDispatch")
    abstract fun logisticsDispatch(): Call<Any>

    // Logistic In Transit
    @POST("LogisticInTransit")
    abstract fun logisticsInTransit(): Call<Any>

    // Logistic Hold Delivery
    @POST("LogisticHoldDelivery")
    abstract fun logisticsHoldDelivery(): Call<Any>

    // Logistic Acknowledgment
    @POST("LogisticAcknowledgment")
    abstract fun logisticsAcknowledgement(): Call<Any>

    // installation Refresh
    @POST("LastRefreshedInstallation")
    abstract fun installationRefresh(): Call<Any>

    // installation Open Calls
    @POST("InstallationOpenCalls")
    abstract fun installationOpenCalls(): Call<Any>

    // installation WIP
    @POST("InstallationWIP")
    abstract fun installationWIP(): Call<Any>

    // installation DOAIR
    @POST("InstallationDOAIR")
    abstract fun installationDOAIR(): Call<Any>

    // Oinstallation Hold
    @POST("InstallationHold")
    abstract fun installationHold(): Call<Any>

    // Collection Refresh
    @POST("LastRefreshedOrderProcessing")
    abstract fun collectionRefresh(): Call<Any>

    // Collection Outstanding
    @POST("OrderProcessingFinanceApproval")
    abstract fun collectionOutstanding(): Call<Any>

    // Collection Collection
    @POST("OrderProcessingLowMargin")
    abstract fun collectionCollection(): Call<Any>

    // Collection OS Ageing
    @POST("OrderProcessingSOAuthorization")
    abstract fun collectionOSAgeing(): Call<Any>

    // Collection Delivery/Installation
    @POST("OrderProcessingSPCSubmission")
    abstract fun collectionDeliveryInstallation(): Call<Any>

    // Fiscal year list
    @POST("FiscalYearList")
    abstract fun fiscalYearList(): Call<FiscalYearModel>

    // YTD QTD
    @FormUrlEncoded
    @POST("SalesReceiveablesFiscal")
    abstract fun salesReceiveablesFiscal(@Field("UserId") userId: String,
                                         @Field("FiscalYear") fiscalYear: String): Call<Any>

    // YTD QTD
    @FormUrlEncoded
    @POST("YTDQTDFiscal")
    abstract fun fiscalYTDQTD(@Field("UserId") userId: String,
                              @Field("FiscalYear") fiscalYear: String): Call<Any>

    // Fiscal Sales List
    @FormUrlEncoded
    @POST("FiscalSalesList")
    abstract fun fiscalSalesList(@Field("UserId") userId: String,
                                 @Field("Level") level: String,
                                 @Field("Type") type: String,
                                 @Field("RSM") RSM: String,
                                 @Field("Sales") sales: String,
                                 @Field("Customer") customer: String,
                                 @Field("StateCode") stateCode: String,
                                 @Field("Product") product: String,
                                 @Field("FiscalYear") fiscalYear: String): Call<Any>

    // Open Sales Order List
    @FormUrlEncoded
    @POST("SalesOpenOrderList")
    abstract fun openSalesOrderList(@Field("UserId") userId: String,
                                    @Field("Level") level: String,
                                    @Field("Type") type: String,
                                    @Field("RSM") RSM: String,
                                    @Field("Sales") sales: String,
                                    @Field("Customer") customer: String,
                                    @Field("StateCode") stateCode: String,
                                    @Field("Invoice") invoice: String,
                                    @Field("Product") product: String): Call<Any>

    // Outstanding List
    @FormUrlEncoded
    @POST("AccountReceivablesList")
    abstract fun outstandingList(@Field("UserId") userId: String,
                                 @Field("Level") level: String,
                                 @Field("Type") type: String,
                                 @Field("RSM") RSM: String,
                                 @Field("Sales") sales: String,
                                 @Field("Customer") customer: String,
                                 @Field("StateCode") stateCode: String,
                                 @Field("Product") product: String): Call<Any>

    // New Sales List
    @FormUrlEncoded
    @POST("SalesListApr")
    fun salesListApr(@Field("UserId") userId: String,
                     @Field("Level") level: String,
                     @Field("Type") type: String,
                     @Field("RSM") RSM: String,
                     @Field("Sales") sales: String,
                     @Field("Customer") customer: String,
                     @Field("StateCode") stateCode: String,
                     @Field("Product") product: String,
                     @Field("FiscalYear") fiscalYear: String): Call<Any>

    // New Open Sales Order List
    @FormUrlEncoded
    @POST("SalesOpenOrderApr")
    fun salesOpenOrderApr(@Field("UserId") userId: String,
                          @Field("Level") level: String,
                          @Field("Type") type: String,
                          @Field("RSM") RSM: String,
                          @Field("Sales") sales: String,
                          @Field("Customer") customer: String,
                          @Field("StateCode") stateCode: String,
                          @Field("SONumber") SONumber: String,
                          @Field("Product") product: String): Call<Any>

    // New Outstanding List
    @FormUrlEncoded
    @POST("AccountReceivablesApr")
    fun accountReceivablesApr(@Field("UserId") userId: String,
                              @Field("Level") level: String,
                              @Field("Type") type: String,
                              @Field("RSM") RSM: String,
                              @Field("Sales") sales: String,
                              @Field("Customer") customer: String,
                              @Field("StateCode") stateCode: String,
                              @Field("Product") product: String): Call<Any>
}