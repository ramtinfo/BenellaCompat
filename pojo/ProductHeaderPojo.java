package com.example.ram.benellacompat.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ram on 2/16/18.
 */

public class ProductHeaderPojo implements Parcelable {
    private String sortdesc;
    private String id;
    private String title;
    private String price;
    private String product_size;
    private String name;
    private String image;
    private String spacel_price;

    public ProductHeaderPojo() {

    }

    public ProductHeaderPojo(Parcel in) {
        sortdesc = in.readString();
        id = in.readString();
        title = in.readString();
        price = in.readString();
        product_size = in.readString();
        name = in.readString();
        image = in.readString();
        spacel_price = in.readString();
    }

    public static final Creator<ProductHeaderPojo> CREATOR = new Creator<ProductHeaderPojo>() {
        @Override
        public ProductHeaderPojo createFromParcel(Parcel in) {
            return new ProductHeaderPojo(in);
        }

        @Override
        public ProductHeaderPojo[] newArray(int size) {
            return new ProductHeaderPojo[size];
        }
    };

    public String getSortdesc() {
        return sortdesc;
    }

    public void setSortdesc(String sortdesc) {
        this.sortdesc = sortdesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSpacel_price() {
        return spacel_price;
    }

    public void setSpacel_price(String spacel_price) {
        this.spacel_price = spacel_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sortdesc);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(price);
        dest.writeString(product_size);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(spacel_price);
    }
}
