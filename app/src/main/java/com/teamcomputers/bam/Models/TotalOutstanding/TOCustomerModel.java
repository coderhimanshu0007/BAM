package com.teamcomputers.bam.Models.TotalOutstanding;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TOCustomerModel implements Parcelable {
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("open")
    @Expose
    private int open;
    @SerializedName("Position")
    @Expose
    private int position;
    @SerializedName("StateCodeWise")
    @Expose
    private List<StateCodeWise> stateCodeWise = null;

    protected TOCustomerModel(Parcel in) {
        userId = in.readString();
        customerName = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readDouble();
        }
        open = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(customerName);
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(amount);
        }
        dest.writeInt(open);
        dest.writeInt(position);
    }

    public static final Creator<TOCustomerModel> CREATOR = new Creator<TOCustomerModel>() {
        @Override
        public TOCustomerModel createFromParcel(Parcel in) {
            return new TOCustomerModel(in);
        }

        @Override
        public TOCustomerModel[] newArray(int size) {
            return new TOCustomerModel[size];
        }
    };

    public TOCustomerModel() {
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    @Override
    public int describeContents() {
        return 0;
    }

    public class StateCodeWise {

        @SerializedName("StateCode")
        @Expose
        private String stateCode;
        @SerializedName("Amount")
        @Expose
        private Double amount;

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }
}
