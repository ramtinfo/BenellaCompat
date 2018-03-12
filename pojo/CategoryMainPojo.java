package com.example.ram.benellacompat.pojo;

import java.util.ArrayList;

/**
 * Created by ram on 2/17/18.
 */

public class CategoryMainPojo {
    String status;
    ArrayList<CategoryPojo> data;

    public CategoryMainPojo() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<CategoryPojo> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryPojo> data) {
        this.data = data;
    }
}
