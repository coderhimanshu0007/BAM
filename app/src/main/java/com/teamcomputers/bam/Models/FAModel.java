package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FAModel {
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

    public FAModel() {
    }




    private class Table {

        @SerializedName("CustomerName")
        @Expose
        private String customerName;
        @SerializedName("BU")
        @Expose
        private String bU;
        @SerializedName("Count")
        @Expose
        private Integer count;
        @SerializedName("Value")
        @Expose
        private Double value;
        @SerializedName("ProjectNo")
        @Expose
        private String projectNo;
        @SerializedName("HoursDays")
        @Expose
        private String hoursDays;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("FinanceDelay")
        @Expose
        private String financeDelay;

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

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFinanceDelay() {
            return financeDelay;
        }

        public void setFinanceDelay(String financeDelay) {
            this.financeDelay = financeDelay;
        }
    }
}
