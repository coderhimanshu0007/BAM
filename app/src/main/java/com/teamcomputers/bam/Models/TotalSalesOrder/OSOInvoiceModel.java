package com.teamcomputers.bam.Models.TotalSalesOrder;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OSOInvoiceModel implements Parcelable {

    @SerializedName("InvoiceNo")
    @Expose
    private String invoiceNo;
    @SerializedName("SOAmount")
    @Expose
    private Double sOAmount;
    @SerializedName("Position")
    @Expose
    private int position;
    @SerializedName("open")
    @Expose
    private int open;
    @SerializedName("Product")
    @Expose
    private List<Product> product = null;

    protected OSOInvoiceModel(Parcel in) {
        invoiceNo = in.readString();
        if (in.readByte() == 0) {
            sOAmount = null;
        } else {
            sOAmount = in.readDouble();
        }
        position = in.readInt();
        open = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoiceNo);
        if (sOAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(sOAmount);
        }
        dest.writeInt(position);
        dest.writeInt(open);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OSOInvoiceModel> CREATOR = new Creator<OSOInvoiceModel>() {
        @Override
        public OSOInvoiceModel createFromParcel(Parcel in) {
            return new OSOInvoiceModel(in);
        }

        @Override
        public OSOInvoiceModel[] newArray(int size) {
            return new OSOInvoiceModel[size];
        }
    };

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }


    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public class Product {

        @SerializedName("Product")
        @Expose
        private String product;
        @SerializedName("SOAmount")
        @Expose
        private Double sOAmount;

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public Double getSOAmount() {
            return sOAmount;
        }

        public void setSOAmount(Double sOAmount) {
            this.sOAmount = sOAmount;
        }

    }
}