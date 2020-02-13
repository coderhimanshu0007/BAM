package com.teamcomputers.bam.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FullSalesModel implements Parcelable {
    @SerializedName("TMC")
    @Expose
    private String tMC;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("YTD")
    @Expose
    private Double yTD;
    @SerializedName("MTD")
    @Expose
    private Double mTD;
    @SerializedName("QTD")
    @Expose
    private Double qTD;

    public String getTMC() {
        return tMC;
    }

    public void setTMC(String tMC) {
        this.tMC = tMC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getYTD() {
        return yTD;
    }

    public void setYTD(Double yTD) {
        this.yTD = yTD;
    }

    public Double getMTD() {
        return mTD;
    }

    public void setMTD(Double mTD) {
        this.mTD = mTD;
    }

    public Double getQTD() {
        return qTD;
    }

    public void setQTD(Double qTD) {
        this.qTD = qTD;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tMC);
        dest.writeString(this.name);
        dest.writeDouble(this.yTD);
        dest.writeDouble(this.qTD);
        dest.writeDouble(this.mTD);
    }

    protected FullSalesModel(Parcel in) {
        this.tMC = in.readString();
        this.name = in.readString();
        this.yTD = in.readDouble();
        this.qTD = in.readDouble();
        this.mTD = in.readDouble();
    }

    public static final Creator<FullSalesModel> CREATOR = new Creator<FullSalesModel>() {
        @Override
        public FullSalesModel createFromParcel(Parcel source) {
            return new FullSalesModel(source);
        }

        @Override
        public FullSalesModel[] newArray(int size) {
            return new FullSalesModel[size];
        }
    };
}
