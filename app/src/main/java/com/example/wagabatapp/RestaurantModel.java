package com.example.wagabatapp;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RestaurantModel {
    private String name;
    private String specialty;
    private String delivery_time;
    private String delivery_fee;
    private String rating;
    private String imageLink;

    public RestaurantModel(){

    }

    public RestaurantModel( String name, String specialty, String delivery_time, String delivery_fee, String rating, String imageLink ) {
        this.name = name;
        this.specialty = specialty;
        this.delivery_time = delivery_time;
        this.delivery_fee = delivery_fee;
        this.rating = rating;
        this.imageLink = imageLink;
    }


    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public String getRating() {
        return rating;
    }

    public String getImageLink() {
        return imageLink;
    }

}
