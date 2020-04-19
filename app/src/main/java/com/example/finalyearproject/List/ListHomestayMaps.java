package com.example.finalyearproject.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalyearproject.Adapter.HomestayAdapter;
import com.example.finalyearproject.Model.HomestayProfile;
import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class ListHomestayMaps extends AppCompatActivity {

    RecyclerView RecyclerHome;
    ArrayList<HomestayProfile> homestay;
    HomestayAdapter adapter;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;

    //String homeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_homestay_maps);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerHome = findViewById(R.id.RecyclerHome);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        RecyclerHome.setHasFixedSize(true);
        RecyclerHome.setLayoutManager( new LinearLayoutManager(this));

        //homeID = getIntent().getStringExtra("HomestayID");

        dbRef = FirebaseDatabase.getInstance().getReference().child("Homestay");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                homestay= new ArrayList<HomestayProfile>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    HomestayProfile p = dataSnapshot1.getValue(HomestayProfile.class);
                    homestay.add(p);
                }
                adapter = new HomestayAdapter(homestay);
                RecyclerHome.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListHomestayMaps.this, "There is an error occur. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
