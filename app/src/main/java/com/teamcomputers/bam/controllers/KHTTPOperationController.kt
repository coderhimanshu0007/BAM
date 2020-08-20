package com.teamcomputers.bam.controllers

import com.google.gson.JsonObject
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Interface.retrofit.KApiInterface
import com.teamcomputers.bam.KBAMApplication
import com.teamcomputers.bam.Models.AppVersionResponse
import com.teamcomputers.bam.Models.LoginModel
import com.teamcomputers.bam.Models.SessionDetailsModel
import com.teamcomputers.bam.Models.WSModels.NewLoginModel
import com.teamcomputers.bam.webservice.ApiResponse
import com.teamcomputers.bam.webservice.KConnectionUtils
import java.util.*


class KHTTPOperationController : KBAMConstant {
    private fun getApiInterface(): KApiInterface? {
        return KBAMApplication.apiInterface
    }
    /*private fun getApiInterface(): KApiInterface? {
        //val instance : KBAMApplication()
        //instance.getApiInterface()
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
        val client = builder.build()

        var retrofit = Retrofit.Builder()
                .baseUrl("http://bam.teamcomputers.com:5558/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        //apiInterface = retrofit.create(KApiInterface::class.java)
        return retrofit.create(KApiInterface::class.java)
    }*/

    fun getCurrentAppVersion(): ApiResponse<*>? {
        return getApiInterface()?.getCurrentAppVersion()?.let { KConnectionUtils().execute<ArrayList<AppVersionResponse>>(it) }
    }

    fun loginUser(tmcId: String, password: String, deviceId: String): ApiResponse<*>? {
        return getApiInterface()?.loginNewUser(tmcId, password, deviceId)?.let { KConnectionUtils().execute<NewLoginModel>(it) }
    }

    // Order Processing
    fun orderProcessingRefresh(): ApiResponse<*>? {
        return getApiInterface()?.orderProcessingRefresh()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun orderProessingFinaneApproval(): ApiResponse<*>? {
        return getApiInterface()?.orderProcessingFinanceApproval()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun orderProessingLowMargin(): ApiResponse<*>? {
        return getApiInterface()?.orderProcessingLowMargin()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun orderProessingSOAuthorization(): ApiResponse<*>? {
        return getApiInterface()?.orderProcessingSOAuthorization()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun orderProessingSPCSubmission(): ApiResponse<*>? {
        return getApiInterface()?.orderProcessingSPCSubmission()?.let { KConnectionUtils().execute<Any>(it) }
    }

    // Purchase
    fun purchaseRefresh(): ApiResponse<*>? {
        return getApiInterface()?.purchaseRefresh()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun purchaseSalesOrder(): ApiResponse<*>? {
        return getApiInterface()?.purchaseSalesOrder()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun purchaseBilling(): ApiResponse<*>? {
        return getApiInterface()?.purchaseBilling()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun purchaseStock(): ApiResponse<*>? {
        return getApiInterface()?.purchaseStock()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun purchaseEDD(): ApiResponse<*>? {
        return getApiInterface()?.purchaseEDD()?.let { KConnectionUtils().execute<Any>(it) }
    }

    // Logistics
    fun logisticsRefresh(): ApiResponse<*>? {
        return getApiInterface()?.logisticsRefresh()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun logisticsDispatch(): ApiResponse<*>? {
        return getApiInterface()?.logisticsDispatch()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun logisticsInTransit(): ApiResponse<*>? {
        return getApiInterface()?.logisticsInTransit()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun logisticsHoldDelivery(): ApiResponse<*>? {
        return getApiInterface()?.logisticsHoldDelivery()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun logisticsAcknowledgement(): ApiResponse<*>? {
        return getApiInterface()?.logisticsAcknowledgement()?.let { KConnectionUtils().execute<Any>(it) }
    }

    // Installation
    fun installationRefresh(): ApiResponse<*>? {
        return getApiInterface()?.installationRefresh()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun installationOpenCalls(): ApiResponse<*>? {
        return getApiInterface()?.installationOpenCalls()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun installationWIP(): ApiResponse<*>? {
        return getApiInterface()?.installationWIP()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun installationDOAIR(): ApiResponse<*>? {
        return getApiInterface()?.installationDOAIR()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun installationHold(): ApiResponse<*>? {
        return getApiInterface()?.installationHold()?.let { KConnectionUtils().execute<Any>(it) }
    }

    // Collection
    fun collectionRefresh(): ApiResponse<*>? {
        return getApiInterface()?.collectionRefresh()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionOutstanding(): ApiResponse<*>? {
        return getApiInterface()?.collectionOutstanding()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionTotalOutstanding(): ApiResponse<*>? {
        return getApiInterface()?.collectionTotalOutstanding()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionTotalOutstandingInvice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.collectionTotalOutstandingInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionTotalOutstandingCustomerInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.collectionTotalOutstandingCustomerInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionCollectibleOutstanding(): ApiResponse<*>? {
        return getApiInterface()?.collectionCollectibleOutstanding()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionCollectibleOutstandingInvice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.collectionCollectibleOutstandingInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionCollectibleOutStandingCustomerInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.collectionCollectibleOutStandingCustomerInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionOutstandingCurrentMonth(): ApiResponse<*>? {
        return getApiInterface()?.collectionOutstandingCurrentMonth()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionOutstandingCurrentMonthInvice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.collectionOutstandingCurrentMonthInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionCollectibleOutStandingCurrentMonthCustomerInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.collectionCollectibleOutStandingCurrentMonthCustomerInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionOutstandingSubsequentMonth(): ApiResponse<*>? {
        return getApiInterface()?.collectionOutstandingSubsequentMonth()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionOutstandingSubsequentMonthInvoice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.collectionOutstandingSubsequentMonthInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionOutstandingSubsequentMonthCustomerInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.collectionCollectibleOutStandingSubsequentMonthCustomerInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionCollection(): ApiResponse<*>? {
        return getApiInterface()?.collectionCollection()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionECW(): ApiResponse<*>? {
        return getApiInterface()?.collectionECW()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionECWInvoice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.collectionECWInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionECWInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.collectionECWInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionECM(): ApiResponse<*>? {
        return getApiInterface()?.collectionECM()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionECMInvoice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.collectionECMInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionECMInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.collectionECMInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionPCW(): ApiResponse<*>? {
        return getApiInterface()?.collectionPCW()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionPCWInvice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.collectionPCWInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionPCWInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.collectionPCWInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionPCM(): ApiResponse<*>? {
        return getApiInterface()?.collectionPCM()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionPCMInvoice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.collectionPCMInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionPCMInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.collectionPCMInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionOSAgeing(): ApiResponse<*>? {
        return getApiInterface()?.collectionOSAgeing()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun collectionDeliveryInstallation(): ApiResponse<*>? {
        return getApiInterface()?.collectionDeliveryInstallation()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIP015DaysCustomer(): ApiResponse<*>? {
        return getApiInterface()?.WIP015DaysCustomer()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIP015DaysCustomerInvoice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.WIP015DaysCustomerInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIP015DaysCustomerInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.WIP015DaysCustomerInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIP1630DaysCustomer(): ApiResponse<*>? {
        return getApiInterface()?.WIP1630DaysCustomer()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIP1630DaysCustomerInvoice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.WIP1630DaysCustomerInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIP1630DaysCustomerInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.WIP1630DaysCustomerInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIP30DaysCustomer(): ApiResponse<*>? {
        return getApiInterface()?.WIP30DaysCustomer()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIP30DaysCustomerInvoice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.WIP30DaysCustomerInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIP30DaysCustomerInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.WIP30DaysCustomerInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIPPendingDocSubLessThan2DaysCustomer(): ApiResponse<*>? {
        return getApiInterface()?.WIPPendingDocSubLessThan2DaysCustomer()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIPPendingDocSubLessThan2DaysCustomerInvoice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.WIPPendingDocSubLessThan2DaysCustomerInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIPPendingDocSubLessThan2DaysCustomerInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.WIPPendingDocSubLessThan2DaysCustomerInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIPPendingDocSubGreaterThan2DaysCustomer(): ApiResponse<*>? {
        return getApiInterface()?.WIPPendingDocSubGreaterThan2DaysCustomer()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIPPendingDocSubGreaterThan2DaysCustomerInvoice(customer: String, start: String, end: String): ApiResponse<*>? {
        return getApiInterface()?.WIPPendingDocSubGreaterThan2DaysCustomerInvoice(customer, start, end)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun WIPPendingDocSubGreaterThan2DaysCustomerInvoiceSearch(customer: String, invoice: String): ApiResponse<*>? {
        return getApiInterface()?.WIPPendingDocSubGreaterThan2DaysCustomerInvoiceSearch(customer, invoice)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun activeEmployeeAccess(userId: String, userName: String): ApiResponse<*>? {
        return getApiInterface()?.activeEmployeeAccess(userId, userName)?.let { KConnectionUtils().execute<LoginModel>(it) }
    }

    //WS Requesters
    fun fiscalYear(): ApiResponse<*>? {
        return getApiInterface()?.fiscalYearList()?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun salesReceiveablesFiscal(userId: String, fiscalYear: String): ApiResponse<*>? {
        return getApiInterface()?.salesReceiveablesFiscal(userId, fiscalYear)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun fiscalYTDQTD(userId: String, fiscalYear: String): ApiResponse<*>? {
        return getApiInterface()?.fiscalYTDQTD(userId, fiscalYear)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun fiscalSalesList(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, product: String, fiscalYear: String): ApiResponse<*>? {
        return getApiInterface()?.fiscalSalesList(userId, level, type, RSM, sales, customer, stateCode, product, fiscalYear)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun openSalesOrderList(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, invoice: String, product: String): ApiResponse<*>? {
        return getApiInterface()?.openSalesOrderList(userId, level, type, RSM, sales, customer, stateCode, invoice, product)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun outstandingList(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, product: String): ApiResponse<*>? {
        return getApiInterface()?.outstandingList(userId, level, type, RSM, sales, customer, stateCode, product)?.let { KConnectionUtils().execute<Any>(it) }
    }

    // Integrate New API
    fun salesListApr(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, product: String, fiscalYear: String): ApiResponse<*>? {
        return getApiInterface()?.salesListApr(userId, level, type, RSM, sales, customer, stateCode, product, fiscalYear)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun salesOpenOrderApr(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, invoice: String, product: String): ApiResponse<*>? {
        return getApiInterface()?.salesOpenOrderApr(userId, level, type, RSM, sales, customer, stateCode, invoice, product)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun accountReceivablesApr(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, product: String, invoice: String, startIndex: String, endIndex: String): ApiResponse<*>? {
        return getApiInterface()?.accountReceivablesApr(userId, level, type, RSM, sales, customer, stateCode, product, invoice, startIndex, endIndex)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun salesOpenOrderJun(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, product: String, documnetNo: String, startIndex: String, endIndex: String, minAmount: String, maxAmount: String, minNOD: String, maxNOD: String): ApiResponse<*>? {
        return getApiInterface()?.salesOpenOrderJun(userId, level, type, RSM, sales, customer, stateCode, product, documnetNo, startIndex, endIndex, minAmount, maxAmount, minNOD, maxNOD)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun accountReceivablesJun(userId: String, level: String, type: String, RSM: String, sales: String, customer: String, stateCode: String, product: String, invoice: String, startIndex: String, endIndex: String, minAmount: String, maxAmount: String, minNOD: String, maxNOD: String): ApiResponse<*>? {
        return getApiInterface()?.accountReceivablesJun(userId, level, type, RSM, sales, customer, stateCode, product, invoice, startIndex, endIndex, minAmount, maxAmount, minNOD, maxNOD)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun invoiceSerachApr(userId: String, level: String, documnetNo: String): ApiResponse<*>? {
        return getApiInterface()?.invoiceSerachApr(userId, level, documnetNo)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun salesOpenOrderSearch(userId: String, level: String, documnetNo: String): ApiResponse<*>? {
        return getApiInterface()?.salesOpenOrderSearch(userId, level, documnetNo)?.let { KConnectionUtils().execute<Any>(it) }
    }

    fun saveSessionDetail(sessionDetailsModel: JsonObject): ApiResponse<*>? {
        return getApiInterface()?.saveSessionDetail(sessionDetailsModel.toString())?.let { KConnectionUtils().execute<Any>(it) }
    }
}