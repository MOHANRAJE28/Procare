package com.example.procare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class S_AddressActivity extends AppCompatActivity {
    EditText houseno,street,distate,pincode;
    Button submit;
    private FirebaseAuth auth;
    FirebaseFirestore fstore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saddress);
        auth= FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        houseno =findViewById(R.id.houseno);
        street =findViewById(R.id.street);
        distate =findViewById(R.id.distate);
        pincode=findViewById(R.id.pincode);
        submit =findViewById(R.id.submit);
    }
    public void submit(View view) {

        String S_houseno = houseno.getText().toString();
        String S_street =street.getText().toString();
        String S_distate =distate.getText().toString();
        String S_pincode =pincode.getText().toString();

        //validation
        boolean check = validateinfo(S_houseno, S_street,S_distate,S_pincode);
        if(check == true){
            String address= S_houseno+","+ S_street+","+S_distate+","+S_pincode;
            FirebaseUser user=auth.getCurrentUser();
            DocumentReference df = fstore.collection("Users").document(user.getUid());
            Map<String, Object> userinfo = new HashMap<>();
            //pushing the data into the firebase
            userinfo.put("Address",address);
            df.update(userinfo);
            Toast.makeText(S_AddressActivity.this, "completed the qualification ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(S_AddressActivity.this, S_HomeActivity.class));
            finish();
        }

    }

    private boolean validateinfo(String s_houseno, String s_street, String s_distate, String s_pincode) {

        if(s_houseno.length()==0) {
            houseno.requestFocus();
            houseno.setError("FIELD CANNOT BE EMPTY");
            return false;

        } else if (s_street.length()==0) {
            street.requestFocus();
            street.setError("FIELD CANNOT BE EMPTY");
            return false;
    }
        else if (s_distate.length()==0) {
            distate.requestFocus();
            distate.setError("FIELD CANNOT BE EMPTY");
            return false;

        }else if (s_pincode.length()==0) {
            pincode.requestFocus();
            pincode.setError("FIELD CANNOT BE EMPTY");
            return false;
        }else if (!s_pincode.matches("[0-9]+")) {
            pincode.requestFocus();
            pincode.setError("ENTER ONLY ALPHABETICAL CHARACTER");
            return false;
        }else {
        return true;}
    }




}