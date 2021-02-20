package com.example.daan;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Myadapter extends FirebaseRecyclerAdapter<Model,Myadapter.ViewHolder> {

    public Myadapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);
    }

    FirebaseAuth firebaseAuth;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.feedlayout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Model model) {

        holder.category.setText(model.getCategory());
        holder.ItemName.setText(model.getItemName());
        holder.Location.setText(model.getLocation());
        String imgaeUrl= model.getUrl();

        Glide.with(holder.itemImage.getContext()).load(imgaeUrl).into(holder.itemImage);

        String UidOfProduct=model.getUid();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        String uidOfCurrentUser= user.getUid();


        if(UidOfProduct.equals(uidOfCurrentUser))
        {
            holder.deleteBtn.setVisibility(View.VISIBLE);

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Delete Item");
                    builder.setMessage("Delet.......?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("ITEMS")
                                    .child(getRef(position).getKey()).removeValue();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            });

        }



        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle =  new Bundle();
                bundle.putString("email",model.getContact());
                bundle.putString("category",model.getCategory());
                bundle.putString("description", model.getDescription());
                bundle.putString("name",model.getItemName());
                bundle.putString("location",model.getLocation());
                bundle.putString("url",model.getUrl());



                Intent intent = new Intent(v.getContext(),Cart.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);

            }
        });







    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView ItemName,Location,category;

        ImageView editBtn,deleteBtn,itemImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemName=(itemView).findViewById(R.id.rItemName);
            Location=(itemView).findViewById(R.id.rItemLocation);
            category=(itemView).findViewById(R.id.categoryText);
            editBtn=(itemView).findViewById(R.id.editButton);
            deleteBtn=(itemView).findViewById(R.id.deleteButton);
            itemImage=(itemView).findViewById(R.id.itemImage);

        }
    }
}
