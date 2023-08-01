package com.mohan.procare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.mohan.procare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,email,password,phone;
     FirebaseAuth auth;
    FirebaseFirestore fstore;
    CheckBox customer,service;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth =FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        getSupportActionBar().hide();
        name=findViewById(R.id.name);
        email=findViewById(R.id.Email);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.pasword);
        customer=findViewById(R.id.customer);
        service=findViewById(R.id.service_provider);


        //check boxes logic
        customer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    Button signUpButton = findViewById(R.id.button);
                    signUpButton.setText("Sign Up");
                    service.setChecked(false);
                }
            }
        });

        service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    Button signUpButton = findViewById(R.id.button);
                    signUpButton.setText("Next");
                    customer.setChecked(false);
                }
            }
        });
    }
    //signup intent
    public void signup(View view) {

        String username = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPhone = phone.getText().toString();
        String userPassword = password.getText().toString();


            //validation
        boolean check = validateinfo(username, userEmail,userPhone, userPassword);
        //check the box validation
        if(!(customer.isChecked()||service.isChecked())){
            Toast.makeText(RegistrationActivity.this, "Select the Account type", Toast.LENGTH_SHORT).show();
        return;
        }

        if (check == true) {

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user=auth.getCurrentUser();
                            Toast.makeText(RegistrationActivity.this, "Successfully register", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fstore.collection("Users").document(user.getUid());
                            Map<String, Object> userinfo = new HashMap<>();
                            //pushing the data into the firebase
                            userinfo.put("FullName",username);
                            userinfo.put("UserEmail",userEmail);
                            userinfo.put("PhoneNumber",userPhone);


                            //specify the if the user customer or service provider
                            if(customer.isChecked()){
                                userinfo.put("customer","customer");
                            }
                            if(service.isChecked()){
                                userinfo.put("serviceprovider","serviceprovider");
                            }
                            df.set(userinfo);

                            if(customer.isChecked()){
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                finish();
                            }

                            if(service.isChecked()){
                                startActivity(new Intent(RegistrationActivity.this, QualificationActivity.class));
                                finish();
                            }


                        } else {

                            Toast.makeText(RegistrationActivity.this, "Registration Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
        else {
            Toast.makeText(RegistrationActivity.this, "Sorry check information again", Toast.LENGTH_SHORT).show();
        }

//        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }

    //sign in intent
    public void signin(View view) {
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));

    }
    //validation of the user name
    private Boolean validateinfo(String username,String useremail,String userphone, String userpassword){

        if(username.length()==0) {
            name.requestFocus();
            name.setError("FIELD CANNOT BE EMPTY");
            return false;
        } else if (!username.matches("[a-zA-Z]+")) {
            name.requestFocus();
            name.setError("ENTER ONLY ALPHABETICAL CHARACTER");
            return false;
        } else if (useremail.length()==0){
            email.requestFocus();
            email.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if(!useremail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            email.requestFocus();
            email.setError("ENTER THE VALID EMAIL");
            return false;
        } else if (!(userphone.length()==10)) {
            name.requestFocus();
            name.setError("PHONE NUMBER SHOULD BE 10");
            return false;
        } else if(userpassword.length()<=5){
            password.requestFocus();
            password.setError("MINIMUM 6 CHARACTER REQUIRED");
            return false;
        }
        else {
            return true;
        }
    }
}