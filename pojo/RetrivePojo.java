package com.example.ram.benellacompat.pojo;

import java.util.ArrayList;

/**
 * Created by ram on 2/16/18.
 */

public class RetrivePojo {
    private String status;

    private ArrayList<RetriveData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RetriveData> getData() {
        return data;
    }

    public void setData(ArrayList<RetriveData> data) {
        this.data = data;
    }
}
