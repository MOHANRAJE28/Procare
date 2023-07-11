package com.example.procare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BookingDetailActivity extends AppCompatActivity {

    TextView cname, serviceanme, desc, sdate, stime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);


        cname = findViewById(R.id.cname);
        serviceanme = findViewById(R.id.service);
        desc = findViewById(R.id.description);
        sdate = findViewById(R.id.date);
        stime = findViewById(R.id.time);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String phone = intent.getExtras().getString("phone");
        String description = intent.getExtras().getString("description");
        String date = intent.getExtras().getString("date");
        String time = intent.getExtras().getString("time");
        String email =intent.getExtras().getString("email");


        cname.setText(name);
        serviceanme.setText(phone);
        desc.setText(description);
        sdate.setText(date);
        stime.setText(time);


        findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });


        findViewById(R.id.message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phone));
                startActivity(intent);
            }
        });












    }
}