package com.example.ram.benellacompat.pojo;

/**
 * Created by ram on 3/12/18.
 */

public class Product {
    int id;
    String name;
    int quantity;
    public Product(int id,String name,int quantity){
        this.id=id;
        this.name=name;
        this.quantity=quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
