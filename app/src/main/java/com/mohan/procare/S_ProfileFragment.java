
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
import android.widget.ImageView;
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

public class S_ProfileFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    private FirebaseUser firebaseUser;
    TextView titlename,titleemail;
    ImageView image;
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s__profile, container, false);

        image=view.findViewById(R.id.profile_image);
        titlename=view.findViewById(R.id.titlename);
        titleemail=view.findViewById(R.id.titlemail);
        Button logout = view.findViewById(R.id.logout);
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


        showuserdata();
        CardView profile = view.findViewById(R.id.sprofiledetail);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),EditprofileActivity.class));
            }
        });


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

    public void showuserdata(){
        auth =FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
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







//package com.example.procare;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//
//public class S_ProfileFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public S_ProfileFragment() {
//        // Required empty public constructor
//    }
//
//    // TODO: Rename and change types and number of parameters
//    public static S_ProfileFragment newInstance(String param1, String param2) {
//        S_ProfileFragment fragment = new S_ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.fragment_s__profile, container, false);
//
//        View view=inflater.inflate(R.layout.fragment_s__profile,container,false);
//
//        Button logout =view.findViewById(R.id.logout);
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(getActivity(),LoginActivity.class));
//                finish();
//            }
//        });
//
//
//
//
//
//
//
//        return view;
//    }
//
//
//
//
//}

