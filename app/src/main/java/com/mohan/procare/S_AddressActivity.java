package com.mohan.procare;//package com.example.procare;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.widget.Autocomplete;
//import com.google.android.libraries.places.widget.AutocompleteActivity;
//import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
//import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
//
//
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class S_AddressActivity extends AppCompatActivity implements OnMapReadyCallback {
//    EditText address;
//    private EditText searchEditText;
//    Button submit;
//    private FirebaseAuth auth;
//    FirebaseFirestore fstore;
//    Location currentLocation;
//    private PlacesClient placesClient;
//
//    private GoogleMap mMap;
//    Geocoder geocoder;
//    Button searchButton;
//    private final int FINE_PERMISSION_CODE = 1;
//    FusedLocationProviderClient fusedLocationProviderClient;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_saddress);
//        auth = FirebaseAuth.getInstance();
//        fstore = FirebaseFirestore.getInstance();
//        address = findViewById(R.id.address);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        geocoder = new Geocoder(this);
//        getLastLocation();
//
//
//        searchEditText = findViewById(R.id.searchEditText);
//
//
//
//
//    }
//
//
//    public void submit(View view) {
//
//        String S_address = address.getText().toString();
//        double latitude = currentLocation.getLatitude();
//        double longitude = currentLocation.getLongitude();
//
//        //validation
//        boolean check = validateinfo(S_address);
//        if (check == true) {
//            String address = S_address;
//            FirebaseUser user = auth.getCurrentUser();
//            DocumentReference df = fstore.collection("Users").document(user.getUid());
//            Map<String, Object> userinfo = new HashMap<>();
//            //pushing the data into the firebase
//            userinfo.put("Address", address);
//            userinfo.put("Slatitude",latitude);
//            userinfo.put("Slongitude",longitude);
//
//
//            df.update(userinfo);
//            Toast.makeText(S_AddressActivity.this, "completed the qualification ", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(S_AddressActivity.this, S_HomeActivity.class));
//            finish();
//        }
//
//    }
//
//    private boolean validateinfo(String S_address) {
//
//        if (S_address.length() == 0) {
//            address.requestFocus();
//            address.setError("FIELD CANNOT BE EMPTY");
//            return false;
//        }else {
//            return true;
//        }
//    }
//
//    //google map
//    private void getLastLocation() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
//            return;
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location != null){
//                    currentLocation =location;
//                    SupportMapFragment mapFragment = new SupportMapFragment();
//                    getSupportFragmentManager().beginTransaction().add(R.id.mapFragment, mapFragment).commit();
//                    mapFragment.getMapAsync( S_AddressActivity.this);
//                }
//
//                try {
//                    List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
//                    if (addresses != null && !addresses.isEmpty()) {
//                        String addressLine = addresses.get(0).getAddressLine(0);
//                        address.setText(addressLine);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        float zoomLevel = 15f; // Set your desired zoom level here
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == FINE_PERMISSION_CODE){
//            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                getLastLocation();
//            }else {
//                Toast.makeText(this,"Location permission is denied allow the permission",Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }
//}
//
//
//



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mohan.procare.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class S_AddressActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private EditText address;
    private Button submit;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private Marker currentLocationMarker;
    private GoogleMap mMap;
    private Marker manualMarker;

    private FirebaseAuth auth;
    FirebaseFirestore fstore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saddress);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        address = findViewById(R.id.address);
        submit = findViewById(R.id.submit);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public void submit(View view) {
        String S_address = address.getText().toString();

        // Validate address input
        if (S_address.isEmpty()) {
            address.setError("Field cannot be empty");
            address.requestFocus();
            return;
        }

        double latitude, longitude;

        if (manualMarker != null) {
            LatLng manualLatLng = manualMarker.getPosition();
            latitude = manualLatLng.latitude;
            longitude = manualLatLng.longitude;
        } else if (currentLocation != null) {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Process the address and coordinates
        // ...
        boolean check = validateinfo(S_address);
        if (check == true) {
            String address = S_address;
            FirebaseUser user = auth.getCurrentUser();
            DocumentReference df = fstore.collection("Users").document(user.getUid());
            Map<String, Object> userinfo = new HashMap<>();
            //pushing the data into the firebase
            userinfo.put("Address", address);
            userinfo.put("Slatitude",latitude);
            userinfo.put("Slongitude",longitude);
            df.update(userinfo);
        Toast.makeText(S_AddressActivity.this, "Completed the qualification", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(S_AddressActivity.this, S_HomeActivity.class));
        finish();
        }
    }

        private boolean validateinfo(String S_address) {

        if (S_address.length() == 0) {
            address.requestFocus();
            address.setError("FIELD CANNOT BE EMPTY");
            return false;
        }else {
            return true;
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    updateAddressField();
                    if (mMap != null) {
                        LatLng currentUserLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(currentUserLocation).title("My Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLocation));
                        float zoomLevel = 15f;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, zoomLevel));
                    }
                }
            }
        });
    }

    private void updateAddressField() {
        if (currentLocation != null) {
            try {
                Geocoder geocoder = new Geocoder(S_AddressActivity.this);
                List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                if (addresses != null && !addresses.isEmpty()) {
                    String addressLine = addresses.get(0).getAddressLine(0);
                    address.setText(addressLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);
        // Add zoom controls to the map
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    @Override
    public void onMapClick(LatLng latLng) {
        if (manualMarker != null) {
            manualMarker.remove();
        }
        manualMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Manual Location"));
        updateAddressField(latLng);

    }

    private void updateAddressField(LatLng latLng) {
        try {
            Geocoder geocoder = new Geocoder(S_AddressActivity.this);
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String addressLine = addresses.get(0).getAddressLine(0);
                address.setText(addressLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle permission request result if needed
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }
    }
}
