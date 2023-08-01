

//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//public class S_BookFragment extends Fragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_s__book, container, false);
//
//
//
//
//
//        return view;
//    }
//}

// PriceAddActivity.java

package com.mohan.procare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;



public class Product {
    private String name;
    private String charge;
    private String photoUrl;

    public Product() {
        // Empty constructor needed for Firestore deserialization
    }

    public Product(String name, String charge, String photoUrl) {
        this.name = name;
        this.charge = charge;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getCharge() {
        return charge;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}

