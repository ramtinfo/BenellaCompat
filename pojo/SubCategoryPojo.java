package com.example.ram.benellacompat.pojo;

import java.util.ArrayList;

/**
 * Created by ram on 2/16/18.
 */

public class SubCategoryPojo {
    private String status;

    private ArrayList<SubCategoryData> data;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public ArrayList<SubCategoryData> getData ()
    {
        return data;
    }

    public void setData (ArrayList<SubCategoryData> data)
    {
        this.data = data;
    }
}
