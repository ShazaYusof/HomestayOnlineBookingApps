package com.example.finalyearproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.Model.RateProfile;
import com.example.finalyearproject.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {


    Context context;
    ArrayList<RateProfile> listRate;

    public ReviewAdapter(ArrayList<RateProfile> listRate) {
        //this.context = context;
        this.listRate = listRate;
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list, parent, false);

        context = parent.getContext();
        return new ReviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, final int position) {

        holder.rentalName.setText(listRate.get(position).getUserName());
        holder.homeRate.setText(listRate.get(position).getStarHomestay() + " / 5.0");
        holder.review.setText(listRate.get(position).getReviewHomestay());


    }

    @Override
    public int getItemCount() {
        return listRate.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rentalName, homeRate,review;

        //Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            rentalName = (TextView) itemView.findViewById(R.id.giveName);
            homeRate = (TextView) itemView.findViewById(R.id.totalStar);
            review = (TextView) itemView.findViewById(R.id.review);
        }

    }
}
