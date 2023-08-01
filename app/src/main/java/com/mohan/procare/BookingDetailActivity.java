package com.mohan.procare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mohan.procare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingDetailActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    CircleImageView profileimg;
    private FirebaseUser firebaseUser;
    TextView cname, serviceanme, desc, sdate, stime, request;

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
        request = findViewById(R.id.approve);
        profileimg =findViewById(R.id.profile_image);




        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String phone = intent.getExtras().getString("phone");
        String description = intent.getExtras().getString("description");
        String date = intent.getExtras().getString("date");
        String time = intent.getExtras().getString("time");
        String email = intent.getExtras().getString("email");
        String profile = intent.getStringExtra("profile");
        String approve = intent.getExtras().getString("approve");


        //
        String profileUrl = intent.getStringExtra("profile");

        if (profileUrl != null && !profileUrl.isEmpty()) {
            Glide.with(getApplication()).load(profileUrl).into(profileimg);
        } else {
            Glide.with(getApplication()).load(R.drawable.dprofile).into(profileimg);
        }


//        showuserdata();

        cname.setText(name);
        serviceanme.setText(phone);
        desc.setText(description);
        sdate.setText(date);
        stime.setText(time);
        request.setText(approve);

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


        public void showuserdata() {
            auth = FirebaseAuth.getInstance();
            fstore = FirebaseFirestore.getInstance();
            firebaseUser = auth.getCurrentUser();

            if (auth.getCurrentUser() != null) {
                DocumentReference df = fstore.collection("Booking").document(auth.getCurrentUser().getUid());
                df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            request.setText(value.getString("Request"));

                        }
                    }
                });

            } else {
                startActivity(new Intent(BookingDetailActivity.this, LoginActivity.class));
            }

        }
    }
