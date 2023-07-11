package com.example.procare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class S_HomeFragment extends Fragment {

    CardView  pending;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s__home, container, false);
        pending =view.findViewById(R.id.pending);

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),PendingrequestActivity.class));
            }
        });







        return view;

    }
}