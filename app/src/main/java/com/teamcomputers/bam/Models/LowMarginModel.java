package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LowMarginModel {
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

    public LowMarginModel() {
    }

    public class Table {

        @SerializedName("CustomerName")
        @Expose
        private String customerName;
        @SerializedName("BU")
        @Expose
        private String bU;
        @SerializedName("Count")
        @Expose
        private Integer count;
        @SerializedName("Amount")
        @Expose
        private Double amount;
        @SerializedName("ProjectNo")
        @Expose
        private String projectNo;
        @SerializedName("HoursDays")
        @Expose
        private String hoursDays;
        @SerializedName("Margin")
        @Expose
        private Double margin;

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getBU() {
            return bU;
        }

        public void setBU(String bU) {
            this.bU = bU;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getProjectNo() {
            return projectNo;
        }

        public void setProjectNo(String projectNo) {
            this.projectNo = projectNo;
        }

        public String getHoursDays() {
            return hoursDays;
        }

        public void setHoursDays(String hoursDays) {
            this.hoursDays = hoursDays;
        }

        public Double getMargin() {
            return margin;
        }

        public void setMargin(Double margin) {
            this.margin = margin;
        }
    }
}
