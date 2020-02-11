package com.teamcomputers.bam.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RSMDataModel  implements Parcelable {
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("YTD")
    @Expose
    private String ytd;
    @SerializedName("QTD")
    @Expose
    private String qtd;
    @SerializedName("MTD")
    @Expose
    private String mtd;

    public RSMDataModel(String name, String ytd, String qtd, String mtd) {
        this.name = name;
        this.ytd = ytd;
        this.qtd = qtd;
        this.mtd = mtd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYtd() {
        return ytd;
    }

    public void setYtd(String ytd) {
        this.ytd = ytd;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public String getMtd() {
        return mtd;
    }

    public void setMtd(String mtd) {
        this.mtd = mtd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.ytd);
        dest.writeString(this.qtd);
        dest.writeString(this.mtd);
    }

    protected RSMDataModel(Parcel in) {
        this.name = in.readString();
        this.ytd = in.readString();
        this.qtd = in.readString();
        this.mtd = in.readString();
    }

    public static final Creator<RSMDataModel> CREATOR = new Creator<RSMDataModel>() {
        @Override
        public RSMDataModel createFromParcel(Parcel source) {
            return new RSMDataModel(source);
        }

        @Override
        public RSMDataModel[] newArray(int size) {
            return new RSMDataModel[size];
        }
    };
}
