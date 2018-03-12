package com.example.ram.benellacompat.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ram on 3/8/18.
 */

public class FuckProductPojo implements Parcelable{
    ProductDataPojo data;
    ArrayList<String> product_image;
    public FuckProductPojo(){

    }

    protected FuckProductPojo(Parcel in) {
        data = in.readParcelable(ProductDataPojo.class.getClassLoader());
        product_image = in.createStringArrayList();
    }

    public static final Creator<FuckProductPojo> CREATOR = new Creator<FuckProductPojo>() {
        @Override
        public FuckProductPojo createFromParcel(Parcel in) {
            return new FuckProductPojo(in);
        }

        @Override
        public FuckProductPojo[] newArray(int size) {
            return new FuckProductPojo[size];
        }
    };

    public ProductDataPojo getData() {
        return data;
    }

    public void setData(ProductDataPojo data) {
        this.data = data;
    }

    public ArrayList<String> getProduct_image() {
        return product_image;
    }

    public void setProduct_image(ArrayList<String> product_image) {
        this.product_image = product_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeStringList(product_image);
    }
}
