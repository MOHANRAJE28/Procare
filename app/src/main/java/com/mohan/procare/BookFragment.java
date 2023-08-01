package com.mohan.procare;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class BookFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Booking> bookingArrayList;
    //RequestAdapter myAdapter;
    BookingAdapter myAdapter;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    String cemail;
    private FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        auth =FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fstore = FirebaseFirestore.getInstance();
        bookingArrayList = new ArrayList<Booking>();
        myAdapter = new BookingAdapter(getContext(), bookingArrayList);
        recyclerView.setAdapter(myAdapter);
        showuserdata();

        return view;
    }

    private void EventchangeLister() {
        fstore.collection("Bookings")
                .whereEqualTo("CustomerEmail", cemail) // Filter requests by ProviderEmail field
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        bookingArrayList.clear(); // Clear the previous list
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                bookingArrayList.add(dc.getDocument().toObject(Booking.class));
                            }
                        }

                        myAdapter.notifyDataSetChanged();
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }


    public void showuserdata
            () {
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            cemail = firebaseUser.getEmail(); // Retrieve the email of the current user
            Toast.makeText(getContext(), cemail, Toast.LENGTH_SHORT).show(); // Display the email in a toast message

            EventchangeLister();
        } else {

            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }
}