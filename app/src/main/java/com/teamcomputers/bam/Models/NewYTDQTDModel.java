package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewYTDQTDModel {
    @SerializedName("QTD")
    @Expose
    private List<QTD> qTD = null;
    @SerializedName("MTD")
    @Expose
    private List<MTD> mTD = null;

    public List<QTD> getQTD() {
        return qTD;
    }

    public void setQTD(List<QTD> qTD) {
        this.qTD = qTD;
    }

    public List<MTD> getMTD() {
        return mTD;
    }

    public void setMTD(List<MTD> mTD) {
        this.mTD = mTD;
    }

    public class MTD {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Target")
        @Expose
        private Double target;
        @SerializedName("Actual")
        @Expose
        private Double actual;
        @SerializedName("Percentage")
        @Expose
        private Double percentage;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getTarget() {
            return target;
        }

        public void setTarget(Double target) {
            this.target = target;
        }

        public Double getActual() {
            return actual;
        }

        public void setActual(Double actual) {
            this.actual = actual;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

    }

    public static class QTD {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Target")
        @Expose
        private Double target;
        @SerializedName("Actual")
        @Expose
        private Double actual;
        @SerializedName("Percentage")
        @Expose
        private Double percentage;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getTarget() {
            return target;
        }

        public void setTarget(Double target) {
            this.target = target;
        }

        public Double getActual() {
            return actual;
        }

        public void setActual(Double actual) {
            this.actual = actual;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }
}
