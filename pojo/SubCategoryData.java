package com.example.ram.benellacompat.pojo;

/**
 * Created by ram on 2/16/18.
 */

public class SubCategoryData {
    private String id;
    private String title;

    public SubCategoryData() {

    }

    public SubCategoryData(String id, String title) {
        this.id = id;
        this.title = title;
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
}
