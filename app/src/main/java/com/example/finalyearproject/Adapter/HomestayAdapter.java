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
import com.example.finalyearproject.HomestayBookingActivity;
import com.example.finalyearproject.Model.HomestayProfile;
import com.example.finalyearproject.R;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.ArrayList;

public class HomestayAdapter extends RecyclerView.Adapter<HomestayAdapter.MyViewHolder> {

    Context context;
    ArrayList<HomestayProfile> listHome;




    public HomestayAdapter(ArrayList<HomestayProfile> listHome) {
        //this.context = context;
        this.listHome = listHome;
    }

    @NonNull
    @Override
    public HomestayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_homestay_list, parent, false);

        context = parent.getContext();
        return new HomestayAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomestayAdapter.MyViewHolder holder, final int position) {

        holder.homestayName.setText(listHome.get(position).getHomestayName());
        //holder.address.setText(listHome.get(position).getHomestayAddress());
        holder.price.setText("RM "+ listHome.get(position).getHomestayPrice()+" per night");
        //holder.bed.setText(listHome.get(position).getNumBed());
        //holder.toilet.setText(listHome.get(position).getNumToilet());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String homeName = listHome.get(position).getHomestayName();
                String homeAddress = listHome.get(position).getHomestayAddress();
                String homePrice = listHome.get(position).getHomestayPrice();
                String homeBed = listHome.get(position).getNumBed();
                String homeToilet = listHome.get(position).getNumToilet();
                String homestayID = listHome.get(position).getHomestayID();


                Intent intent = new Intent(context, HomestayBookingActivity.class);

                intent.putExtra("homeName",homeName );
                intent.putExtra("homeAddress",homeAddress);
                intent.putExtra("homePrice",homePrice);
                intent.putExtra("homeBed",homeBed);
                intent.putExtra("homeToilet",homeToilet);
                intent.putExtra("homestayID",homestayID);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listHome.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JustifiedTextView homestayName,address;
        TextView price,bed,toilet;


        //Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            homestayName = itemView.findViewById(R.id.propertyName);
            //address = itemView.findViewById(R.id.address);
            price = (TextView) itemView.findViewById(R.id.amount);
           // bed = (TextView) itemView.findViewById(R.id.bedCount);
            //toilet = (TextView) itemView.findViewById(R.id.toiletCount);

        }

    }
}
