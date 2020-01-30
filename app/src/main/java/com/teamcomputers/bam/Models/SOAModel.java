package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SOAModel {
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

        @SerializedName("No")
        @Expose
        private String no;
        @SerializedName("Count")
        @Expose
        private Integer count;
        @SerializedName("Value")
        @Expose
        private Double value;
        @SerializedName("HoursDays")
        @Expose
        private String hoursDays;
        @SerializedName("SalesCordinator")
        @Expose
        private String salesCordinator;

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
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

        public String getHoursDays() {
            return hoursDays;
        }

        public void setHoursDays(String hoursDays) {
            this.hoursDays = hoursDays;
        }

        public String getSalesCordinator() {
            return salesCordinator;
        }

        public void setSalesCordinator(String salesCordinator) {
            this.salesCordinator = salesCordinator;
        }

    }
}
