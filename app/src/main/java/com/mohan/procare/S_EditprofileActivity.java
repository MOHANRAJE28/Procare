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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mohan.procare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class S_EditprofileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    FirebaseFirestore fstore;
    EditText phone,name,email;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    //    ImageView image;
    CircleImageView image;
    Button upload;
    private  String PhotoUrl;
    private Uri uriImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seditprofile);
        phone=findViewById(R.id.phone);
        progressBar=findViewById(R.id.progressbar);
        name=findViewById(R.id.name);
        email=findViewById(R.id.mail);
        image=findViewById(R.id.profile_image);
        upload=findViewById(R.id.upload);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile");






        if (firebaseUser != null) {
            Uri photoUri = firebaseUser.getPhotoUrl();
            if (photoUri != null) {
                Picasso.with(S_EditprofileActivity.this).load(photoUri).into(image);
            } else {
                Picasso.with(S_EditprofileActivity.this).load(R.drawable.dprofile).into(image); //set default image
            }
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        //set the user's current qualification
        //regular URIs.

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



        if (auth.getCurrentUser() != null) {
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(auth.getCurrentUser().getUid());
            df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    email.setText(value.getString("UserEmail"));
                    name.setText(value.getString("FullName"));
                    phone.setText(value.getString("PhoneNumber"));
                }
            });
        }else {
            // Handle the case when the user is not authenticated
            // For example, you can redirect them to the login screen or display a message indicating that they need to log in
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

    }

    public void save(View view) {
        String u_name = name.getText().toString();
        String u_phone = phone.getText().toString();
        boolean check = validateinfo(u_name, u_phone);
        if (check) {
            FirebaseUser user = auth.getCurrentUser();
            DocumentReference df = fstore.collection("Users").document(user.getUid());
            Map<String, Object> userinfo = new HashMap<>();
            userinfo.put("Profilephoto",PhotoUrl);
            userinfo.put("PhoneNumber",u_phone);
            userinfo.put("FullName", u_name);
            df.update(userinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(S_EditprofileActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(S_EditprofileActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateinfo(String u_name, String u_phone) {

        if(u_name.length()==0) {
            name.requestFocus();
            name.setError("FIELD CANNOT BE EMPTY");
            return false;

        } else if (u_phone.length()==0) {
            phone.requestFocus();
            phone.setError("FIELD CANNOT BE EMPTY");
            return false;
        }else if (u_phone.length()!=10) {
            phone.requestFocus();
            phone.setError("PHONE SHOULD BE 10+");
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
            StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid()+"."
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
                                userinfo.put("Profilephoto",PhotoUrl);
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
                            Toast.makeText(S_EditprofileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(S_EditprofileActivity.this,"Upload Successfully",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(S_EditprofileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(S_EditprofileActivity.this,"No file was selected",Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}







