package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SPCSModel {
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

    public SPCSModel() {
    }

    public class Table {

        @SerializedName("CustomerName")
        @Expose
        private String customerName;
        @SerializedName("Count")
        @Expose
        private Integer count;
        @SerializedName("ProjectNo")
        @Expose
        private String projectNo;
        @SerializedName("HoursDays")
        @Expose
        private String hoursDays;
        @SerializedName("Reasons")
        @Expose
        private String reasons;
        @SerializedName("ProjectHeadName")
        @Expose
        private String projectHeadName;

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
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

        public String getReasons() {
            return reasons;
        }

        public void setReasons(String reasons) {
            this.reasons = reasons;
        }

        public String getProjectHeadName() {
            return projectHeadName;
        }

        public void setProjectHeadName(String projectHeadName) {
            this.projectHeadName = projectHeadName;
        }
    }
}
