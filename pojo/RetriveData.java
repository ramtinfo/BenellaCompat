package com.example.ram.benellacompat.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ram on 2/16/18.
 */

public class RetriveData implements Parcelable{
    private String id;
    private String name;
    private String category_id;
    private String price;
    private String spacel_price;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpacel_price() {
        return spacel_price;
    }

    public void setSpacel_price(String spacel_price) {
        this.spacel_price = spacel_price;
    }

    public static Creator<RetriveData> getCREATOR() {
        return CREATOR;
    }

    public RetriveData(){

    }

    protected RetriveData(Parcel in) {
        id = in.readString();
        name = in.readString();
        category_id = in.readString();
        price=in.readString();
        spacel_price=in.readString();
        image=in.readString();
    }

    public static final Creator<RetriveData> CREATOR = new Creator<RetriveData>() {
        @Override
        public RetriveData createFromParcel(Parcel in) {
            return new RetriveData(in);
        }

        @Override
        public RetriveData[] newArray(int size) {
            return new RetriveData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(category_id);
        dest.writeString(price);
        dest.writeString(spacel_price);
        dest.writeString(image);
    }
}
