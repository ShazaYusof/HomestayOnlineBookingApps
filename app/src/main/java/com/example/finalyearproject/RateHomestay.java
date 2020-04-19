package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.Adapter.ReviewAdapter;
import com.example.finalyearproject.List.ListBooking;
import com.example.finalyearproject.List.ListBookingHistory;
import com.example.finalyearproject.List.ListReview;
import com.example.finalyearproject.Model.Booking;
import com.example.finalyearproject.Model.RateProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.uncopt.android.widget.text.justify.JustifiedEditText;

public class RateHomestay extends AppCompatActivity {

    AppCompatRatingBar star;
    TextView totRate;
    Button buttonRate;
    String rateStar,homeID,homeName,reviewHome,rentalName;
    JustifiedEditText review;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_homestay);

        star = findViewById(R.id.RateStar);
        totRate = findViewById(R.id.totalRate);
        buttonRate = findViewById(R.id.btnRate);
        review = findViewById(R.id.tvReview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Intent intent = getIntent();

        homeID = getIntent().getStringExtra("HomestayID");
        homeName = getIntent().getStringExtra("HomestayName");
        rentalName = getIntent().getStringExtra("UserName");


        star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                totRate.setText("" + rating);
            }
        });


        buttonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {

                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Review").push();
                    RateProfile rate = new RateProfile(homeID, FirebaseAuth.getInstance().getCurrentUser().getUid(), reviewHome, rateStar, homeName, rentalName);

                    addRate(rate);
                }
            }
        });
    }

    private void addRate(RateProfile rate){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Review").push();

        String key = myref.getKey();
        rate.setRateID(key);

        //add post data to firebase database
        myref.setValue(rate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(RateHomestay.this,"Thank you !..Your rating are submitted", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(RateHomestay.this, ListBookingHistory.class));
            }
        });

    }
    private Boolean validate(){
        Boolean result=false;

        rateStar = totRate.getText().toString();
        reviewHome = review.getText().toString();

        if(rateStar.isEmpty() || reviewHome.isEmpty())
        {
            Toast.makeText(this,"Please fill up review and rates!!", Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return result;
    }
}
