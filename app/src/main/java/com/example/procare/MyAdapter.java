package com.example.procare;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import org.w3c.dom.Text;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Context;
import android.widget.Toast;

import com.bumptech.glide.Glide;


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
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileimage;
        TextView sname,sphone;
        CardView cardview;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage=itemView.findViewById(R.id.profile_image);
            sname=itemView.findViewById(R.id.electriname);
            sphone=itemView.findViewById(R.id.electriph);
            cardview =itemView.findViewById(R.id.cardviewelectri);
        }
    }

}
