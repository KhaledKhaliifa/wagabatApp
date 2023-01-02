package com.example.wagabatapp.Models;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderModel {
    String total_price;
    String delivery_fee;
    String subtotal_price;
    String restaurant_name;
    List<DishModel> dishes;
    String status;
    long date;
    Integer id;
    String order_reference;
    String hour_minute;
    String userAuthId;
    String gate;


    public OrderModel() {

    }

    public OrderModel(String total_price, String delivery_fee, String subtotal_price, List<DishModel> dishes, long date,
                      String status, String restaurant_name, Integer id, String order_reference, String hour_minute, String userAuthId, String gate) {
        this.total_price = total_price;
        this.delivery_fee = delivery_fee;
        this.subtotal_price = subtotal_price;
        this.dishes = dishes;
        this.date = date;
        this.status = status;
        this.restaurant_name = restaurant_name;
        this.id = id;
        this.order_reference = order_reference;
        this.hour_minute = hour_minute;
        this.userAuthId = userAuthId;
        this.gate = gate;
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

    public List<DishModel> getDishList() {
        return dishes;
    }

    public void setDishList(List<DishModel> dishes) {
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

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder_reference() {
        return order_reference;
    }

    public void setOrder_reference(String order_reference) {
        this.order_reference = order_reference;
    }

    public String getHour_minute() {
        return hour_minute;
    }

    public void setHour_minute(String hour_minute) {
        this.hour_minute = hour_minute;
    }

    public String getUserAuthId() {
        return userAuthId;
    }

    public void setUserAuthId(String userAuthId) {
        this.userAuthId = userAuthId;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }
}
