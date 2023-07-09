package com.example.procare;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Context;


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
    public void onBindViewHolder(@NonNull MyAdapter.MyviewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.sname.setText(user.FullName);
        holder.sphone.setText(user.PhoneNumber);
//        holder.profileimage.setImageURI(Uri.parse(user.Profilephoto));
        holder.profileimage.setImageURI(Uri.parse(user.Profilephoto));
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileimage;
        TextView sname,sphone;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage=itemView.findViewById(R.id.profile_image);
            sname=itemView.findViewById(R.id.electriname);
            sphone=itemView.findViewById(R.id.electriph);

        }
    }

}
