package com.teamcomputers.bam.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FiscalYearModel {
    @SerializedName("LastTimeRefreshed")
    @Expose
    private String lastTimeRefreshed;
    @SerializedName("FascialYear")
    @Expose
    private List<FascialYear> fascialYear = null;

    public String getLastTimeRefreshed() {
        return lastTimeRefreshed;
    }

    public void setLastTimeRefreshed(String lastTimeRefreshed) {
        this.lastTimeRefreshed = lastTimeRefreshed;
    }

    public List<FascialYear> getFascialYear() {
        return fascialYear;
    }

    public void setFascialYear(List<FascialYear> fascialYear) {
        this.fascialYear = fascialYear;
    }


    public class FascialYear {

        @SerializedName("Year")
        @Expose
        private String year;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

    }
}
