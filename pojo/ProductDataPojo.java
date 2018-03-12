package com.example.ram.benellacompat.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ram on 2/19/18.
 */

public class ProductDataPojo implements Parcelable{
    private String id;
    private String name;
    private String sku;
    private String sortdesc;
    private String price;
    private String spacel_prize;
    private String descount;
    private String product_stock;
    private String product_size;
    public String getSpacel_prize() {
        return spacel_prize;
    }

    public void setSpacel_prize(String spacel_prize) {
        this.spacel_prize = spacel_prize;
    }

    public String getDescount() {
        return descount;
    }

    public void setDescount(String descount) {
        this.descount = descount;
    }

    public String getProduct_stock() {
        return product_stock;
    }

    public void setProduct_stock(String product_stock) {
        this.product_stock = product_stock;
    }

    public static Creator<ProductDataPojo> getCREATOR() {
        return CREATOR;
    }


    public ProductDataPojo(){

    }

    protected ProductDataPojo(Parcel in) {
        id = in.readString();
        name = in.readString();
        sku = in.readString();
        sortdesc = in.readString();
        price = in.readString();
        product_size = in.readString();
        descount=in.readString();
        spacel_prize=in.readString();
        product_stock=in.readString();
    }

    public static final Creator<ProductDataPojo> CREATOR = new Creator<ProductDataPojo>() {
        @Override
        public ProductDataPojo createFromParcel(Parcel in) {
            return new ProductDataPojo(in);
        }

        @Override
        public ProductDataPojo[] newArray(int size) {
            return new ProductDataPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(sku);
        dest.writeString(sortdesc);
        dest.writeString(price);
        dest.writeString(product_size);
        dest.writeString(descount);
        dest.writeString(spacel_prize);
        dest.writeString(product_stock);
    }

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSortdesc() {
        return sortdesc;
    }

    public void setSortdesc(String sortdesc) {
        this.sortdesc = sortdesc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

}
