package com.example.daan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    Button register;
    FirebaseAuth firebaseAuth;
    EditText name,email,password,phoneNO;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        register= findViewById(R.id.btnSignUp);
        firebaseAuth=FirebaseAuth.getInstance();
        name=findViewById(R.id.editNameSignUp);
        email=findViewById(R.id.editEmailSignUp);
        password=findViewById(R.id.editPasswdSignUp);
        phoneNO=findViewById(R.id.editPhoneSignUp);
        databaseUsers= FirebaseDatabase.getInstance().getReference("USERS");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name= name.getText().toString();
                String Email= email.getText().toString();
                String Password= password.getText().toString();
                String phone=phoneNO.getText().toString();

                // Toast.makeText(SignUpActivity.this, Name, Toast.LENGTH_SHORT).show();

                if(!Name.equalsIgnoreCase(""))
                {
                    if(!Email.equalsIgnoreCase(""))
                    {
                        if(!Password.equalsIgnoreCase(""))
                        {
                            if(!phone.equalsIgnoreCase("")) {

                                registerUsers(Name,Email,Password,phone);
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, "Enter PhoneNo", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "!Enter Email", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "!Enter Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    public void registerUsers(final String name, final String email, String password, final String phoneNo)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    String uid= user.getUid();
                    Users newuUser= new Users(name,email,phoneNo,1);
                    databaseUsers.child(uid).setValue(newuUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(SignUpActivity.this, "Tum Register kiye ja chuke ho",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Error aa gya bhaiya ji", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}