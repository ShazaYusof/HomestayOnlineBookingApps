package com.example.finalyearproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.BookingHouseActivity;
import com.example.finalyearproject.InfoAppActivity;
import com.example.finalyearproject.Model.Booking;
import com.example.finalyearproject.Model.HomestayProfile;
import com.example.finalyearproject.Model.UserProfile;
import com.example.finalyearproject.R;

import java.util.ArrayList;

public class BookingHistoryAdapter extends  RecyclerView.Adapter<BookingHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<Booking> listBooking;
    //ArrayList<HomestayProfile> listHomestay;

    public BookingHistoryAdapter(ArrayList<Booking> listBooking) {
        //this.context = context;
        this.listBooking = listBooking;
    }


    @NonNull
    @Override
    public BookingHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_list, parent, false);

        context = parent.getContext();
        return new BookingHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistoryAdapter.MyViewHolder holder, final int position) {

        holder.homestayName.setText(listBooking.get(position).getHomestayName());
        holder.homestatus.setText(listBooking.get(position).getHomestayStatus());
        holder.txtIn.setText(listBooking.get(position).getCheckInDate());
        holder.txtOut.setText(listBooking.get(position).getCheckOutDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String homestayName = listBooking.get(position).getHomestayName();
                String userPhone = listBooking.get(position).getUserPhone();
                String inDate = listBooking.get(position).getCheckInDate();
                String outDate = listBooking.get(position).getCheckOutDate();
                String price = listBooking.get(position).getTotalPrice();
                String status = listBooking.get(position).getHomestayStatus();
                String days = listBooking.get(position).getTotalDays();
                String homestayID= listBooking.get(position).getHomestayID();
                String bookingID = listBooking.get(position).getBookingID();
                String userID = listBooking.get(position).getUserID();
                String userName = listBooking.get(position).getUserName();


                Intent intent = new Intent(context, InfoAppActivity.class);

                intent.putExtra("homestayName",homestayName);
                intent.putExtra("userPhone",userPhone);
                intent.putExtra("inDate",inDate);
                intent.putExtra("outDate",outDate);
                intent.putExtra("price",price);
                intent.putExtra("status",status);
                intent.putExtra("days",days);
                intent.putExtra("userID",userID);
                intent.putExtra("bookingID",bookingID);
                intent.putExtra("homestayID",homestayID);
                intent.putExtra("userName",userName);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listBooking.size();

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView homestayName, txtIn, txtOut,homestatus;

        //Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            homestayName = (TextView) itemView.findViewById(R.id.homestayName);
            txtIn = (TextView) itemView.findViewById(R.id.txtIn);
            txtOut = (TextView) itemView.findViewById(R.id.txtOut);
            homestatus = itemView.findViewById(R.id.status);
        }

    }
}
