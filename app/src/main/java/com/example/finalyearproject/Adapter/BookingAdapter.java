package com.example.finalyearproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.Model.Booking;
import com.example.finalyearproject.BookingHouseActivity;
import com.example.finalyearproject.Model.HomestayProfile;
import com.example.finalyearproject.Model.UserProfile;
import com.example.finalyearproject.R;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    Context context;
    ArrayList<Booking> listBooking;

    public BookingAdapter(ArrayList<Booking> listBooking) {
        this.listBooking = listBooking;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list, parent, false);

        context = parent.getContext();
        return new BookingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.rentalName.setText(listBooking.get(position).getUserName());
        holder.homestatus.setText(listBooking.get(position).getHomestayStatus());
        holder.txtIn.setText(listBooking.get(position).getCheckInDate());
        holder.txtOut.setText(listBooking.get(position).getCheckOutDate());
        holder.price.setText("RM " + listBooking.get(position).getTotalPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rentName = listBooking.get(position).getUserName();
                String rentPhone = listBooking.get(position).getUserPhone();
                String inDate = listBooking.get(position).getCheckInDate();
                String outDate = listBooking.get(position).getCheckOutDate();
                String price = listBooking.get(position).getTotalPrice();
                String status = listBooking.get(position).getHomestayStatus();
                String days = listBooking.get(position).getTotalDays();
                String hostID= listBooking.get(position).getHomestayID();
                String userID = listBooking.get(position).getUserID();
                String bookingID = listBooking.get(position).getBookingID();


                Intent intent = new Intent(context, BookingHouseActivity.class);

                intent.putExtra("rentName",rentName);
                intent.putExtra("rentPhone",rentPhone);
                intent.putExtra("inDate",inDate);
                intent.putExtra("outDate",outDate);
                intent.putExtra("price",price);
                intent.putExtra("status",status);
                intent.putExtra("days",days);
                intent.putExtra("hostID",hostID);
                intent.putExtra("userID",userID);
                intent.putExtra("bookingID",bookingID);

                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount()
    {
        return listBooking.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rentalName, txtIn, txtOut,homestatus,price;

        //Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            rentalName = (TextView) itemView.findViewById(R.id.rentalName);
            txtIn = (TextView) itemView.findViewById(R.id.txtIn);
            txtOut = (TextView) itemView.findViewById(R.id.txtOut);
            homestatus = itemView.findViewById(R.id.status);
            price = itemView.findViewById(R.id.price);
        }

    }
}
