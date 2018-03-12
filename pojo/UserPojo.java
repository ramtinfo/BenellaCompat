package com.example.ram.benellacompat.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ram on 3/10/18.
 */

public class UserPojo implements Parcelable{
    String status;
    ArrayList<UserDetail> data;
    public UserPojo(){

    }
    protected UserPojo(Parcel in) {
        status = in.readString();
        data = in.createTypedArrayList(UserDetail.CREATOR);
    }

    public static final Creator<UserPojo> CREATOR = new Creator<UserPojo>() {
        @Override
        public UserPojo createFromParcel(Parcel in) {
            return new UserPojo(in);
        }

        @Override
        public UserPojo[] newArray(int size) {
            return new UserPojo[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<UserDetail> getData() {
        return data;
    }

    public void setData(ArrayList<UserDetail> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeTypedList(data);
    }
}
