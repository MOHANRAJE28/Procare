package com.mohan.procare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mohan.procare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EletricbookActivity extends AppCompatActivity {

    CircleImageView profileimg;
    TextView sname, servicename, number;
    ImageView sphone, smessage;
    Button request;
    RecyclerView recyclerView;
    ArrayList<Product> productList;
    ProductAdapter productAdapter;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    ProgressDialog progressDialog;
    CollectionReference productCollectionRef;

    public class Product {
        private String id;
        private String name;
        private String charge,duration;
        private String photoUrl;

        public Product(String id, String name, String charge, String photoUrl,String duration) {
            this.id = id;
            this.name = name;
            this.charge = charge;
            this.photoUrl = photoUrl;
            this.duration =duration;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCharge() {
            return charge;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public String getDuration() {
            return duration;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eletricbook);
        getSupportActionBar().hide();
        profileimg = findViewById(R.id.profile_image);
        sname = findViewById(R.id.sname);
        servicename = findViewById(R.id.service);
        sphone = findViewById(R.id.phone);
        smessage = findViewById(R.id.message);
        request = findViewById(R.id.request);
        number = findViewById(R.id.number);
//
//        progressDialog = new ProgressDialog(EletricbookActivity.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching Data");
//        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(EletricbookActivity.this));
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);

        recyclerView.setAdapter(productAdapter);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String profile = intent.getStringExtra("profile");
        String service = intent.getStringExtra("servicename");
        String email = intent.getStringExtra("email");

        loadProducts(email);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EletricbookActivity.this, RequestsActivity.class);
                intent.putExtra("profile",profile);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("servicename", service);

                startActivity(intent);
            }
        });

        sphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        smessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phone));
                startActivity(intent);
            }
        });

        sname.setText(name);
        servicename.setText(service);
        number.setText(phone);

        String profileUrl = intent.getStringExtra("profile");

        if (profileUrl != null && !profileUrl.isEmpty()) {
            Glide.with(getApplication()).load(profileUrl).into(profileimg);
        } else {
            Glide.with(getApplication()).load(R.drawable.dprofile).into(profileimg);
        }

    }

    private void loadProducts(String email) {
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        CollectionReference userCollectionRef = fstore.collection("Users");
        Query query = userCollectionRef.whereEqualTo("UserEmail", email);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot userDocument : task.getResult()) {
                    String userId = userDocument.getId();
                    productCollectionRef = userCollectionRef.document(userId).collection("product");

                    productCollectionRef.get().addOnCompleteListener(productTask -> {
                        if (productTask.isSuccessful()) {
                            productList.clear();
                            for (DocumentSnapshot productDocument : productTask.getResult()) {
                                String productName = productDocument.getString("productName");
                                String productCharge = productDocument.getString("productCharge");
                                String productPhoto = productDocument.getString("Productphoto");
                                String duration =productDocument.getString("duration");

                                Product product = new Product(productDocument.getId(), productName, productCharge, productPhoto,duration);
                                productList.add(product);
                            }

                            productAdapter.notifyDataSetChanged();

                            if (productList.isEmpty()) {
//                                progressDialog.dismiss();
                            }
                        } else {
//                            progressDialog.dismiss();
                            Log.e("Firestore error", productTask.getException().getMessage());
                        }
                    });
                }
            } else {
//                progressDialog.dismiss();
                Log.e("Firestore error", task.getException().getMessage());
            }
        });
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

        private List<Product> productList;

        public ProductAdapter(List<Product> productList) {
            this.productList = productList;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            Product product = productList.get(position);

            // Set the product data to the views in the item layout
            holder.productName.setText(product.getName());
            holder.productCharge.setText(product.getCharge());
            holder.duration.setText(product.getDuration());
            // Load the product photo using Glide or any other image loading library
            Glide.with(holder.itemView.getContext())
                    .load(product.getPhotoUrl())
                    .placeholder(R.drawable.dprofile) // Replace with your placeholder image
                    .into(holder.productImage);
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {

            ImageView productImage;
            TextView productName, productCharge,duration;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);

                productImage = itemView.findViewById(R.id.profile_image);
                productName = itemView.findViewById(R.id.electriname);
                productCharge = itemView.findViewById(R.id.electriph);
                duration=itemView.findViewById(R.id.servicename);
            }
        }
    }
}





//package com.example.procare;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
//import com.bumptech.glide.request.RequestOptions;
//import com.bumptech.glide.request.target.CustomTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import org.w3c.dom.Text;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class EletricbookActivity extends AppCompatActivity {
//
//    CircleImageView profileimg;
//    TextView sname,servicename,number;
//    ImageView sphone,smessage;
//    Button request;
//    RecyclerView recyclerView;
//    ArrayList<Booking> bookingArrayList;
//    //RequestAdapter myAdapter;
//    BookingAdapter myAdapter;
//    FirebaseAuth auth;
//    FirebaseFirestore fstore;
//    String cemail;
//    private static final int PICK_IMAGE_REQUEST = 1;
//
//    private FirebaseUser firebaseUser;
//    private ProgressBar progressBar;
//    private StorageReference storageReference;
//
//
//
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_eletricbook);
//        getSupportActionBar().hide();
//        profileimg =findViewById(R.id.profile_image);
//        sname=findViewById(R.id.sname);
//        servicename=findViewById(R.id.service);
//        sphone =findViewById(R.id.phone);
//        smessage=findViewById(R.id.message);
//        request=findViewById(R.id.request);
//        number=findViewById(R.id.number);
//
//        progressDialog = new ProgressDialog(EletricbookActivity.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching Data");
//        progressDialog.show();
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(EletricbookActivity.this));
//        auth = FirebaseAuth.getInstance();
//        fstore = FirebaseFirestore.getInstance();
//
//        productList = new ArrayList<>();
//        productAdapter = new S_PriceAddFragment.ProductAdapter(productList);
//
//        recyclerView.setAdapter(productAdapter);
//
//        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DocumentReference userDocRef = fstore.collection("Users").document(user.getUid());
//        productCollectionRef = userDocRef.collection("product");
//        loadProducts();
//
//
//        Intent intent =getIntent();
//        String name=intent.getExtras().getString("name");
//        String phone=intent.getExtras().getString("phone");
//        String profile=intent.getExtras().getString("profile");
//        String service=intent.getExtras().getString("servicename");
//        String email=intent.getExtras().getString("email");
//
//
//        request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent(EletricbookActivity.this,RequestsActivity.class);
//                intent.putExtra("profile",phone);
//                intent.putExtra("name",name);
//                intent.putExtra("phone",phone);
//                intent.putExtra("email",email);
//                intent.putExtra("servicename",service);
//                v.getContext().startActivity(intent);
//            }
//        });
//
//
//
//        sphone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:" + phone));
//                startActivity(intent);
//            }
//        });
//
//
//        smessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setData(Uri.parse("smsto:" + phone));
//                startActivity(intent);
//            }
//        });
//
//        sname.setText(name);
//        servicename.setText(service);
//        number.setText(phone);
//
//
//        Glide.with(this).load(profile).apply(RequestOptions.centerCropTransform())
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        profileimg.setBackground(resource);
//                    }
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                        // Do nothing
//                    }
//                });
//
//    }
//
//
//
//    private class ProductAdapter extends RecyclerView.Adapter<S_PriceAddFragment.ProductViewHolder> {
//        private List<S_PriceAddFragment.Product> productList;
//
//        public ProductAdapter(List<S_PriceAddFragment.Product> productList) {
//            this.productList = productList;
//        }
//        @NonNull
//        @Override
//        public S_PriceAddFragment.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
//            return new S_PriceAddFragment.ProductViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull S_PriceAddFragment.ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
//            S_PriceAddFragment.Product product = productList.get(position);
//
//            holder.productName.setText(product.getName());
//            holder.productCharge.setText(product.getCharge());
//
//            // Load the product photo using Glide or any other image loading library
//            Glide.with(requireContext())
//                    .load(product.getPhotoUrl())
//                    .placeholder(R.drawable.dprofile)
//                    .into(holder.productImage);
//
//
//
//
//        }
//        @Override
//        public int getItemCount() {
//            return productList.size();
//        }
//    }
//
//
//
//
//
//
//}
