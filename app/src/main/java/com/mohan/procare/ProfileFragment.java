
package com.mohan.procare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mohan.procare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    TextView titlename,titleemail;
    CircleImageView image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        titlename=view.findViewById(R.id.titlename);
        titleemail=view.findViewById(R.id.titlemail);
        image=view.findViewById(R.id.profile_image);
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile");


        if (firebaseUser != null) {
            Uri photoUri = firebaseUser.getPhotoUrl();
            if (photoUri != null) {
                Picasso.with(getContext()).load(photoUri).into(image);
            } else {
                Picasso.with(getContext()).load(R.drawable.dprofile).into(image); //set default image
            }
        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }


//        if (firebaseUser != null) {
//            Uri photoUri = firebaseUser.getPhotoUrl();
//            if (photoUri != null) {
//                Picasso.with(getContext())
//                        .load(photoUri)
//                        .into(image);
//            } else{
//                Picasso.with(getContext())
//                        .load(R.drawable.electrician)  // Set default image
//                        .into(image);
//            }
//        }else{
//            startActivity(new Intent(getContext(), LoginActivity.class));
//        }



        showuserdata();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})

        CardView profile = view.findViewById(R.id.profiledetail);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),EditprofileActivity.class));
            }
        });



        Button logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }

    public void showuserdata() {
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseUser=auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            DocumentReference df = fstore.collection("Users").document(auth.getCurrentUser().getUid());
            df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null) {
                        titleemail.setText(value.getString("UserEmail"));
                        titlename.setText(value.getString("FullName"));
                    }
                }
            });

        } else {
            // Handle the case when the user is not authenticated
            // For example, you can redirect them to the login screen or display a message indicating that they need to log in
            startActivity(new Intent(getContext(),LoginActivity.class));
        }

    }


}


