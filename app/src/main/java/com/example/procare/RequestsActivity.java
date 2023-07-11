package com.example.procare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RequestsActivity extends AppCompatActivity  implements OnMapReadyCallback {
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    private FirebaseUser firebaseUser;

    EditText date,time,description;
    String cemail;
    String cname,cphone;
    String name,email,phone,type,service,profile;
    Button send;
    //google map
    private GoogleMap mMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth =FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_requests);

        date =findViewById(R.id.date);
        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    Calendar calendar=Calendar.getInstance();
                    DatePickerDialog datePickerDialog=new DatePickerDialog(RequestsActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    month=month+1;
                                    date.setText(dayOfMonth+"-"+month+"-"+year);
                                }
                            },
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();}
                return false;
            }
        });

        time = findViewById(R.id.time);
        time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar calendar = Calendar.getInstance();
                    TimePickerDialog timePickerDialog = new TimePickerDialog(RequestsActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String ampm;
                                    if (hourOfDay >= 12) {
                                        hourOfDay = hourOfDay - 12;
                                        ampm = "PM";
                                    } else {
                                        ampm = "AM";
                                    }
                                    time.setText(hourOfDay + ":" + minute+" "+ampm);
                                }
                            },
                            calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);
                    timePickerDialog.show();

                }
                return false;
            }
        });
        description =findViewById(R.id.description);
        Intent intent =getIntent();
        name=intent.getExtras().getString("name");
        phone=intent.getExtras().getString("phone");
        profile=intent.getExtras().getString("profile");
        service=intent.getExtras().getString("servicename");
        email=intent.getExtras().getString("email");
        description.setText(service);
        showuserdata();
        //google map
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        //
        send=findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sname = name; //service provider name
                String semail = email; //service provide email
                String sphone = phone; //service provide phone
                String stype= service;  // service type
                String Date=date.getText().toString();
                String Time =time.getText().toString();
                String Description = description.getText().toString();
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();

                //request collection
                DocumentReference df = fstore.collection("Request").document(cemail);
                Map<String, Object> userinfo = new HashMap<>();
                //pushing the data into the firebase
                userinfo.put("CustomerName",cname);
                userinfo.put("CustomerEmail",cemail);
                userinfo.put("CustomerNumber",cphone);
                userinfo.put("ServiceType",stype);
                userinfo.put("CustomerDescription",Description);
                userinfo.put("CustomerDate",Date);
                userinfo.put("CustomerTime",Time);
                userinfo.put("ProviderName",sname);
                userinfo.put("ProviderEmail",semail);
                userinfo.put("ProviderNumber",sphone);
                userinfo.put("Clatitude",latitude);
                userinfo.put("Clongitude",longitude);


                df.set(userinfo);

                //booking collection
                DocumentReference df1 = fstore.collection("Bookings").document(semail);
                Map<String, Object> userinfor = new HashMap<>();
                //pushing the data into the firebase
                userinfor.put("CustomerName",cname);
                userinfor.put("CustomerEmail",cemail);
                userinfor.put("CustomerNumber",cphone);
                userinfor.put("CustomerDescription",Description);
                userinfor.put("CustomerDate",Date);
                userinfor.put("CustomerTime",Time);
                userinfo.put("ServiceType",stype);
                userinfor.put("ProviderName",sname);
                userinfor.put("ProviderEmail",semail);
                userinfor.put("ProviderNumber",sphone);
                df1.set(userinfor);
                Toast.makeText(RequestsActivity.this, "Successfully send the request", Toast.LENGTH_SHORT).show();
                //service side request
                //customerName," "customerEmail," "serviceType," "requestDate", collection //Requests
            }
        });






    }

    //google map
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
                    mapFragment.getMapAsync( RequestsActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        float zoomLevel = 15f; // Set your desired zoom level here
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
                        cemail= value.getString("UserEmail"); //customer email
                         cname = value.getString("FullName");
                          cphone=value.getString("PhoneNumber");// customer name
                    }
                }
            });
        } else {
            // Handle the case when the user is not authenticated
            // For example, you can redirect them to the login screen or display a message indicating that they need to log in
            startActivity(new Intent(RequestsActivity.this,LoginActivity.class));
        }
    }
}