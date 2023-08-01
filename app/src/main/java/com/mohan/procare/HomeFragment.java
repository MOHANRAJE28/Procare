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

public class HomeFragment extends Fragment {

    CardView electrician,plumber,carpenter,pestcontrol;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        electrician=view.findViewById(R.id.electrician);
        plumber=view.findViewById(R.id.plumber);
        carpenter=view.findViewById(R.id.carpenter);
        pestcontrol=view.findViewById(R.id.pestcontrol);
        electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ElectricianListActivity.class));
            }
        });

        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),PlumberListActivity.class));
            }
        });


        carpenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),CarpenterListActivity.class));
            }
        });


        pestcontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),PestControlListActivity.class));
            }
        });





        return view;

    }
}