package com.example.wagabatapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wagabatapp.Models.DishModel;
import com.example.wagabatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<DishModel> list;
    View view;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        databaseReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users/"+uid+"/cart/"+dish.getReference()+"/itemCount");
        DatabaseReference dishReference = FirebaseDatabase.getInstance("https://wagbaapp-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users/"+uid+"/cart/"+dish.getReference());
        holder.item_name.setText(dish.getName());
        holder.item_count.setText(dish.getItemCount());
        holder.item_reference.setText(dish.getReference());

        Float dish_price = Float.valueOf(dish.getPrice());

        Integer itemCount = Integer.valueOf(dish.getItemCount());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dishReference.removeValue();
            }
        });
//        holder.plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Integer count = Integer.valueOf(dish.getItemCount());
//                count ++;
//                databaseReference.setValue(count.toString());
//                //TODO: FIX PLUS AND MINUS BUG WITH RECYCLER VIEW
//            }
//        });
//        holder.minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Integer count = Integer.valueOf(dish.getItemCount());
//                if(count !=1){
//                    count --;
//                    databaseReference.setValue(count.toString());
//                    //TODO: FIX PLUS AND MINUS BUG WITH RECYCLER VIEW
//
//                }
//            }
//        });
        Integer count = Integer.valueOf(dish.getItemCount());
        Float total_price = dish_price * count;
        holder.item_price.setText(total_price.toString());
        Glide.with(context).load(dish.getImageLink()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_price, item_count, item_reference;
        ImageView image,plus,minus,remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.item_name_cart);
            item_price = itemView.findViewById(R.id.item_price_cart);
            item_count = itemView.findViewById(R.id.item_count_cart);
            image = itemView.findViewById(R.id.item_picture);
            plus = itemView.findViewById(R.id.plus_sign);
            minus = itemView.findViewById(R.id.minus_sign);
            remove = itemView.findViewById(R.id.x_icon_cart);
            item_reference = itemView.findViewById(R.id.referenceHidden);

        }
    }

}

