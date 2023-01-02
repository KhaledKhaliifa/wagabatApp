package com.example.wagabatapp.Adapters;
import com.bumptech.glide.Glide;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Index;

import com.example.wagabatapp.Models.DishModel;
import com.example.wagabatapp.Models.OrderModel;
import com.example.wagabatapp.R;
import com.example.wagabatapp.RestaurantMenu;
import com.example.wagabatapp.Models.RestaurantModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderDishAdapter extends RecyclerView.Adapter<OrderDishAdapter.ViewHolder> {
    ArrayList<DishModel> list;
    View view;
    DatabaseReference databaseReference;
    static Context context;
    FirebaseAuth mAuth;

    public OrderDishAdapter(ArrayList<DishModel> list){
        this.list = list;
    }

    @NonNull
    @Override
    public OrderDishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        mAuth = FirebaseAuth.getInstance();
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.order_details_from_history_item, parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DishModel dish = list.get(position);
        String name = dish.getName();
        String quantity = dish.getItemCount();
        String price = dish.getPrice();
        String total = String.valueOf(Float.valueOf(quantity) * Float.valueOf(price));

        holder.name.setText(dish.getName());
        holder.quantity.setText(dish.getItemCount());
        holder.total_price.setText(total);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, quantity, total_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dish_name_order_details);
            quantity = itemView.findViewById(R.id.item_amount_order_details);
            total_price = itemView.findViewById(R.id.dish_price_order_details);

        }
    }

}
