package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HoldModel {
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
        @SerializedName("OnHoldAmount")
        @Expose
        private Double onHoldAmount;
        @SerializedName("NOD")
        @Expose
        private Integer nOD;
        @SerializedName("RemainingAmount")
        @Expose
        private Double remainingAmount;
        @SerializedName("ZoneName")
        @Expose
        private String zoneName;
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

        public Double getOnHoldAmount() {
            return onHoldAmount;
        }

        public void setOnHoldAmount(Double onHoldAmount) {
            this.onHoldAmount = onHoldAmount;
        }

        public Integer getNOD() {
            return nOD;
        }

        public void setNOD(Integer nOD) {
            this.nOD = nOD;
        }

        public Double getRemainingAmount() {
            return remainingAmount;
        }

        public void setRemainingAmount(Double remainingAmount) {
            this.remainingAmount = remainingAmount;
        }

        public String getZoneName() {
            return zoneName;
        }

        public void setZoneName(String zoneName) {
            this.zoneName = zoneName;
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
