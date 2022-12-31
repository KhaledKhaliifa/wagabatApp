package com.example.wagabatapp.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DishModel {
    String name;
    String price;
    String available;
    String description;
    String imageLink;
    public String itemCount;
    public String reference;

    public DishModel(){

    }

    public DishModel(String name, String price, String available, String description, String imageLink,String itemCount, String reference) {
        this.name = name;
        this.price = price;
        this.available = available;
        this.description = description;
        this.imageLink = imageLink;
        this.itemCount = itemCount;
        this.reference = reference;
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

    public String getItemCount(){
        return itemCount;
    }

    public String getReference(){ return reference;}
}
