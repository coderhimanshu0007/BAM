package com.teamcomputers.bam.Models.TotalOutstanding;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TORSMSalesModel implements Parcelable {
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("TMC")
    @Expose
    private String tMC;
    @SerializedName("DSO")
    @Expose
    private Double dSO;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("Position")
    @Expose
    private int position;

    protected TORSMSalesModel(Parcel in) {
        name = in.readString();
        tMC = in.readString();
        if (in.readByte() == 0) {
            dSO = null;
        } else {
            dSO = in.readDouble();
        }
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readDouble();
        }
        if (in.readInt() == 0) {
            position = 0;
        } else {
            position = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(tMC);
        if (dSO == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dSO);
        }
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(amount);
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

    public static final Creator<TORSMSalesModel> CREATOR = new Creator<TORSMSalesModel>() {
        @Override
        public TORSMSalesModel createFromParcel(Parcel in) {
            return new TORSMSalesModel(in);
        }

        @Override
        public TORSMSalesModel[] newArray(int size) {
            return new TORSMSalesModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTMC() {
        return tMC;
    }

    public void setTMC(String tMC) {
        this.tMC = tMC;
    }

    public Double getDSO() {
        return dSO;
    }

    public void setDSO(Double dSO) {
        this.dSO = dSO;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
