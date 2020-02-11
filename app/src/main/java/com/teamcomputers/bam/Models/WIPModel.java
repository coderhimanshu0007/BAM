package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WIPModel {
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Invoices")
    @Expose
    private String invoices;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("Table")
    @Expose
    private List<Table> table = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInvoices() {
        return invoices;
    }

    public void setInvoices(String invoices) {
        this.invoices = invoices;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<Table> getTable() {
        return table;
    }

    public void setTable(List<Table> table) {
        this.table = table;
    }

    public class Table {

        @SerializedName("InvoiceNo")
        @Expose
        private String invoiceNo;
        @SerializedName("CustomerName")
        @Expose
        private String customerName;
        @SerializedName("PaymentStatus")
        @Expose
        private String paymentStatus;
        @SerializedName("Count")
        @Expose
        private String count;
        @SerializedName("Amount")
        @Expose
        private String amount;
        @SerializedName("ZoneName")
        @Expose
        private String zoneName;
        @SerializedName("NOD")
        @Expose
        private String nOD;
        @SerializedName("RemainingAmount")
        @Expose
        private String remainingAmount;
        @SerializedName("Reason")
        @Expose
        private String reason;
        @SerializedName("City")
        @Expose
        private String city;

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

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getZoneName() {
            return zoneName;
        }

        public void setZoneName(String zoneName) {
            this.zoneName = zoneName;
        }

        public String getNOD() {
            return nOD;
        }

        public void setNOD(String nOD) {
            this.nOD = nOD;
        }

        public String getRemainingAmount() {
            return remainingAmount;
        }

        public void setRemainingAmount(String remainingAmount) {
            this.remainingAmount = remainingAmount;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    }
}
