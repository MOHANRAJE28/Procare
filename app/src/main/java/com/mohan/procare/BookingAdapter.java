package com.mohan.procare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohan.procare.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyviewHolder> {

    Context context;
    ArrayList<Booking> bookingArrayList;

    public BookingAdapter(Context context, ArrayList<Booking> bookingArrayList) {
        this.context = context;
        this.bookingArrayList = bookingArrayList;
    }

    @NonNull
    @Override
    public BookingAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemlist, parent, false);
        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.MyviewHolder holder, @SuppressLint("RecyclerView") int position) {

        Booking booking =bookingArrayList.get(position);
        holder.sname.setText(booking.ProviderName);
        holder.sphone.setText(booking.ProviderNumber);
        holder.semail.setText(booking.ProviderEmail);
//        holder.servicename.setText(booking.);

        if(booking.SProfile != null){
            Glide.with(context)
                    .load(booking.getSProfile())
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
                Intent intent =new Intent(context, BookingDetailActivity.class);
//                intent.putExtra("profile",requestArrayList.get(position).getProfilephoto());
                intent.putExtra("name",bookingArrayList.get(position).getProviderName());
                intent.putExtra("phone",bookingArrayList.get(position).getProviderNumber());
                intent.putExtra("email",bookingArrayList.get(position).getProviderEmail());
                intent.putExtra("date",bookingArrayList.get(position).getCustomerDate());
                intent.putExtra("time",bookingArrayList.get(position).getCustomerTime());
                intent.putExtra("description",bookingArrayList.get(position).getCustomerDescription());
                intent.putExtra("profile",bookingArrayList.get(position).getSProfile());
                intent.putExtra("approve",bookingArrayList.get(position).getRequest());
                context.startActivity(intent);
            }
        });






    }

    @Override
    public int getItemCount() {
        return bookingArrayList.size();

    }

    public static class MyviewHolder extends RecyclerView.ViewHolder {

        TextView sname, sphone, servicename, semail;
        CircleImageView profileimage;
        CardView cardview;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage=itemView.findViewById(R.id.profile_image);
            sname = itemView.findViewById(R.id.electriname);
            sphone = itemView.findViewById(R.id.electriph);
            servicename = itemView.findViewById(R.id.servicename);
            semail = itemView.findViewById(R.id.email);
            cardview =itemView.findViewById(R.id.cardviewelectri);
        }
    }


}
