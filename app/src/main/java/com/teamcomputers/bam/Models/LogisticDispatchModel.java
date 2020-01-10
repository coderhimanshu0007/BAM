
package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LogisticDispatchModel {
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Invoices")
    @Expose
    private Integer invoices;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("Table")
    @Expose
    private List<Table> table = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getInvoices() {
        return invoices;
    }

    public void setInvoices(Integer invoices) {
        this.invoices = invoices;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<Table> getTable() {
        return table;
    }

    public void setTable(List<Table> table) {
        this.table = table;
    }


    public static class Table {

        @SerializedName("InvoiceNo")
        @Expose
        private String invoiceNo;
        @SerializedName("CustomerName")
        @Expose
        private String customerName;
        @SerializedName("FromCity")
        @Expose
        private String fromCity;
        @SerializedName("HoursDays")
        @Expose
        private String hoursDays;
        @SerializedName("DispatchPending")
        @Expose
        private Integer dispatchPending;
        @SerializedName("DispatchPendingValue")
        @Expose
        private Double dispatchPendingValue;

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getFromCity() {
            return fromCity;
        }

        public void setFromCity(String fromCity) {
            this.fromCity = fromCity;
        }

        public String getHoursDays() {
            return hoursDays;
        }

        public void setHoursDays(String hoursDays) {
            this.hoursDays = hoursDays;
        }

        public Integer getDispatchPending() {
            return dispatchPending;
        }

        public void setDispatchPending(Integer dispatchPending) {
            this.dispatchPending = dispatchPending;
        }

        public Double getDispatchPendingValue() {
            return dispatchPendingValue;
        }

        public void setDispatchPendingValue(Double dispatchPendingValue) {
            this.dispatchPendingValue = dispatchPendingValue;
        }
    }

}

