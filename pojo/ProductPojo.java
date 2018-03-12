package com.example.ram.benellacompat.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ram on 2/19/18.
 */

public class ProductPojo implements Parcelable {
    private String status;
    FuckProductPojo data;
    public ProductPojo() {

    }

    protected ProductPojo(Parcel in) {
        status = in.readString();
        data = in.readParcelable(FuckProductPojo.class.getClassLoader());
    }

    public static final Creator<ProductPojo> CREATOR = new Creator<ProductPojo>() {
        @Override
        public ProductPojo createFromParcel(Parcel in) {
            return new ProductPojo(in);
        }

        @Override
        public ProductPojo[] newArray(int size) {
            return new ProductPojo[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FuckProductPojo getData() {
        return data;
    }

    public void setData(FuckProductPojo data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeParcelable(data, flags);
    }
}
