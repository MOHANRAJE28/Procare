package com.example.procare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class EletricbookActivity extends AppCompatActivity {

    CircleImageView profileimg;
    TextView sname,servicename;
    ImageView sphone,smessage;
    Button request;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eletricbook);
        getSupportActionBar().hide();
        profileimg =findViewById(R.id.profile_image);
        sname=findViewById(R.id.sname);
        servicename=findViewById(R.id.service);
        sphone =findViewById(R.id.phone);
        smessage=findViewById(R.id.message);
        request=findViewById(R.id.request);


        Intent intent =getIntent();
        String name=intent.getExtras().getString("name");
        String phone=intent.getExtras().getString("phone");
        String profile=intent.getExtras().getString("profile");
        String service=intent.getExtras().getString("servicename");
        String email=intent.getExtras().getString("email");


        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(EletricbookActivity.this,RequestsActivity.class);
                intent.putExtra("profile",phone);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("email",email);
                intent.putExtra("servicename",service);
                v.getContext().startActivity(intent);
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

        Glide.with(this).load(profile).apply(RequestOptions.centerCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        profileimg.setBackground(resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Do nothing
                    }
                });

    }

}
