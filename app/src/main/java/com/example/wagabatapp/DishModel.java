package com.example.wagabatapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DishModel {
    String name;
    String price;
    String available;
    String description;
    String imageLink;
    public DishModel(){

    }

    public DishModel(String name, String price, String available, String description, String imageLink) {
        this.name = name;
        this.price = price;
        this.available = available;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getAvailable() {
        return available;
    }

    public String getDescription(){
        return description;
    }

    public String getImageLink(){
        return imageLink;
    }
}
