package com.mohan.procare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mohan.procare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    FirebaseAuth auth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth =FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        email=findViewById(R.id.Email);
        password=findViewById(R.id.pasword);
    }

    public void signin(View view){

        String userEmail=email.getText().toString();
        String userPassword=password.getText().toString();
        boolean check = validateinfo(userEmail,userPassword);
        if (check == true) {
            auth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                checkuserAccessLevel(authResult.getUser().getUid()) ;
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(LoginActivity.this, "Sorry Check information Again ", Toast.LENGTH_SHORT).show();


        }

//        startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }
    public void signup(View view){
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }

// validation
    private Boolean validateinfo(String useremail, String userpassword){
        if (useremail.length()==0){
            email.requestFocus();
            email.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if(!useremail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            email.requestFocus();
            email.setError("ENTER THE VALID EMAIL");
            return false;
        }
        else if(userpassword.length()<=5){
            password.requestFocus();
            password.setError("MINIMUM 6 CHARACTER REQUIRED");
            return false;
        }
        else {
            return true;
        }

    }

    //
    private void checkuserAccessLevel(String uid){
        DocumentReference df = fstore.collection("Users").document(uid);
        //extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess"+documentSnapshot.getData());
                //identify the user access level
                if (documentSnapshot.getString("customer")!= null){
                    //user is customer
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                if(documentSnapshot.getString("serviceprovider")!=null){
                    startActivity(new Intent(getApplicationContext(),S_HomeActivity.class));
                    finish();
                }
            }
        });
    }
    //check the user have a exist account
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("customer")!= null){
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    if (documentSnapshot.getString("serviceprovider")!= null){
                        startActivity(new Intent(getApplicationContext(),S_HomeActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                }
            });
        }
    }
}







//code

//                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//
//                            if (task.isSuccessful()) {
//                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                                checkuserAccessLevel(authResult) ;
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                finish();
//
//                            } else {
//                                Toast.makeText(LoginActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
//                    });