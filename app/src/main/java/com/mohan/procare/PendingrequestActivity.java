package com.mohan.procare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mohan.procare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PendingrequestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Request> requestArrayList;
    RequestAdapter myAdapter;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    String semail;
    private FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendingrequest);

        auth =FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fstore = FirebaseFirestore.getInstance();
        requestArrayList = new ArrayList<Request>();
//        myAdapter = new RequestAdapter(PendingrequestActivity.this,  requestArrayList);
        myAdapter = new RequestAdapter(PendingrequestActivity.this, requestArrayList);

        recyclerView.setAdapter(myAdapter);
        showuserdata();
    }
    private void EventchangeLister() {
        fstore.collection("Request")
                .whereEqualTo("ProviderEmail", semail) // Filter requests by ProviderEmail field
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        requestArrayList.clear(); // Clear the previous list
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                requestArrayList.add(dc.getDocument().toObject(Request.class));
                            }
                        }

                        myAdapter.notifyDataSetChanged();
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }





    public void showuserdata() {
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            semail = firebaseUser.getEmail(); // Retrieve the email of the current user
            Toast.makeText(PendingrequestActivity.this, semail, Toast.LENGTH_SHORT).show(); // Display the email in a toast message
            // Call EventchangeLister() here to fetch requests for the current user
            EventchangeLister();
        } else {
            // Handle the case when the user is not authenticated
            // For example, you can redirect them to the login screen or display a message indicating that they need to log in
            startActivity(new Intent(PendingrequestActivity.this, LoginActivity.class));
        }
    }



}

//   .whereEqualTo("ProviderEmail", "vk2gmail.com")