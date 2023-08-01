package com.mohan.procare;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohan.procare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class S_PriceAddFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    private List<Product> productList;
    Button add;
    ProgressDialog progressDialog;
    private CollectionReference productCollectionRef;

        public class Product {
            private String id;
            private String name;
            private String charge;
            private String photoUrl, duration;

            public Product(String id, String name, String charge, String photoUrl,String duration) {
                this.id = id;
                this.name = name;
                this.charge = charge;
                this.photoUrl = photoUrl;
                this.duration = duration;
            }

            public String getDuration() {
                return duration;
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
        }

        @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s__book, container, false);

        add =view.findViewById(R.id.add);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            auth = FirebaseAuth.getInstance();
            fstore = FirebaseFirestore.getInstance();

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);

        recyclerView.setAdapter(productAdapter);

        // Get a reference to your product collection in Firestore
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference userDocRef = fstore.collection("Users").document(user.getUid());
        productCollectionRef = userDocRef.collection("product");
        loadProducts();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PriceAddActivity.class));
            }
        });

        return view;
    }

    @SuppressLint("RestrictedApi")
    private void loadProducts() {
        // Fetch the products from Firestore
        productCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productList.clear();
                for (DocumentSnapshot document : task.getResult()) {

                    if (error != null) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("Firestore error", error.toString());
                    }
                    // Extract the necessary data from the document
                    String productName = document.getString("productName");
                    String productCharge = document.getString("productCharge");
                    String productPhoto = document.getString("Productphoto");
                    String duration = document.getString("duration");

// Create a Product object and add it to the list
                    Product product = new Product(document.getId(), productName, productCharge, productPhoto, duration);
                    productList.add(product);
                }

                // Notify the adapter that the data has changed
                productAdapter.notifyDataSetChanged();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (productList.isEmpty() && progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }, 50);
            } else {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.e("Firestore error", task.getException().getMessage());
            }
        });

    }

    // ViewHolder class for holding the views of each item in the RecyclerView
    private static class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage,edit,delete;
        private TextView productName;
        private TextView productCharge,duration;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productCharge = itemView.findViewById(R.id.product_charge);
            edit =itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
            duration=itemView.findViewById(R.id.duration);

        }
    }

    // Adapter class for the RecyclerView
    private class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
        private List<Product> productList;

        public ProductAdapter(List<Product> productList) {
            this.productList = productList;
        }
        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Product product = productList.get(position);

            holder.productName.setText(product.getName());
            holder.productCharge.setText(product.getCharge());
            holder.duration.setText(product.getDuration());

            // Load the product photo using Glide or any other image loading library
            Glide.with(requireContext())
                    .load(product.getPhotoUrl())
                    .placeholder(R.drawable.dprofile)
                    .into(holder.productImage);

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productId = productList.get(position).getId();
                    Log.d("EditProductActivity", "Product ID: " + productId);
                    Intent intent = new Intent(getContext(), EditProductActivity.class);
                    intent.putExtra("productId", productId);
                    startActivity(intent);

                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Selecting ", Toast.LENGTH_SHORT).show();

                    String productId = productList.get(position).getId();

                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            DocumentReference productRef = fstore.collection("Users")
                                    .document(user.getUid())
                                    .collection("product")
                                    .document(productId);

                            productRef.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Failed to delete product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }


            });
        }
        @Override
        public int getItemCount() {
            return productList.size();
        }
    }
}
