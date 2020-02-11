package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YTDQTDModel {
    @SerializedName("YTD")
    @Expose
    private List<YTD> yTD = null;
    @SerializedName("QTD")
    @Expose
    private List<QTD> qTD = null;

    public List<YTD> getYTD() {
        return yTD;
    }

    public void setYTD(List<YTD> yTD) {
        this.yTD = yTD;
    }

    public List<QTD> getQTD() {
        return qTD;
    }

    public void setQTD(List<QTD> qTD) {
        this.qTD = qTD;
    }


    public class QTD {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Value")
        @Expose
        private Double value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }

    public class YTD {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Value")
        @Expose
        private Double value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }
}
