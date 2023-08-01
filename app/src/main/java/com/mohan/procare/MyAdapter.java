package com.mohan.procare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Context;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mohan.procare.R;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyviewHolder> {

    Context context;
    ArrayList<User> userArrayList;

    public MyAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemlist,parent,false);
        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyviewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = userArrayList.get(position);
        holder.sname.setText(user.FullName);
        holder.sphone.setText(user.PhoneNumber);
        holder.servicename.setText(user.Qualification);
        holder.email.setText(user.UserEmail);
//        holder.profileimage.setImageURI(Uri.parse(user.Profilephoto));
        if(user.Profilephoto != null){
         Glide.with(context)
                .load(user.getProfilephoto())
                .into(holder.profileimage);}
        else {
            Glide.with(context)
                    .load(R.drawable.dprofile)
                    .into(holder.profileimage);

        }

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"card"+position,Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(context,EletricbookActivity.class);
                intent.putExtra("profile",userArrayList.get(position).getProfilephoto());
                intent.putExtra("name",userArrayList.get(position).getFullName());
                intent.putExtra("phone",userArrayList.get(position).getPhoneNumber());
                intent.putExtra("email",userArrayList.get(position).getUserEmail());
                intent.putExtra("servicename",userArrayList.get(position).getQualification());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileimage;
        TextView sname,sphone,servicename,email;
        CardView cardview;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage=itemView.findViewById(R.id.profile_image);
            sname=itemView.findViewById(R.id.electriname);
            sphone=itemView.findViewById(R.id.electriph);
            email=itemView.findViewById(R.id.email);
            servicename=itemView.findViewById(R.id.servicename);
            cardview =itemView.findViewById(R.id.cardviewelectri);

        }
    }

}
