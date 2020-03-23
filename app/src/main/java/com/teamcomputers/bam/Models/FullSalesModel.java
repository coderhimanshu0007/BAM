package com.teamcomputers.bam.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FullSalesModel implements Parcelable {
    @SerializedName("TMC")
    @Expose
    private String tMC;
    @SerializedName("Code")
    @Expose
    private String code;
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
    @SerializedName("YTDTarget")
    @Expose
    private Double yTDTarget;
    @SerializedName("QTDTarget")
    @Expose
    private Double qTDTarget;
    @SerializedName("MTDTarget")
    @Expose
    private Double mTDTarget;
    @SerializedName("YTDPercentage")
    @Expose
    private Double yTDPercentage;
    @SerializedName("QTDPercentage")
    @Expose
    private Double qTDPercentage;
    @SerializedName("MTDPercentage")
    @Expose
    private Double mTDPercentage;
    @SerializedName("Position")
    @Expose
    private int Position;

    public String getTMC() {
        return tMC;
    }

    public void setTMC(String tMC) {
        this.tMC = tMC;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Double getYTDTarget() {
        return yTDTarget;
    }

    public void setYTDTarget(Double yTDTarget) {
        this.yTDTarget = yTDTarget;
    }

    public Double getQTDTarget() {
        return qTDTarget;
    }

    public void setQTDTarget(Double qTDTarget) {
        this.qTDTarget = qTDTarget;
    }

    public Double getMTDTarget() {
        return mTDTarget;
    }

    public void setMTDTarget(Double mTDTarget) {
        this.mTDTarget = mTDTarget;
    }

    public Double getYTDPercentage() {
        return yTDPercentage;
    }

    public void setYTDPercentage(Double yTDPercentage) {
        this.yTDPercentage = yTDPercentage;
    }

    public Double getQTDPercentage() {
        return qTDPercentage;
    }

    public void setQTDPercentage(Double qTDPercentage) {
        this.qTDPercentage = qTDPercentage;
    }

    public Double getMTDPercentage() {
        return mTDPercentage;
    }

    public void setMTDPercentage(Double mTDPercentage) {
        this.mTDPercentage = mTDPercentage;
    }


    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tMC);
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeDouble(this.yTD);
        dest.writeDouble(this.qTD);
        dest.writeDouble(this.mTD);
        dest.writeDouble(this.yTDTarget);
        dest.writeDouble(this.qTDTarget);
        dest.writeDouble(this.mTDTarget);
        dest.writeDouble(this.yTDPercentage);
        dest.writeDouble(this.qTDPercentage);
        dest.writeDouble(this.mTDPercentage);
        dest.writeInt(this.Position);
    }

    protected FullSalesModel(Parcel in) {
        this.tMC = in.readString();
        this.code = in.readString();
        this.name = in.readString();
        this.yTD = in.readDouble();
        this.qTD = in.readDouble();
        this.mTD = in.readDouble();
        this.yTDTarget = in.readDouble();
        this.qTDTarget = in.readDouble();
        this.mTDTarget = in.readDouble();
        this.yTDPercentage = in.readDouble();
        this.qTDPercentage = in.readDouble();
        this.mTDPercentage = in.readDouble();
        this.Position = in.readInt();
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
