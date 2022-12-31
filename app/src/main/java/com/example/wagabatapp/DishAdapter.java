package com.example.wagabatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder>  {
    ArrayList<DishModel> list;
    View view;
    DatabaseReference databaseReference;
    static Context context;

    public DishAdapter(ArrayList<DishModel> list){
        this.list = list;
    }

    @NonNull
    @Override
    public DishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SharedPreferences prefs;
        context = parent.getContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String restaurant = prefs.getString("restaurant","0");
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("restaurants/"+"restaurant"+restaurant+"/dishes");

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.restaurant_menu_item, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishAdapter.ViewHolder holder, int position) {
        DishModel dish = list.get(position);

        holder.name.setText(dish.getName());
        holder.price.setText(dish.getPrice());
        if(dish.getAvailable().equals("1")){
            holder.availability.setText("Available");
            holder.availability.setTextColor(Color.parseColor("#00AA00"));
        }
        else{
            holder.availability.setText("Unavailable");
            holder.availability.setTextColor(Color.parseColor("#AA0000"));

        }
        Glide.with(context).load(dish.getImageLink()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,price,availability;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.dish_name);
            price = itemView.findViewById(R.id.item_price);
            availability = itemView.findViewById(R.id.availability);
            image = itemView.findViewById(R.id.dishIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                SharedPreferences prefs;

                @Override
                public void onClick(View view) {
                    prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    int position = getAdapterPosition();
                    editor.putString("dish", Integer.toString(position));
                    editor.commit();
                    Intent intent = new Intent(context, RestaurantItemExpanded.class);
                    //intent.putExtra("position", Integer.toString(position));
                    context.startActivity(intent);

                }
            });

        }

    }
}

