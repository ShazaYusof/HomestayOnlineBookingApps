package com.example.finalyearproject.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.Adapter.ReviewAdapter;
import com.example.finalyearproject.Model.RateProfile;
import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListReview extends AppCompatActivity {

    RecyclerView RecyclerStar;
    ArrayList<RateProfile> review;
    ReviewAdapter adapter;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;

    TextView txtMoment;
    ImageView man;

    String homeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_review);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerStar = findViewById(R.id.RecyclerReview);
        progressBar = findViewById(R.id.progressBar);
        man = findViewById(R.id.sadMan);
        txtMoment = findViewById(R.id.moment);

        man.setVisibility(View.VISIBLE);
        txtMoment.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

        RecyclerStar.setHasFixedSize(true);
        RecyclerStar.setLayoutManager( new LinearLayoutManager(this));

        homeID = getIntent().getStringExtra("homestayID");

        dbRef = FirebaseDatabase.getInstance().getReference().child("Review");
        final Query queryRef = dbRef.orderByChild("homestayID").equalTo(homeID);

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                review = new ArrayList<RateProfile>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    RateProfile p = dataSnapshot1.getValue(RateProfile.class);
                    review.add(p);
                    man.setVisibility(View.GONE);
                    txtMoment.setVisibility(View.GONE);

                }
                adapter = new ReviewAdapter(review);
                RecyclerStar.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListReview.this, "There is an error occur. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
