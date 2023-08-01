package com.mohan.procare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohan.procare.R;


public class S_HomeFragment extends Fragment {

    CardView  pending,complete,active;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s__home, container, false);

        pending =view.findViewById(R.id.pending);
        complete=view.findViewById(R.id.complete);
        active =view.findViewById(R.id.active);
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),S_ActiveRequestActivity.class));
            }
        });


        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),PendingrequestActivity.class));
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),S_completeActivity
                        .class));
            }
        });







        return view;

    }
}