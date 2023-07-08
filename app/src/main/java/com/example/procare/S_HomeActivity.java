package com.example.procare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.procare.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class S_HomeActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shome);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new S_HomeFragment());
        binding.bottomNavigationview.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.home) {

                replaceFragment(new S_HomeFragment());
            }
            else if (id ==  R.id.books) {
                replaceFragment(new S_BookFragment());
            } else if (id == R.id.profile) {

                replaceFragment(new S_ProfileFragment());
            }


            return true;
        });

    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();

    }



    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(S_HomeActivity.this,LoginActivity.class));
        finish();

    }


}