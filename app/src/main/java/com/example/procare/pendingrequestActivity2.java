package com.example.procare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

public class pendingrequestActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView cname, cphone, desc, sdate, stime;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendingrequest2);

        cname = findViewById(R.id.cname);
        cphone = findViewById(R.id.phonenumber);
        desc = findViewById(R.id.description);
        sdate = findViewById(R.id.date);
        stime = findViewById(R.id.time);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String phone = intent.getExtras().getString("phone");
        String description = intent.getExtras().getString("description");
        String date = intent.getExtras().getString("date");
        String time = intent.getExtras().getString("time");
        String email = intent.getExtras().getString("email");

        latitude = intent.getDoubleExtra("clatitude", 0.0);
        longitude = intent.getDoubleExtra("clongitude", 0.0);


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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    mapFragment.getMapAsync( pendingrequestActivity2.this);
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

    public void accept(View view){





    }



}