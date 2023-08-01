package com.mohan.procare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mohan.procare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    FirebaseFirestore fstore;
    EditText charge,productname,duration;
    private ProgressBar progressBar;
    private StorageReference storageReference;

    //    ImageView image;
    CircleImageView image;

    ImageView delete,edit;
    Button upload,save;
    private  String PhotoUrl;
    private Uri uriImage;
    private String productId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);


        productname=findViewById(R.id.productname);
        charge=findViewById(R.id.charge);
        image=findViewById(R.id.profile_image);
        upload=findViewById(R.id.upload);
        delete=findViewById(R.id.delete);
       duration=findViewById(R.id.duration);
        edit=findViewById(R.id.edit);
        save=findViewById(R.id.save);
        productId = getIntent().getStringExtra("productId");
        Log.d("EditProductActivity", "Received Product ID: " + productId);



        progressBar=findViewById(R.id.progressbar);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("product");


        if (firebaseUser != null) {
            Uri photoUri = firebaseUser.getPhotoUrl();
            if (photoUri != null) {
                Picasso.with(EditProductActivity.this).load(photoUri).into(image);
            } else {
                Picasso.with(EditProductActivity.this).load(R.drawable.dprofile).into(image); //set default image
            }
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }


        //selecting the image from the storage
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFilechoser();
            }
        });
        // uploading the image to the storage
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });

        getProductDetails();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
                Toast.makeText(EditProductActivity.this,"Successfully uploaded",Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void getProductDetails() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && productId != null) { // Add null check for productId
            DocumentReference productRef = fstore.collection("Users")
                    .document(user.getUid())
                    .collection("product")
                    .document(productId);

            productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String productName = documentSnapshot.getString("productName");
                        String productCharge = documentSnapshot.getString("productCharge");
                        PhotoUrl = documentSnapshot.getString("Productphoto");
                        String gduration =documentSnapshot.getString("duration");
                        productname.setText(productName);
                        charge.setText(productCharge);
                        duration.setText(gduration);

                        // Load the product image using Picasso or any other image loading library
                        if (PhotoUrl != null && !PhotoUrl.isEmpty()) {
                            Picasso.with(EditProductActivity.this).load(PhotoUrl).into(image);
                        }
                    }
                }
            });
        } else {
            // Handle the case when productId is null
            Toast.makeText(EditProductActivity.this, "Invalid product ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProduct() {
        String p_name = productname.getText().toString();
        String p_charge = charge.getText().toString();
        String p_duration =duration.getText().toString();
        boolean check = validateinfo(p_name, p_charge,p_duration);

        if (check) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                DocumentReference productRef = fstore.collection("Users")
                        .document(user.getUid())
                        .collection("product")
                        .document(productId);

                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("productName", p_name);
                updatedData.put("productCharge", p_charge);
                updatedData.put("duration",p_duration);

                if (PhotoUrl != null) {
                    updatedData.put("Productphoto", PhotoUrl);
                }

                productRef.update(updatedData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(EditProductActivity.this, "Product details updated successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProductActivity.this,S_PriceAddFragment.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProductActivity.this, "Failed to update product details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private boolean validateinfo(String p_name, String p_charge,String p_duration) {

        if(p_name.length()==0) {
            productname.requestFocus();
            productname.setError("FIELD CANNOT BE EMPTY");
            return false;

        } else if (p_charge.length()==0) {
            charge.requestFocus();
            charge.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if (p_duration.length()==0) {
            duration.requestFocus();
            duration.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else {
            return true;}
    }



    private void openFilechoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null&& data.getData() != null){
            uriImage =data.getData();
            image.setImageURI(uriImage);
        }
    }

    private void UploadPic() {
        if (uriImage != null){
            //Save the image with uid of the currently logged user
            String documentKey = UUID.randomUUID().toString();
            StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid()).child(documentKey + "." + getFileExtension(uriImage));

            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri != null){
                                PhotoUrl=uri.toString();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProductActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProductActivity.this,"Upload Successfully",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProductActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(EditProductActivity.this,"No file was selected",Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}