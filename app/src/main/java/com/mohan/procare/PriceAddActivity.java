package com.mohan.procare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PriceAddActivity extends AppCompatActivity {

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
    Button upload;
    private  String PhotoUrl;
    private Uri uriImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_add);

        productname=findViewById(R.id.productname);
        charge=findViewById(R.id.charge);
        image=findViewById(R.id.profile_image);
        upload=findViewById(R.id.upload);
        delete=findViewById(R.id.delete);
        edit=findViewById(R.id.edit);
        duration =findViewById(R.id.duration);

        progressBar=findViewById(R.id.progressbar);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("product");


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







    }

public void add(View view) {
    String p_name = productname.getText().toString();
    String p_charge = charge.getText().toString();
    String p_duration  = duration.getText().toString();
    boolean check = validateinfo(p_name, p_charge,p_duration);
    if (check) {
        FirebaseUser user = auth.getCurrentUser();
        DocumentReference userDocRef = fstore.collection("Users").document(user.getUid());
        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists() && documentSnapshot.contains("serviceprovider")) {
                            // The user has a field named "ServiceProvider"
                            CollectionReference productCollectionRef = userDocRef.collection("product");
                            Map<String, Object> productInfo = new HashMap<>();
                            productInfo.put("productName", p_name);
                            productInfo.put("productCharge", p_charge);
                            productInfo.put("duration",p_duration);
                            productInfo.put("Productphoto",PhotoUrl);
                            productCollectionRef.add(productInfo)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(PriceAddActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PriceAddActivity.this, "Failed to create product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // The user does not have a field named "ServiceProvider"
                            Toast.makeText(PriceAddActivity.this, "User is not a service provider", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PriceAddActivity.this, "Failed to fetch user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
        } else if (p_duration.length()==0) {
            charge.requestFocus();
            charge.setError("FIELD CANNOT BE EMPTY");
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
            StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid()).child(documentKey+"."
                    +getFileExtension(uriImage));
            //Upload image to Storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri != null){
                                PhotoUrl=uri.toString();
                                FirebaseUser user = auth.getCurrentUser();
                                DocumentReference df = fstore.collection("Users").document(user.getUid());
                                Map<String, Object> userinfo = new HashMap<>();
                                userinfo.put("Productphoto",PhotoUrl);
                                df.update(userinfo);
                            }

                            Uri downloadUri = uri;
//                            //finally set the display image of  the user after upload
                            UserProfileChangeRequest profileupdate =new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileupdate);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PriceAddActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PriceAddActivity.this,"Upload Successfully",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PriceAddActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(PriceAddActivity.this,"No file was selected",Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


}

//
//
//package com.example.procare;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.webkit.MimeTypeMap;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserProfileChangeRequest;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class PriceAddActivity extends AppCompatActivity {
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//    FirebaseAuth auth;
//    private FirebaseUser firebaseUser;
//    FirebaseFirestore fstore;
//    EditText charge,productname,duration;
//    private ProgressBar progressBar;
//    private StorageReference storageReference;
//
//    //    ImageView image;
//    CircleImageView image;
//
//    ImageView delete,edit;
//    Button upload;
//    private  String PhotoUrl;
//    private Uri uriImage;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_price_add);
//
//        productname=findViewById(R.id.productname);
//        charge=findViewById(R.id.charge);
//        image=findViewById(R.id.profile_image);
//        upload=findViewById(R.id.upload);
//        delete=findViewById(R.id.delete);
//        edit=findViewById(R.id.edit);
//        duration =findViewById(R.id.duration);
//
//        progressBar=findViewById(R.id.progressbar);
//
//        auth = FirebaseAuth.getInstance();
//        fstore = FirebaseFirestore.getInstance();
//        firebaseUser = auth.getCurrentUser();
//        storageReference = FirebaseStorage.getInstance().getReference("product");
//
//
//        //selecting the image from the storage
//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                openFilechoser();
//            }
//        });
//        // uploading the image to the storage
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                UploadPic();
//            }
//        });
//
//
//
//
//
//
//
//    }
////
////    public void add(View view) {
////        String p_name = productname.getText().toString();
////        String p_charge = charge.getText().toString();
////        String p_duration  = duration.getText().toString();
////        boolean check = validateinfo(p_name, p_charge,p_duration);
////        if (check) {
////            FirebaseUser user = auth.getCurrentUser();
////            if(user != null) {
////                DocumentReference userDocRef = fstore.collection("Users").document(user.getUid());
////                userDocRef.get()
////                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
////                            @Override
////                            public void onSuccess(DocumentSnapshot documentSnapshot) {
////                                if (documentSnapshot.exists() && documentSnapshot.contains("serviceprovider")) {
////                                    if (userDocRef != null) {
////                                        // The user has a field named "ServiceProvider"
////                                        CollectionReference productCollectionRef = userDocRef.collection("product");
////                                        Map<String, Object> productInfo = new HashMap<>();
////                                        productInfo.put("productName", p_name);
////                                        productInfo.put("productCharge", p_charge);
////                                        productInfo.put("duration", p_duration);
////                                        productInfo.put("Productphoto", PhotoUrl);
////                                        productCollectionRef.add(productInfo)
////                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
////                                                    @Override
////                                                    public void onSuccess(DocumentReference documentReference) {
////                                                        Toast.makeText(PriceAddActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
////                                                    }
////                                                })
////                                                .addOnFailureListener(new OnFailureListener() {
////                                                    @Override
////                                                    public void onFailure(@NonNull Exception e) {
////                                                        Toast.makeText(PriceAddActivity.this, "Failed to create product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                                                    }
////                                                });
////                                    } else {
////                                        // The user does not have a field named "ServiceProvider"
////                                        Toast.makeText(PriceAddActivity.this, "User is not a service provider", Toast.LENGTH_SHORT).show();
////                                    }
////                                }else {
////                                    Toast.makeText(PriceAddActivity.this, "Userdoc is null", Toast.LENGTH_SHORT).show();
////                                }
////                            }
////                        })
////                        .addOnFailureListener(new OnFailureListener() {
////                            @Override
////                            public void onFailure(@NonNull Exception e) {
////                                Toast.makeText(PriceAddActivity.this, "Failed to fetch user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                            }
////                        });
////            }else {
////
////                Toast.makeText(PriceAddActivity.this, "User is not logged in", Toast.LENGTH_SHORT).show();
////            }
////
////        }
////    }
//
//    public void add(View view) {
//        String p_name = productname.getText().toString();
//        String p_charge = charge.getText().toString();
//        String p_duration = duration.getText().toString();
//        boolean check = validateinfo(p_name, p_charge, p_duration);
//
//        if (check) {
//            FirebaseUser user = auth.getCurrentUser();
//            if (user != null) {
//                DocumentReference productRef = fstore.collection("Users")
//                        .document(user.getUid())
//                        .collection("product")
//                        .document(); // Generate a unique document ID
//
//                String documentId = productRef.getId(); // Get the generated document ID
//
//                Map<String, Object> productData = new HashMap<>();
//                productData.put("productName", p_name);
//                productData.put("productCharge", p_charge);
//                productData.put("duration", p_duration);
//
//                productRef.set(productData);
//
//                // Upload the profile photo if it exists
////                if (uriImage != null) {
////                    // Save the image with a unique filename
////                    String photoFileName = documentId + "." + getFileExtension(uriImage);
////                    StorageReference fileReference = storageReference.child(photoFileName);
////                    // Upload the image to Firebase Storage
////                    fileReference.putFile(uriImage)
////                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////                                @Override
////                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                                    // Get the download URL of the uploaded image
////                                    fileReference.getDownloadUrl()
////                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
////                                                @Override
////                                                public void onSuccess(Uri uri) {
////                                                    String photoUrl = uri.toString();
////                                                    productData.put("Productphoto", photoUrl);
////
////                                                    // Add the product data to Firestore
////                                                    productRef.set(productData)
////                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                                                @Override
////                                                                public void onSuccess(Void aVoid) {
////                                                                    Toast.makeText(PriceAddActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
////                                                                    startActivity(new Intent(PriceAddActivity.this, S_PriceAddFragment.class));
////                                                                    finish();
////                                                                }
////                                                            })
////                                                            .addOnFailureListener(new OnFailureListener() {
////                                                                @Override
////                                                                public void onFailure(@NonNull Exception e) {
////                                                                    Toast.makeText(PriceAddActivity.this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                                                                }
////                                                            });
////                                                }
////                                            })
////                                            .addOnFailureListener(new OnFailureListener() {
////                                                @Override
////                                                public void onFailure(@NonNull Exception e) {
////                                                    Toast.makeText(PriceAddActivity.this, "Failed to upload profile photo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                                                }
////                                            });
////                                }
////                            })
////                            .addOnFailureListener(new OnFailureListener() {
////                                @Override
////                                public void onFailure(@NonNull Exception e) {
////                                    Toast.makeText(PriceAddActivity.this, "Failed to upload profile photo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                                }
////                            });
////                } else {
////                    // No profile photo, directly add the product data to Firestore
////                    productRef.set(productData)
////                            .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                @Override
////                                public void onSuccess(Void aVoid) {
////                                    Toast.makeText(PriceAddActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
////                                    startActivity(new Intent(PriceAddActivity.this, S_PriceAddFragment.class));
////                                    finish();
////                                }
////                            })
////                            .addOnFailureListener(new OnFailureListener() {
////                                @Override
////                                public void onFailure(@NonNull Exception e) {
////                                    Toast.makeText(PriceAddActivity.this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                                }
////                            });
//                }
//            }
//
//    }
//
//    private boolean validateinfo(String p_name, String p_charge,String p_duration) {
//
//        if(p_name.length()==0) {
//            productname.requestFocus();
//            productname.setError("FIELD CANNOT BE EMPTY");
//            return false;
//
//        } else if (p_charge.length()==0) {
//            charge.requestFocus();
//            charge.setError("FIELD CANNOT BE EMPTY");
//            return false;
//        } else if (p_duration.length()==0) {
//            charge.requestFocus();
//            charge.setError("FIELD CANNOT BE EMPTY");
//            return false;
//        }
//        else {
//            return true;}
//    }
//
//    private void openFilechoser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,PICK_IMAGE_REQUEST);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null&& data.getData() != null){
//            uriImage =data.getData();
//            image.setImageURI(uriImage);
//        }
//    }
//
//    private void UploadPic() {
//        if (uriImage != null){
//            //Save the image with uid of the currently logged user
//            String documentKey = UUID.randomUUID().toString();
//            StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid()).child(documentKey+"."
//                    +getFileExtension(uriImage));
//            //Upload image to Storage
//            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            if(uri != null){
//                                PhotoUrl=uri.toString();
//                                FirebaseUser user = auth.getCurrentUser();
//                                DocumentReference df = fstore.collection("Users").document(user.getUid());
//                                Map<String, Object> userinfo = new HashMap<>();
//                                userinfo.put("Productphoto",PhotoUrl);
//                                df.update(userinfo);
//                            }
//
//                            Uri downloadUri = uri;
////                            //finally set the display image of  the user after upload
//                            UserProfileChangeRequest profileupdate =new UserProfileChangeRequest.Builder()
//                                    .setPhotoUri(downloadUri).build();
//                            firebaseUser.updateProfile(profileupdate);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(PriceAddActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(PriceAddActivity.this,"Upload Successfully",Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(PriceAddActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }else {
//            progressBar.setVisibility(View.GONE);
//            Toast.makeText(PriceAddActivity.this,"No file was selected",Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private String getFileExtension(Uri uri){
//        ContentResolver cr = getContentResolver();
//        MimeTypeMap mime =MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cr.getType(uri));
//    }
//
