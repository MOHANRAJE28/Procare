package com.example.procare;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class QualificationActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    FirebaseFirestore fstore;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST =1;
    private Uri uriImage;
    EditText qualification,experience;
    ImageView imageViewupload;
    Button   choose,upload;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification);
//      getSupportActionBar().setTitle("Upload Profile Picture");
        choose = findViewById(R.id.chooseimage);
        upload = findViewById(R.id.uploadimage);
        imageViewupload=findViewById(R.id.qulifi_image);
        progressBar=findViewById(R.id.progressbar);
        qualification=findViewById(R.id.qualification);
        experience=findViewById(R.id.exp);
        fstore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("qulification");

        Uri uri =firebaseUser.getPhotoUrl();
        //set the user's current qualification
        //regular URIs.
        Picasso.with(QualificationActivity.this).load(uri).into(imageViewupload);

        //Choosing the  Image from the phone
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilechoser();
            }
        });

        //upload the image into the firebase
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });


    }
    public void next(View view){

        String providerqualification = qualification.getText().toString();
        String providerexperience =experience.getText().toString();

        //validation
        boolean check = validateinfo(providerqualification, providerexperience);

        if(check == true){
            FirebaseUser user=auth.getCurrentUser();
            DocumentReference df = fstore.collection("Users").document(user.getUid());
            Map<String, Object> userinfo = new HashMap<>();
            //pushing the data into the firebase
            userinfo.put("Qualification",providerqualification);
            userinfo.put("Experience",providerexperience);
            df.update(userinfo);
            Toast.makeText(QualificationActivity.this, "completed the qualification ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(QualificationActivity.this, S_AddressActivity.class));
            finish();
            }
    }

    private boolean validateinfo(String providerqualification, String providerexperience) {

        if(providerqualification.length()==0) {
            qualification.requestFocus();
            qualification.setError("FIELD CANNOT BE EMPTY");
            return false;

        }  else if (!providerqualification.matches("[a-zA-Z]+")) {
            qualification.requestFocus();
            qualification.setError("ENTER ONLY ALPHABETICAL CHARACTER");
            return false;
        }
        else if (providerexperience.length()==0){
            experience.requestFocus();
            experience.setError("FIELD CANNOT BE EMPTY");
            return false;
        }
        else if (!providerexperience.matches("[0-9]+")){
            experience.requestFocus();
            experience.setError("ENTER ONLY THE NUMBER");
            return false;
        }else {
            return true;
        }

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
            imageViewupload.setImageURI(uriImage);
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
//                            Uri downloadUri =uri;
//                            firebaseUser = auth.getCurrentUser();
//                            //finally set the display image of  the user after upload
//                            UserProfileChangeRequest profileupdate =new UserProfileChangeRequest.Builder()
//                                    .setPhotoUri(downloadUri).build();
//                            firebaseUser.updateProfile(profileupdate);

                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(QualificationActivity.this,"Upload Successfully",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(QualificationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(QualificationActivity.this,"No file was selected",Toast.LENGTH_SHORT).show();
        }
    }
    //Obtain File Extension of the image
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}