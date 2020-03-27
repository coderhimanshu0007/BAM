package com.teamcomputers.bam.Models.TotalSalesOrder;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OSOCustomerModel implements Parcelable {
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("SOAmount")
    @Expose
    private Double sOAmount;
    @SerializedName("open")
    @Expose
    private int open;
    @SerializedName("Position")
    @Expose
    private int position;
    @SerializedName("StateCodeWise")
    @Expose
    private List<StateCodeWise> stateCodeWise = null;

    protected OSOCustomerModel(Parcel in) {
        userId = in.readString();
        customerName = in.readString();
        if (in.readByte() == 0) {
            sOAmount = null;
        } else {
            sOAmount = in.readDouble();
        }
        open = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(customerName);
        if (sOAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(sOAmount);
        }
        dest.writeInt(open);
        dest.writeInt(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OSOCustomerModel> CREATOR = new Creator<OSOCustomerModel>() {
        @Override
        public OSOCustomerModel createFromParcel(Parcel in) {
            return new OSOCustomerModel(in);
        }

        @Override
        public OSOCustomerModel[] newArray(int size) {
            return new OSOCustomerModel[size];
        }
    };

    public OSOCustomerModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getSOAmount() {
        return sOAmount;
    }

    public void setSOAmount(Double sOAmount) {
        this.sOAmount = sOAmount;
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
        @SerializedName("SOAmount")
        @Expose
        private Double sOAmount;

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public Double getSOAmount() {
            return sOAmount;
        }

        public void setSOAmount(Double sOAmount) {
            this.sOAmount = sOAmount;
        }

    }
}
