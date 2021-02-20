package com.example.daan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.GenericArrayType;

public class Cart extends AppCompatActivity {

    TextView name,categoty,location,contact,description;
    ImageView image;
    String email;
    Button contactMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        name=findViewById(R.id.itemNameCart);
        categoty=findViewById(R.id.categityCart);
        location=findViewById(R.id.locationCart);
      //  contact=findViewById(R.id.contactCart);
        description=findViewById(R.id.descritionCart);
        image=findViewById(R.id.imageCart);
        contactMe=findViewById(R.id.contactMe);

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null)
        {
            name.setText(bundle.getString("name"));
            categoty.setText(bundle.getString("category"));
            location.setText(bundle.getString("location"));
            description.setText(bundle.getString("description"));
            email=(bundle.getString("email"));
            String url=bundle.getString("url");
            Glide.with(Cart.this).load(url).into(image);
        }



        contactMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mailto = "mailto:"+email;

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    //TODO: Handle case where no email app is available
                }

            }
        });

    }
}