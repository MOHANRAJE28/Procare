
package com.example.procare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class S_ProfileFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    TextView titlename,titleemail;
    ImageView img;
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s__profile, container, false);

        img=view.findViewById(R.id.image1);
        titlename=view.findViewById(R.id.titlename);
        titleemail=view.findViewById(R.id.titlemail);
        showuserdata();

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

    public void showuserdata(){
        auth =FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(auth.getCurrentUser().getUid());
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                titleemail.setText(value.getString("UserEmail"));

                titlename.setText(value.getString("FullName"));
            }
        });



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

