package com.teamcomputers.bam.Models.TotalSalesOrder;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OSORSMSalesModel implements Parcelable {

    @SerializedName("TMC")
    @Expose
    private String tMC;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("SOAmount")
    @Expose
    private Double sOAmount;
    @SerializedName("Position")
    @Expose
    private int position;

    protected OSORSMSalesModel(Parcel in) {
        tMC = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            sOAmount = null;
        } else {
            sOAmount = in.readDouble();
        }
        if (in.readInt() == 0) {
            position = 0;
        } else {
            position = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tMC);
        dest.writeString(name);
        if (sOAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(sOAmount);
        }
        if (position == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(position);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OSORSMSalesModel> CREATOR = new Creator<OSORSMSalesModel>() {
        @Override
        public OSORSMSalesModel createFromParcel(Parcel in) {
            return new OSORSMSalesModel(in);
        }

        @Override
        public OSORSMSalesModel[] newArray(int size) {
            return new OSORSMSalesModel[size];
        }
    };

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

    public Double getSOAmount() {
        return sOAmount;
    }

    public void setSOAmount(Double sOAmount) {
        this.sOAmount = sOAmount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
