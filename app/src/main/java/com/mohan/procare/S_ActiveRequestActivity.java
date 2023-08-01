//package com.example.procare;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.core.app.ActivityCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentChange;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class S_ActiveRequestActivity extends AppCompatActivity  {
//    RecyclerView recyclerView;
//    ArrayList<Active> ActiveArrayList;
//    ActiveAdapter myAdapter;
//    FirebaseAuth auth;
//    FirebaseFirestore fstore;
//    String semail;
//    private FirebaseUser firebaseUser;
//    ProgressDialog progressDialog;
//
//  public class Active{
//      String CustomerTime,CustomerDate,CustomerDescription,CustomerEmail,CustomerName,CustomerNumber,CProfile;
//      Double Clatitude,Clongitude;
//
//      public Active(){}
//      public Active(String customerTime, String customerDate, String customerDescription, String customerEmail, String customerName, String customerNumber, String CProfile, Double clatitude, Double clongitude) {
//          CustomerTime = customerTime;
//          CustomerDate = customerDate;
//          CustomerDescription = customerDescription;
//          CustomerEmail = customerEmail;
//          CustomerName = customerName;
//          CustomerNumber = customerNumber;
//          this.CProfile = CProfile;
//          Clatitude = clatitude;
//          Clongitude = clongitude;
//      }
//
//      public String getCustomerTime() {
//          return CustomerTime;
//      }
//
//      public void setCustomerTime(String customerTime) {
//          CustomerTime = customerTime;
//      }
//
//      public String getCustomerDate() {
//          return CustomerDate;
//      }
//
//      public void setCustomerDate(String customerDate) {
//          CustomerDate = customerDate;
//      }
//
//      public String getCustomerDescription() {
//          return CustomerDescription;
//      }
//
//      public void setCustomerDescription(String customerDescription) {
//          CustomerDescription = customerDescription;
//      }
//
//      public String getCustomerEmail() {
//          return CustomerEmail;
//      }
//
//      public void setCustomerEmail(String customerEmail) {
//          CustomerEmail = customerEmail;
//      }
//
//      public String getCustomerName() {
//          return CustomerName;
//      }
//
//      public void setCustomerName(String customerName) {
//          CustomerName = customerName;
//      }
//
//      public String getCustomerNumber() {
//          return CustomerNumber;
//      }
//
//      public void setCustomerNumber(String customerNumber) {
//          CustomerNumber = customerNumber;
//      }
//
//      public String getCProfile() {
//          return CProfile;
//      }
//
//      public void setCProfile(String CProfile) {
//          this.CProfile = CProfile;
//      }
//
//      public Double getClatitude() {
//          return Clatitude;
//      }
//
//      public void setClatitude(Double clatitude) {
//          Clatitude = clatitude;
//      }
//
//      public Double getClongitude() {
//          return Clongitude;
//      }
//
//      public void setClongitude(Double clongitude) {
//          Clongitude = clongitude;
//      }
//  }
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sactive_request);
//
//        auth =FirebaseAuth.getInstance();
//        fstore=FirebaseFirestore.getInstance();
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching Data");
//        progressDialog.show();
//        recyclerView = findViewById(R.id.recycleView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        fstore = FirebaseFirestore.getInstance();
//        ActiveArrayList = new ArrayList<Active>();
//        myAdapter = new ActiveAdapter(S_ActiveRequestActivity.this,  ActiveArrayList);
//        recyclerView.setAdapter(myAdapter);
//        showuserdata();
//
//
//    }
//
//    private void EventchangeLister() {
//        fstore.collection("Active")
//                .whereEqualTo("ProviderEmail", semail) // Filter requests by ProviderEmail field
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            if (progressDialog != null && progressDialog.isShowing())
//                                progressDialog.dismiss();
//                            Log.e("Firestore error", error.getMessage());
//                            return;
//                        }
//                        ActiveArrayList.clear(); // Clear the previous list
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                ActiveArrayList.add(dc.getDocument().toObject(Active.class));
//                            }
//                        }
//
//                        myAdapter.notifyDataSetChanged();
//                        if (progressDialog != null && progressDialog.isShowing())
//                            progressDialog.dismiss();
//                    }
//                });
//    }
//    //
//    public void showuserdata() {
//        firebaseUser = auth.getCurrentUser();
//        if (firebaseUser != null) {
//            semail = firebaseUser.getEmail(); // Retrieve the email of the current user
//            Toast.makeText(S_ActiveRequestActivity.this, semail, Toast.LENGTH_SHORT).show(); // Display the email in a toast message
//            // Call EventchangeLister() here to fetch requests for the current user
//            EventchangeLister();
//        } else {
//            // Handle the case when the user is not authenticated
//            // For example, you can redirect them to the login screen or display a message indicating that they need to log in
//            startActivity(new Intent(S_ActiveRequestActivity.this, LoginActivity.class));
//        }
//    }
//        // adapter
//
//    // Adapter class for the RecyclerView
//
//
//    public class ActiveAdapter extends RecyclerView.Adapter<com.example.procare.ActiveAdapter.MyviewHolder> implements com.example.procare.ActiveAdapter {
//        Context context;
//        ArrayList<Active> ActiveArrayList;
//
//        public ActiveAdapter(Context context, ArrayList<Active> ActiveArrayList) {
//            this.context = context;
//            this.ActiveArrayList = ActiveArrayList;
//        }
//
//        @NonNull
//        @Override
//        public com.example.procare.ActiveAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(context).inflate(R.layout.itemlist, parent, false);
//            return new com.example.procare.ActiveAdapter.MyviewHolder(v);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull com.example.procare.RequestAdapter.MyviewHolder holder, @SuppressLint("RecyclerView") int position) {
//           Active request = ActiveArrayList.get(position);
//            holder.cname.setText(request.CustomerName);
//            holder.cphone.setText(request.CustomerNumber);
//            holder.cemail.setText(request.CustomerEmail);
//            holder.servicename.setText(request.CustomerDescription);
//
//            if(request.CProfile != null){
//                Glide.with(context)
//                        .load(request.getCProfile())
//                        .into(holder.profileimage);}
//            else {
//                Glide.with(context)
//                        .load(R.drawable.dprofile)
//                        .into(holder.profileimage);
//
//            }
//
//
//
//
//            holder.cardview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context,"card"+position,Toast.LENGTH_SHORT).show();
//                    Intent intent =new Intent(context, pendingrequestActivity2.class);
////                intent.putExtra("profile",requestArrayList.get(position).getProfilephoto());
//                    intent.putExtra("name",requestArrayList.get(position).getCustomerName());
//                    intent.putExtra("phone",requestArrayList.get(position).getCustomerNumber());
//                    intent.putExtra("email",requestArrayList.get(position).getCustomerEmail());
//                    intent.putExtra("date",requestArrayList.get(position).getCustomerDate());
//                    intent.putExtra("time",requestArrayList.get(position).getCustomerTime());
//                    intent.putExtra("description",requestArrayList.get(position).getCustomerDescription());
//                    intent.putExtra("clongitude",requestArrayList.get(position).getClongitude());
//                    intent.putExtra("clatitude",requestArrayList.get(position).getClatitude());
//                    intent.putExtra("profile",requestArrayList.get(position).getCProfile());
//                    context.startActivity(intent);
//                }
//            });
//
//
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return ActionArrayList.size();
//        }
//
//        public static class MyviewHolder extends RecyclerView.ViewHolder {
//
//            TextView cname, cphone, servicename, cemail;
//            CardView cardview;
//            CircleImageView profileimage;
//            public MyviewHolder(@NonNull View itemView) {
//                super(itemView);
//                cname = itemView.findViewById(R.id.electriname);
//                cphone = itemView.findViewById(R.id.electriph);
//                servicename = itemView.findViewById(R.id.servicename);
//                cemail = itemView.findViewById(R.id.email);
//                cardview =itemView.findViewById(R.id.cardviewelectri);
//                profileimage=itemView.findViewById(R.id.profile_image);
//            }
//        }
//    }
//
//
//
//
//}

package com.mohan.procare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mohan.procare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class S_ActiveRequestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Active> activeArrayList;
    ActiveAdapter myAdapter;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    String semail;
    private FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    public class Active {
        String CustomerTime, CustomerDate, CustomerDescription, CustomerEmail, CustomerName, CustomerNumber, CProfile,ServiceType,AKey;
        Double Clatitude, Clongitude;

        public Active() {
        }
        public Active(String customerTime, String customerDate, String customerDescription, String customerEmail, String customerName, String customerNumber, String CProfile, String serviceType, String AKey, Double clatitude, Double clongitude) {
            CustomerTime = customerTime;
            CustomerDate = customerDate;
            CustomerDescription = customerDescription;
            CustomerEmail = customerEmail;
            CustomerName = customerName;
            CustomerNumber = customerNumber;
            this.CProfile = CProfile;
            ServiceType = serviceType;
            this.AKey = AKey;
            Clatitude = clatitude;
            Clongitude = clongitude;
        }

        public String getAKey() {
            return AKey;
        }

        public void setAKey(String AKey) {
            this.AKey = AKey;
        }

        public String getServiceType() {
            return ServiceType;
        }

        public void setServiceType(String serviceType) {
            ServiceType = serviceType;
        }

        public String getCustomerTime() {
            return CustomerTime;
        }

        public void setCustomerTime(String customerTime) {
            CustomerTime = customerTime;
        }

        public String getCustomerDate() {
            return CustomerDate;
        }

        public void setCustomerDate(String customerDate) {
            CustomerDate = customerDate;
        }

        public String getCustomerDescription() {
            return CustomerDescription;
        }

        public void setCustomerDescription(String customerDescription) {
            CustomerDescription = customerDescription;
        }

        public String getCustomerEmail() {
            return CustomerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            CustomerEmail = customerEmail;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }

        public String getCustomerNumber() {
            return CustomerNumber;
        }

        public void setCustomerNumber(String customerNumber) {
            CustomerNumber = customerNumber;
        }

        public String getCProfile() {
            return CProfile;
        }

        public void setCProfile(String cProfile) {
            CProfile = cProfile;
        }

        public Double getClatitude() {
            return Clatitude;
        }

        public void setClatitude(Double clatitude) {
            Clatitude = clatitude;
        }

        public Double getClongitude() {
            return Clongitude;
        }

        public void setClongitude(Double clongitude) {
            Clongitude = clongitude;
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sactive_request);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fstore = FirebaseFirestore.getInstance();

        activeArrayList = new ArrayList<>();
        myAdapter = new ActiveAdapter(S_ActiveRequestActivity.this, activeArrayList);
        recyclerView.setAdapter(myAdapter);

        showuserdata();
    }

    private void EventchangeLister() {
        CollectionReference activeCollectionRef = fstore.collection("Active");
        activeCollectionRef.whereEqualTo("ProviderEmail", semail)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        activeArrayList.clear();
                        for (DocumentSnapshot document : value.getDocuments()) {
                            String customerTime = document.getString("CustomerTime");
                            String customerDate = document.getString("CustomerDate");
                            String customerDescription = document.getString("CustomerDescription");
                            String customerEmail = document.getString("CustomerEmail");
                            String customerName = document.getString("CustomerName");
                            String customerNumber = document.getString("CustomerNumber");
                            String cProfile = document.getString("CProfile");
                            Double clatitude = document.getDouble("Clatitude");
                            Double clongitude = document.getDouble("Clongitude");
                            String servicetype  = document.getString("ServiceType");
                            String key = document.getString("AKey");
                            String type = document.getString("ServiceType");

                            Active active = new Active(customerTime, customerDate, customerDescription,
                                    customerEmail, customerName, customerNumber, cProfile,servicetype,key, clatitude, clongitude);
                            activeArrayList.add(active);
                        }

                        myAdapter.notifyDataSetChanged();
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }

    public void showuserdata() {
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            semail = firebaseUser.getEmail();
            Toast.makeText(S_ActiveRequestActivity.this, semail, Toast.LENGTH_SHORT).show();
            EventchangeLister();
        } else {
            startActivity(new Intent(S_ActiveRequestActivity.this, LoginActivity.class));
        }
    }

    public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.MyViewHolder> {
        Context context;
        ArrayList<Active> activeArrayList;

        public ActiveAdapter(Context context, ArrayList<Active> activeArrayList) {
            this.context = context;
            this.activeArrayList = activeArrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.itemlist, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Active active = activeArrayList.get(position);
            holder.cname.setText(active.getCustomerName());
            holder.cphone.setText(active.getCustomerNumber());
            holder.servicename.setText(active.getCustomerDescription());
            holder.cemail.setText(active.getCustomerEmail());

            if (active.getCProfile() != null) {
                Glide.with(context)
                        .load(active.getCProfile())
                        .into(holder.profileimage);
            } else {
                Glide.with(context)
                        .load(R.drawable.dprofile)
                        .into(holder.profileimage);
            }

            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "card" + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, S_ActiveRequestActivity2.class);
                    intent.putExtra("name", active.getCustomerName());
                    intent.putExtra("phone", active.getCustomerNumber());
                    intent.putExtra("email", active.getCustomerEmail());
                    intent.putExtra("date", active.getCustomerDate());
                    intent.putExtra("time", active.getCustomerTime());
                    intent.putExtra("description", active.getCustomerDescription());
                    intent.putExtra("clongitude", active.getClongitude());
                    intent.putExtra("clatitude", active.getClatitude());
                    intent.putExtra("profile", active.getCProfile());
                    intent.putExtra("servicetype",active.getServiceType());
                    intent.putExtra("key",active.getAKey());
                   // intent.putExtra("type",active.getServiceType());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return activeArrayList.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            TextView cname, cphone, servicename, cemail;
            CardView cardview;
            CircleImageView profileimage;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                cname = itemView.findViewById(R.id.electriname);
                cphone = itemView.findViewById(R.id.electriph);
                servicename = itemView.findViewById(R.id.servicename);
                cemail = itemView.findViewById(R.id.email);
                cardview = itemView.findViewById(R.id.cardviewelectri);
                profileimage = itemView.findViewById(R.id.profile_image);
            }
        }
    }
}
