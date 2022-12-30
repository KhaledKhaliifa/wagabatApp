package com.example.wagabatapp;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wagabatapp.databinding.ActivityRestaurantItemExpandedBinding;
import com.example.wagabatapp.databinding.CartItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<DishModel> list;
    View view;
    DatabaseReference databaseReference;
    static Context context;
    public CartAdapter(ArrayList<DishModel> list){
        this.list = list;

    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cart_item, parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DishModel dish = list.get(position);

        holder.item_name.setText(dish.getName());
        holder.item_count.setText(dish.getItemCount());
        holder.item_reference.setText(dish.getReference());

        Integer count = Integer.valueOf(dish.getItemCount());
        Float item_price = Float.valueOf(dish.getPrice());
        item_price = item_price * count;
        holder.item_price.setText(item_price.toString());

        if(dish.getImageLink()!= null){
            new DownloadImageTask( holder.image)
                    .execute(dish.getImageLink());
        }
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer number_of_items =  Integer.valueOf(holder.item_count.getText().toString());
                Float singleItemPrice = Float.valueOf(holder.item_price.getText().toString()) / number_of_items;
                number_of_items+=1;
                holder.item_count.setText(number_of_items.toString());
                Float total_cost = number_of_items * singleItemPrice;
                holder.item_price.setText(total_cost.toString());
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer number_of_items =  Integer.valueOf(holder.item_count.getText().toString());
                Float singleItemPrice = Float.valueOf(holder.item_price.getText().toString()) / number_of_items;

                if(number_of_items!=1){
                    number_of_items-=1;
                    holder.item_count.setText(number_of_items.toString());
                    Float total_cost = number_of_items * singleItemPrice;
                    holder.item_price.setText(total_cost.toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_price, item_count, item_reference;
        ImageView image, plus, minus;
//        SharedPreferences prefs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.item_name_cart);
            item_price = itemView.findViewById(R.id.item_price_cart);
            item_count = itemView.findViewById(R.id.item_count_cart);
            image = itemView.findViewById(R.id.item_picture);
            plus = itemView.findViewById(R.id.plus_sign_cart);
            minus = itemView.findViewById(R.id.minus_sign_cart);
            item_reference = itemView.findViewById(R.id.referenceHidden);

            //TODO: Maybe add onclick listener to RestaurantItemExpanded

        }
    }
    public void updateFirebase(String reference, Integer number_of_items){
        String restaurantPosition = String.valueOf(reference.charAt(0));
        String dishPosition = String.valueOf(reference.charAt(1));

        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("/cart/"+restaurantPosition+dishPosition);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DishModel dish = snapshot.getValue(DishModel.class);
                dish.itemCount=number_of_items.toString();
                dish.reference = restaurantPosition+dishPosition;
                databaseReference.setValue(dish);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                   dishName = "Missing Dish Name";
//                   binding.dishNameExtended.setText(dishName);
            }
        });
    }
    public void updateTotal(){

    }
}

