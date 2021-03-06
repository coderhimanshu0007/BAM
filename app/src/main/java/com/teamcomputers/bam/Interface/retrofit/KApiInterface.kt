package com.teamcomputers.bam.Interface.retrofit

import com.teamcomputers.bam.Models.*
import com.teamcomputers.bam.Models.WSModels.NewLoginModel
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface KApiInterface {

    // App Version Check
    @POST("GetAppVersion")
    abstract fun getCurrentAppVersion(): Call<ArrayList<AppVersionResponse>>

    // Login User
    @FormUrlEncoded
    @POST("Login")
    fun loginUser(@Field("LoginId") id: String,
                  @Field("Password") password: String): Call<LoginModel>

    // New Login User
    @FormUrlEncoded
    @POST("LoginNew")
    fun loginNewUser(@Field("LoginId") id: String,
                     @Field("Password") password: String,
                     @Field("DeviceId") deviceId: String): Call<NewLoginModel>

    // ActiveEmployeeAccess
    @FormUrlEncoded
    @POST("ActiveEmployeeAccess")
    fun activeEmployeeAccess(@Field("UserId") userId: String,
                             @Field("UserName") userName: String): Call<ActiveEmployeeAccessModel>

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
    @POST("CollectionOutStandingNew")
    abstract fun collectionOutstanding(): Call<Any>

    // Collection Total Outstanding
    @POST("CollectionTotalOutStandingCustomer")
    abstract fun collectionTotalOutstanding(): Call<Any>

    // Collection Outstanding
    @FormUrlEncoded
    @POST("CollectionTotalOutStandingCustomerInvoice")
    abstract fun collectionTotalOutstandingInvoice(@Field("Customer") customer: String,
                                                   @Field("Start") start: String,
                                                   @Field("End") end: String): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("CollectionTotalOutStandingCustomerInvoiceSearch")
    abstract fun collectionTotalOutstandingCustomerInvoiceSearch(@Field("Customer") customer: String,
                                                                 @Field("Invoice") invoice: String): Call<Any>

    // Collection Total Outstanding
    @POST("CollectionCollectibleOutStandingCustomerNew")
    abstract fun collectionCollectibleOutstanding(): Call<Any>

    // Collection Outstanding
    @FormUrlEncoded
    @POST("CollectionCollectibleOutStandingCustomerInvoiceNew")
    abstract fun collectionCollectibleOutstandingInvoice(@Field("Customer") customer: String,
                                                         @Field("Start") start: String,
                                                         @Field("End") end: String): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("CollectionCollectibleOutStandingCustomerInvoiceSearchNew")
    abstract fun collectionCollectibleOutStandingCustomerInvoiceSearch(@Field("Customer") customer: String,
                                                                       @Field("Invoice") invoice: String): Call<Any>

    // Collection Outstanding Current Month
    @POST("CollectionCollectibleOutStandingCurrentMonthCustomerNew")
    abstract fun collectionOutstandingCurrentMonth(): Call<Any>

    // Collection Outstanding
    @FormUrlEncoded
    @POST("CollectionCollectibleOutStandingCurrentMonthCustomerInvoiceNew")
    abstract fun collectionOutstandingCurrentMonthInvoice(@Field("Customer") customer: String,
                                                          @Field("Start") start: String,
                                                          @Field("End") end: String): Call<Any>

    // Collection Outstanding Current Month
    @FormUrlEncoded
    @POST("CollectionCollectibleOutStandingCurrentMonthCustomerInvoiceSearchNew")
    abstract fun collectionCollectibleOutStandingCurrentMonthCustomerInvoiceSearch(@Field("Customer") customer: String,
                                                                                   @Field("Invoice") invoice: String): Call<Any>

    // Collection Outstanding
    @POST("CollectionCollectibleOutStandingSubsequentMonthCustomerNew")
    abstract fun collectionOutstandingSubsequentMonth(): Call<Any>

    // Collection Outstanding
    @FormUrlEncoded
    @POST("CollectionCollectibleOutStandingSubsequentMonthCustomerInvoiceNew")
    abstract fun collectionOutstandingSubsequentMonthInvoice(@Field("Customer") customer: String,
                                                             @Field("Start") start: String,
                                                             @Field("End") end: String): Call<Any>

    // Collection Outstanding
    @FormUrlEncoded
    @POST("CollectionCollectibleOutStandingSubsequentMonthCustomerInvoiceSearchNew")
    abstract fun collectionCollectibleOutStandingSubsequentMonthCustomerInvoiceSearch(@Field("Customer") customer: String,
                                                                                      @Field("Invoice") invoice: String): Call<Any>

    // Collection Total Outstanding
    @POST("WIP015DaysCustomer")
    abstract fun WIP015DaysCustomer(): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIP015DaysCustomerInvoice")
    abstract fun WIP015DaysCustomerInvoice(@Field("Customer") customer: String,
                                           @Field("Start") start: String,
                                           @Field("End") end: String): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIP015DaysCustomerInvoiceSearch")
    abstract fun WIP015DaysCustomerInvoiceSearch(@Field("Customer") customer: String,
                                                 @Field("Invoice") invoice: String): Call<Any>

    // Collection Total Outstanding
    @POST("WIP1630DaysCustomer")
    abstract fun WIP1630DaysCustomer(): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIP1630DaysCustomerInvoice")
    abstract fun WIP1630DaysCustomerInvoice(@Field("Customer") customer: String,
                                            @Field("Start") start: String,
                                            @Field("End") end: String): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIP1630DaysCustomerInvoiceSearch")
    abstract fun WIP1630DaysCustomerInvoiceSearch(@Field("Customer") customer: String,
                                                  @Field("Invoice") invoice: String): Call<Any>

    // Collection Total Outstanding
    @POST("WIP30DaysCustomer")
    abstract fun WIP30DaysCustomer(): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIP30DaysCustomerInvoice")
    abstract fun WIP30DaysCustomerInvoice(@Field("Customer") customer: String,
                                          @Field("Start") start: String,
                                          @Field("End") end: String): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIP30DaysCustomerInvoiceSearch")
    abstract fun WIP30DaysCustomerInvoiceSearch(@Field("Customer") customer: String,
                                                @Field("Invoice") invoice: String): Call<Any>

    // Collection Total Outstanding
    @POST("WIPPendingDocSubLessThan2DaysCustomer")
    abstract fun WIPPendingDocSubLessThan2DaysCustomer(): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIPPendingDocSubLessThan2DaysCustomerInvoice")
    abstract fun WIPPendingDocSubLessThan2DaysCustomerInvoice(@Field("Customer") customer: String,
                                                              @Field("Start") start: String,
                                                              @Field("End") end: String): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIPPendingDocSubLessThan2DaysCustomerInvoiceSearch")
    abstract fun WIPPendingDocSubLessThan2DaysCustomerInvoiceSearch(@Field("Customer") customer: String,
                                                                    @Field("Invoice") invoice: String): Call<Any>

    // Collection Total Outstanding
    @POST("WIPPendingDocSubGreaterThan2DaysCustomer")
    abstract fun WIPPendingDocSubGreaterThan2DaysCustomer(): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIPPendingDocSubGreaterThan2DaysCustomerInvoice")
    abstract fun WIPPendingDocSubGreaterThan2DaysCustomerInvoice(@Field("Customer") customer: String,
                                                                 @Field("Start") start: String,
                                                                 @Field("End") end: String): Call<Any>

    // Collection Total Outstanding
    @FormUrlEncoded
    @POST("WIPPendingDocSubGreaterThan2DaysCustomerInvoiceSearch")
    abstract fun WIPPendingDocSubGreaterThan2DaysCustomerInvoiceSearch(@Field("Customer") customer: String,
                                                                       @Field("Invoice") invoice: String): Call<Any>

    // Collection Collection
    @POST("CollectionCollection")
    abstract fun collectionCollection(): Call<Any>

    // Collection Collection
    @POST("ExpectedCollectionThisWeekCustomer")
    abstract fun collectionECW(): Call<Any>

    // Collection Collection
    @FormUrlEncoded
    @POST("ExpectedCollectionThisWeekCustomerInvoice")
    abstract fun collectionECWInvoice(@Field("Customer") customer: String,
                                      @Field("Start") start: String,
                                      @Field("End") end: String): Call<Any>

    // Collection Collection
    @FormUrlEncoded
    @POST("ExpectedCollectionThisWeekCustomerInvoiceSearch")
    abstract fun collectionECWInvoiceSearch(@Field("Customer") customer: String,
                                            @Field("Invoice") invoice: String): Call<Any>

    // Collection Collection
    @POST("ExpectedCollectionThisMonthCustomer")
    abstract fun collectionECM(): Call<Any>

    // Collection Collection
    @FormUrlEncoded
    @POST("ExpectedCollectionThisMonthCustomerInvoice")
    abstract fun collectionECMInvoice(@Field("Customer") customer: String,
                                      @Field("Start") start: String,
                                      @Field("End") end: String): Call<Any>

    // Collection Collection
    @FormUrlEncoded
    @POST("ExpectedCollectionThisMonthCustomerInvoiceSearch")
    abstract fun collectionECMInvoiceSearch(@Field("Customer") customer: String,
                                            @Field("Invoice") invoice: String): Call<Any>

    // Collection Collection
    @POST("PaymentCollectedThisWeekCustomer")
    abstract fun collectionPCW(): Call<Any>

    // Collection Collection
    @FormUrlEncoded
    @POST("PaymentCollectedThisWeekCustomerInvoice")
    abstract fun collectionPCWInvoice(@Field("Customer") customer: String,
                                      @Field("Start") start: String,
                                      @Field("End") end: String): Call<Any>

    // Collection Collection
    @FormUrlEncoded
    @POST("PaymentCollectedThisWeekCustomerInvoiceSearch")
    abstract fun collectionPCWInvoiceSearch(@Field("Customer") customer: String,
                                            @Field("Invoice") invoice: String): Call<Any>

    // Collection Collection
    @POST("PaymentCollectedThisMonthCustomer")
    abstract fun collectionPCM(): Call<Any>

    // Collection Collection
    @FormUrlEncoded
    @POST("PaymentCollectedThisMonthCustomerInvoice")
    abstract fun collectionPCMInvoice(@Field("Customer") customer: String,
                                      @Field("Start") start: String,
                                      @Field("End") end: String): Call<Any>

    // Collection Collection
    @FormUrlEncoded
    @POST("PaymentCollectedThisMonthCustomerInvoiceSearch")
    abstract fun collectionPCMInvoiceSearch(@Field("Customer") customer: String,
                                            @Field("Invoice") invoice: String): Call<Any>

    // Collection OS Ageing
    @POST("CollectionAgeing")
    abstract fun collectionOSAgeing(): Call<Any>

    // Collection Delivery/Installation
    @POST("WIP")
    abstract fun collectionDeliveryInstallation(): Call<Any>

    // Fiscal year list
    @POST("FiscalYearList")
    abstract fun fiscalYearList(): Call<FiscalYearModel>

    // YTD QTD
    // Replace SalesReceiveablesFiscal to SalesReceiveablesFiscalJun
    @FormUrlEncoded
    @POST("SalesReceiveablesFiscalJun")
    abstract fun salesReceiveablesFiscal(@Field("UserId") userId: String,
                                         @Field("FiscalYear") fiscalYear: String): Call<Any>

    // Replace YTDQTDFiscal to YTDQTDFiscalJun
    // YTD QTD
    @FormUrlEncoded
    @POST("YTDQTDFiscalJun")
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
    // Replace SalesListApr to SalesListJun
    @FormUrlEncoded
    @POST("SalesListJun")
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
                              @Field("Product") product: String,
                              @Field("DocumnetNo") invoice: String,
                              @Field("StartIndex") startIndex: String,
                              @Field("EndIndex") endIndex: String): Call<Any>

    // New Open Sales Order List
    @FormUrlEncoded
    @POST("SalesOpenOrderJun")
    fun salesOpenOrderJun(@Field("UserId") userId: String,
                          @Field("Level") level: String,
                          @Field("Type") type: String,
                          @Field("RSM") RSM: String,
                          @Field("Sales") sales: String,
                          @Field("Customer") customer: String,
                          @Field("StateCode") stateCode: String,
                          @Field("Product") product: String,
                          @Field("DocumnetNo") documnetNo: String,
                          @Field("StartIndex") startIndex: String,
                          @Field("EndIndex") endIndex: String,
                          @Field("MinAmount") minAmount: String,
                          @Field("MaxAmount") maxAmount: String,
                          @Field("MinNOD") minNOD: String,
                          @Field("MaxNOD") maxNOD: String): Call<Any>

    // New Outstanding List
    @FormUrlEncoded
    @POST("AccountReceivablesJun")
    fun accountReceivablesJun(@Field("UserId") userId: String,
                              @Field("Level") level: String,
                              @Field("Type") type: String,
                              @Field("RSM") RSM: String,
                              @Field("Sales") sales: String,
                              @Field("Customer") customer: String,
                              @Field("StateCode") stateCode: String,
                              @Field("Product") product: String,
                              @Field("DocumnetNo") invoice: String,
                              @Field("StartIndex") startIndex: String,
                              @Field("EndIndex") endIndex: String,
                              @Field("MinAmount") minAmount: String,
                              @Field("MaxAmount") maxAmount: String,
                              @Field("MinNOD") minNOD: String,
                              @Field("MaxNOD") maxNOD: String): Call<Any>

    // Invoice Search Net Receivable
    @FormUrlEncoded
    @POST("AccountReceivablesAprInvoiceSearch")
    fun invoiceSerachApr(@Field("UserId") userId: String,
                         @Field("Level") level: String,
                         @Field("DocumnetNo") type: String): Call<Any>

    // Invoice Search Pending Sales Order
    @FormUrlEncoded
    @POST("SalesOpenOrderSearch")
    fun salesOpenOrderSearch(@Field("UserId") userId: String,
                             @Field("Level") level: String,
                             @Field("DocumnetNo") type: String): Call<Any>

    @Headers("Content-Type: application/json")
    @POST("SaveSessionDetails")
    fun saveSessionDetail(@Body sessionDetailsModel: String): Call<Any>
}