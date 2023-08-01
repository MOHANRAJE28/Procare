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

public class S_completeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Complete> completeArrayList;
    CompleteAdapter myAdapter;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    String semail;
    private FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    public class Complete {
        String CustomerTime, CustomerDate, CustomerDescription, CustomerEmail, CustomerName, CustomerNumber, CProfile, ServiceType;
        Double Clatitude, Clongitude;

        public Complete() {
        }

        public Complete(String customerTime, String customerDate, String customerDescription, String customerEmail, String customerName, String customerNumber, String CProfile, String serviceType, Double clatitude, Double clongitude) {
            CustomerTime = customerTime;
            CustomerDate = customerDate;
            CustomerDescription = customerDescription;
            CustomerEmail = customerEmail;
            CustomerName = customerName;
            CustomerNumber = customerNumber;
            this.CProfile = CProfile;
            ServiceType = serviceType;
            Clatitude = clatitude;
            Clongitude = clongitude;
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
        setContentView(R.layout.activity_scomplete);

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

        completeArrayList = new ArrayList<>();
        myAdapter = new CompleteAdapter(S_completeActivity.this, completeArrayList);
        recyclerView.setAdapter(myAdapter);

        showuserdata();
    }

    private void EventchangeLister() {
        CollectionReference completeCollectionRef = fstore.collection("Complete");
        completeCollectionRef.whereEqualTo("ProviderEmail", semail)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        completeArrayList.clear();
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
                            String servicetype = document.getString("ServiceType");
                            Complete complete = new Complete(customerTime, customerDate, customerDescription,
                                    customerEmail, customerName, customerNumber, cProfile, servicetype, clatitude, clongitude);
                            completeArrayList.add(complete);
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
            Toast.makeText(S_completeActivity.this, semail, Toast.LENGTH_SHORT).show();
            EventchangeLister();
        } else {
            startActivity(new Intent(S_completeActivity.this, LoginActivity.class));
        }
    }

    public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.MyViewHolder> {
        Context context;
        ArrayList<Complete> completeArrayList;

        public CompleteAdapter(Context context, ArrayList<Complete> completeArrayList) {
            this.context = context;
            this.completeArrayList = completeArrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.itemlist, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull S_completeActivity.CompleteAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            S_completeActivity.Complete complete = completeArrayList.get(position);
            holder.cname.setText(complete.getCustomerName());
            holder.cphone.setText(complete.getCustomerNumber());
            holder.servicename.setText(complete.getCustomerDescription());
            holder.cemail.setText(complete.getCustomerEmail());

            if (complete.getCProfile() != null) {
                Glide.with(context)
                        .load(complete.getCProfile())
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
                    Intent intent = new Intent(context, S_completeActivity2.class);
                    intent.putExtra("name", complete.getCustomerName());
                    intent.putExtra("phone", complete.getCustomerNumber());
                    intent.putExtra("email", complete.getCustomerEmail());
                    intent.putExtra("date", complete.getCustomerDate());
                    intent.putExtra("time", complete.getCustomerTime());
                    intent.putExtra("description", complete.getCustomerDescription());
                    intent.putExtra("clongitude", complete.getClongitude());
                    intent.putExtra("clatitude", complete.getClatitude());
                    intent.putExtra("profile", complete.getCProfile());
                    intent.putExtra("servicetype", complete.getServiceType());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return completeArrayList.size();
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
