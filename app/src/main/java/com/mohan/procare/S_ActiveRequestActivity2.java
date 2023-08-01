package com.mohan.procare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mohan.procare.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class S_ActiveRequestActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    private FirebaseUser firebaseUser;

    private GoogleMap mMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;

    CircleImageView profileimg;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView cname, cphone, desc, sdate, stime;
    String  sname,semail,sphone;
    double latitude,longitude;
    String email;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sactive_request2);



        cname = findViewById(R.id.cname);
        cphone = findViewById(R.id.phonenumber);
        desc = findViewById(R.id.description);
        sdate = findViewById(R.id.date);
        stime = findViewById(R.id.time);
        profileimg =findViewById(R.id.profile_image);
        fstore = FirebaseFirestore.getInstance();


        showuserdata();
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String phone = intent.getExtras().getString("phone");
        String description = intent.getExtras().getString("description");
        String date = intent.getExtras().getString("date");
        String time = intent.getExtras().getString("time");
        email = intent.getExtras().getString("email");
        String profile =intent.getExtras().getString("profile");
        latitude = intent.getDoubleExtra("clatitude", 0.0);
        longitude = intent.getDoubleExtra("clongitude", 0.0);
        String servicetype = intent.getExtras().getString("servicetype");
        String key =intent.getExtras().getString("key");


        //accepts button
        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Toast.makeText(S_ActiveRequestActivity2.this,"successfully Approved",Toast.LENGTH_SHORT).show();

                String documentKey1 = fstore.collection("Complete").document().getId();
                DocumentReference df = fstore.collection("Complete").document(documentKey1);
                Map<String, Object> userinfo = new HashMap<>();
                //pushing the data into the firebase
                userinfo.put("CustomerName",name);
                userinfo.put("CustomerEmail",email);
                userinfo.put("CustomerNumber",phone);
//                userinfo.put("ServiceType",type);
                userinfo.put("CustomerDescription",description);
                userinfo.put("CustomerDate",date);
                userinfo.put("CustomerTime",time);
                userinfo.put("ProviderName",sname);
                userinfo.put("ProviderEmail",semail);
                userinfo.put("ProviderNumber",sphone);
                userinfo.put("CProfile",profile);
                userinfo.put("Clatitude",latitude);
                userinfo.put("Clongitude",longitude);
                userinfo.put("ServiceType",servicetype);
                df.set(userinfo);


                DocumentReference ref1 =fstore.collection("Active").document(key);
                ref1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to delete product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });







                Toast.makeText(S_ActiveRequestActivity2.this,"successfully Approved",Toast.LENGTH_SHORT).show();





            }
        });




        //profile pic loadset
        String profileUrl = intent.getStringExtra("profile");

        if (profileUrl != null && !profileUrl.isEmpty()) {
            Glide.with(getApplication()).load(profileUrl).into(profileimg);
        } else {
            Glide.with(getApplication()).load(R.drawable.dprofile).into(profileimg);
        }

        cname.setText(name);
        cphone.setText(phone);
        desc.setText(description);
        sdate.setText(date);
        stime.setText(time);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
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

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation =location;
                    SupportMapFragment mapFragment = new SupportMapFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.mapFragment, mapFragment).commit();
                    mapFragment.getMapAsync( S_ActiveRequestActivity2.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        float zoomLevel = 15f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(this,"Location permission is denied allow the permission",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showuserdata() {
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseUser=auth.getCurrentUser();
        if (auth.getCurrentUser() != null) {
            DocumentReference df = fstore.collection("Users").document(auth.getCurrentUser().getUid());
            df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null) {
                        semail= value.getString("UserEmail"); //customer email
                        sname = value.getString("FullName");
                        sphone=value.getString("PhoneNumber");
//                        cprofile=value.getString("Profilephoto");
                        // customer name
                    }
                }
            });
        } else {
            // Handle the case when the user is not authenticated
            // For example, you can redirect them to the login screen or display a message indicating that they need to log in
            startActivity(new Intent(S_ActiveRequestActivity2.this,LoginActivity.class));
        }
    }




}