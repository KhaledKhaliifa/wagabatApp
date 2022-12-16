package com.example.wagabatapp;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    ArrayList<RestaurantModel> list;
    View view;
    DatabaseReference databaseReference;
    static Context context;

    public RestaurantAdapter(ArrayList<RestaurantModel> list){
        this.list = list;

    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("restaurants");
        final String restaurant_key = databaseReference.getRef().toString();

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.restaurant_list_item, parent,false);

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, RestaurantMenu.class);
                    intent.putExtra("position", Integer.toString(position));
                    context.startActivity(intent);

                }
            });

        }
    }

}
