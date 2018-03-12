package com.example.ram.benellacompat.pojo;

import android.support.annotation.NonNull;

/**
 * Created by ram on 2/21/18.
 */

public class WinnerUID implements Comparable<WinnerUID> {
    String uid;
    String bid;

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    int difference;
    public WinnerUID() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    @Override
    public int compareTo(@NonNull WinnerUID o) {
        return this.getDifference() -o.getDifference();
    }
}
