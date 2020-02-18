package com.teamcomputers.bam.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SalesCustomerModel  implements Parcelable {
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("YTD")
    @Expose
    private Double yTD;
    @SerializedName("QTD")
    @Expose
    private Double qTD;
    @SerializedName("MTD")
    @Expose
    private Double mTD;
    @SerializedName("open")
    @Expose
    private int open;
    @SerializedName("Position")
    @Expose
    private int position;
    @SerializedName("StateCodeWise")
    @Expose
    private List<StateCodeWise> stateCodeWise = null;

    public SalesCustomerModel() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getYTD() {
        return yTD;
    }

    public void setYTD(Double yTD) {
        this.yTD = yTD;
    }

    public Double getQTD() {
        return qTD;
    }

    public void setQTD(Double qTD) {
        this.qTD = qTD;
    }

    public Double getMTD() {
        return mTD;
    }

    public void setMTD(Double mTD) {
        this.mTD = mTD;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<StateCodeWise> getStateCodeWise() {
        return stateCodeWise;
    }

    public void setStateCodeWise(List<StateCodeWise> stateCodeWise) {
        this.stateCodeWise = stateCodeWise;
    }

    public class StateCodeWise {

        @SerializedName("StateCode")
        @Expose
        private String stateCode;
        @SerializedName("YTD")
        @Expose
        private Double yTD;
        @SerializedName("QTD")
        @Expose
        private Double qTD;
        @SerializedName("MTD")
        @Expose
        private Double mTD;
        @SerializedName("Name")
        @Expose
        private String name;

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public Double getYTD() {
            return yTD;
        }

        public void setYTD(Double yTD) {
            this.yTD = yTD;
        }

        public Double getQTD() {
            return qTD;
        }

        public void setQTD(Double qTD) {
            this.qTD = qTD;
        }

        public Double getMTD() {
            return mTD;
        }

        public void setMTD(Double mTD) {
            this.mTD = mTD;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customerName);
        dest.writeDouble(this.yTD);
        dest.writeDouble(this.qTD);
        dest.writeDouble(this.mTD);
        dest.writeInt(this.open);
        dest.writeList(this.stateCodeWise);
    }

    protected SalesCustomerModel(Parcel in) {
        this.customerName = in.readString();
        this.yTD = in.readDouble();
        this.qTD = in.readDouble();
        this.mTD = in.readDouble();
        this.open = in.readInt();
        //this.stateCodeWise = in.readList();
    }

    public static final Creator<SalesCustomerModel> CREATOR = new Creator<SalesCustomerModel>() {
        @Override
        public SalesCustomerModel createFromParcel(Parcel source) {
            return new SalesCustomerModel(source);
        }

        @Override
        public SalesCustomerModel[] newArray(int size) {
            return new SalesCustomerModel[size];
        }
    };
}
