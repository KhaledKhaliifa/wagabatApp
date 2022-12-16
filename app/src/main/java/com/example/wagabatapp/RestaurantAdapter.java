package com.example.wagabatapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    ArrayList<RestaurantModel> list;


    public RestaurantAdapter(ArrayList<RestaurantModel> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.restaurant_list_item, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RestaurantModel restaurant = list.get(position);

        holder.name.setText(restaurant.getName());
        holder.rating.setText(restaurant.getRating());
        holder.specialty.setText(restaurant.getSpecialty());
        holder.delivery_time.setText(restaurant.getDelivery_time());
        holder.delivery_fee.setText(restaurant.getDelivery_fee());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,rating,specialty,delivery_time,delivery_fee;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.restaurant_name_rv);
            rating = itemView.findViewById(R.id.restaurant_rating);
            specialty = itemView.findViewById(R.id.restaurant_specialty);
            delivery_time = itemView.findViewById(R.id.restaurant_delivery_time);
            delivery_fee = itemView.findViewById(R.id.restaurant_delivery_fee);

        }
    }
}
