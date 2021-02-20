package com.example.daan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;

public class DonateItems extends AppCompatActivity {

    Button post,uploadImage;
    EditText category,description,email,name,location;
    ImageView preview;
    Uri filePath;
    Bitmap bitmap;

    DatabaseReference databaseReference;
    FirebaseDatabase database;
    ProgressDialog dialog;
    FirebaseAuth firebaseAuth;
    Uri downloadUri;
    String key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_items);


        uploadImage=findViewById(R.id.btnUploadeImage);
        post=findViewById(R.id.btnPostItem);

        name=findViewById(R.id.editItemName);
        category=findViewById(R.id.editItemCategory);
        description=findViewById(R.id.editItemDescription);
        email=findViewById(R.id.editItemContact);
        location=findViewById(R.id.editItemLocation);

        preview=findViewById(R.id.imagePreview);


        databaseReference= FirebaseDatabase.getInstance().getReference("ITEMS");
        database=FirebaseDatabase.getInstance();
        dialog= new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();

         key= database.getReference().push().getKey();


       uploadImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Dexter.withActivity(DonateItems.this)
                       .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                       .withListener(new PermissionListener() {
                           @Override
                           public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                               Intent intent = new Intent(Intent.ACTION_PICK);
                               intent.setType("image/*");
                               startActivityForResult(Intent.createChooser(intent,"Please select Image"),1);

                           }

                           @Override
                           public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                           }

                           @Override
                           public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                               permissionToken.cancelPermissionRequest();

                           }
                       }).check();
           }
       });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postItems();
            }
        });

    }



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            filePath=data.getData();
            uploadOnFirebase(key);


            try {

                InputStream inputStream=getContentResolver().openInputStream(filePath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                preview.setImageBitmap(bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public  void uploadOnFirebase(String key)
    {
        dialog.setMessage("Uploading Image....");
        dialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference uploader= storage.getReference().child(key);

       UploadTask uploadTask= uploader.putFile(filePath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return uploader.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    downloadUri = task.getResult();

                    Toast.makeText(DonateItems.this, ""+downloadUri, Toast.LENGTH_SHORT).show();

                } else {
                    // Handle failures
                    // ...
                }
            }

        });


    }

    public  void postItems()
    {


        FirebaseUser user=firebaseAuth.getCurrentUser();
        final String uid= user.getUid();
        final String itemName=name.getText().toString();
        final String itemCategory=category.getText().toString();
        final String itemDescription=description.getText().toString();
        final String  userEmail=email.getText().toString();
        final String userLocation=location.getText().toString();

        final String dUrl=downloadUri.toString();
        Log.d("uri of image", "postItems: "+dUrl);

        final Model model = new Model(itemName,itemCategory,itemDescription,userLocation,userEmail,uid,dUrl);

        databaseReference.child("item "+key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    DatabaseReference temp=database.getReference("UserItems");
                    temp.child(uid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            Toast.makeText(DonateItems.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        });






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DonateItems.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}