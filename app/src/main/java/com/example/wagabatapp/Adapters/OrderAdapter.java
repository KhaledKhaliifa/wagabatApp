package com.example.wagabatapp.Adapters;
import com.bumptech.glide.Glide;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Index;

import com.example.wagabatapp.Models.DishModel;
import com.example.wagabatapp.Models.OrderModel;
import com.example.wagabatapp.R;
import com.example.wagabatapp.RestaurantMenu;
import com.example.wagabatapp.Models.RestaurantModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    ArrayList<OrderModel> list;
    FirebaseAuth mAuth;
    View view;
    OrderDishAdapter orderDishAdapter;
    ArrayList<DishModel> dishList;
    DatabaseReference databaseReference;
    DatabaseReference dishReference;
    static Context context;
    LinearLayoutManager layoutManager;

    public OrderAdapter(ArrayList<OrderModel> list){
        this.list = list;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("restaurants");
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.order_history_item, parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // GETTING THE ORDER NUMBER
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users/"+mAuth.getUid()+"orders");
        OrderModel order = list.get(position);
        holder.name.setText(order.getRestaurant_name());
        holder.status.setText(order.getStatus());
        long date = order.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(date);
        holder.date.setText(sdf.format(resultdate));
        holder.id.setText(date+"");
        holder.total.setText(order.getTotal_price()+" EGP");

        // GETTING LIST OF DISHES INTO RECYCLER VIEW

        layoutManager = new LinearLayoutManager(context);
        dishList = new ArrayList<>();
        orderDishAdapter = new OrderDishAdapter(dishList);

        String reference = order.getOrder_reference();
        dishReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users/"+mAuth.getUid()+"/orders/"+reference+"/dishList");
        dishReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d("Gaberreference", dataSnapshot.getValue().toString());
                    DishModel dish = dataSnapshot.getValue(DishModel.class);
                    Log.d("Gaberreference", dish.getName());
                    dishList.add(dish);
                }
                orderDishAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, status, date, id,total;
        ImageView image;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurant_name_history);
            status = itemView.findViewById(R.id.delivery_status_history);
            date = itemView.findViewById(R.id.order_date_history);
            id = itemView.findViewById(R.id.order_id_history);
            image = itemView.findViewById(R.id.restaurant_image_in_history);
            total = itemView.findViewById(R.id.totalPriceHistory);
            recyclerView = itemView.findViewById(R.id.recycler_view_history);
        }
    }

}
