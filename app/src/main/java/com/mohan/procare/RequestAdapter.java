package com.mohan.procare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mohan.procare.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyviewHolder> {
    Context context;
    ArrayList<Request> requestArrayList;



    public RequestAdapter(Context context, ArrayList<Request> requestArrayList) {
        this.context = context;
        this.requestArrayList = requestArrayList;

    }

    @NonNull
    @Override
    public RequestAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist, parent, false);
        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.MyviewHolder holder, @SuppressLint("RecyclerView") int position) {
        Request request = requestArrayList.get(position);
        holder.cname.setText(request.CustomerName);
        holder.cphone.setText(request.CustomerNumber);
        holder.cemail.setText(request.CustomerEmail);
        holder.servicename.setText(request.CustomerDescription);

        if(request.CProfile != null){
            Glide.with(context)
                    .load(request.getCProfile())
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
                Intent intent =new Intent(context, pendingrequestActivity2.class);
//                intent.putExtra("profile",requestArrayList.get(position).getProfilephoto());

                intent.putExtra("bkey",requestArrayList.get(position).getBKey());
                intent.putExtra("rkey",requestArrayList.get(position).getRKey());
                intent.putExtra("name",requestArrayList.get(position).getCustomerName());
                intent.putExtra("phone",requestArrayList.get(position).getCustomerNumber());
                intent.putExtra("email",requestArrayList.get(position).getCustomerEmail());
                intent.putExtra("date",requestArrayList.get(position).getCustomerDate());
                intent.putExtra("time",requestArrayList.get(position).getCustomerTime());
                intent.putExtra("description",requestArrayList.get(position).getCustomerDescription());
                intent.putExtra("clongitude",requestArrayList.get(position).getClongitude());
                intent.putExtra("clatitude",requestArrayList.get(position).getClatitude());
                intent.putExtra("profile",requestArrayList.get(position).getCProfile());
                intent.putExtra("servicetype",requestArrayList.get(position).getServiceType());
//                Toast.makeText(v.getContext(), documentIds,Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }
    public static class MyviewHolder extends RecyclerView.ViewHolder {

        TextView cname, cphone, servicename, cemail;
        CardView cardview;
        CircleImageView profileimage;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            cname = itemView.findViewById(R.id.electriname);
            cphone = itemView.findViewById(R.id.electriph);
            servicename = itemView.findViewById(R.id.servicename);
            cemail = itemView.findViewById(R.id.email);
            cardview =itemView.findViewById(R.id.cardviewelectri);
            profileimage=itemView.findViewById(R.id.profile_image);
        }
    }
}
