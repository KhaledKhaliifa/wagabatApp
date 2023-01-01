package com.example.wagabatapp.Models;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderModel {
    String total_price;
    String delivery_fee;
    String subtotal_price;
    List<DishModel> dishes;
    String status;

    long date;


    public OrderModel(){

    }
    public OrderModel(String total_price, String delivery_fee, String subtotal_price, List<DishModel> dishes, long date, String status) {
        this.total_price = total_price;
        this.delivery_fee = delivery_fee;
        this.subtotal_price = subtotal_price;
        this.dishes = dishes;
        this.date = date;
        this.status = status;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getSubtotal_price() {
        return subtotal_price;
    }

    public void setSubtotal_price(String subtotal_price) {
        this.subtotal_price = subtotal_price;
    }

    public List<DishModel>  getDishList() {
        return dishes;
    }

    public void setDishList(List<DishModel>  dishes) {
        this.dishes = dishes;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
