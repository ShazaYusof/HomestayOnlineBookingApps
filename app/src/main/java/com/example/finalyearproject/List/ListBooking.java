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

import com.example.finalyearproject.Adapter.BookingAdapter;
import com.example.finalyearproject.Model.Booking;
import com.example.finalyearproject.Model.HomestayProfile;
import com.example.finalyearproject.Model.UserProfile;
import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListBooking extends AppCompatActivity {

    RecyclerView RecyclerBooking;
    ArrayList<Booking> booking;
    ArrayList<HomestayProfile> homestay;
    ArrayList<UserProfile> user;
    BookingAdapter adapter;
    TextView txtMoment;
    ImageView man;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_booking);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerBooking = findViewById(R.id.RecyclerBook);
        progressBar = findViewById(R.id.progressBar);
        man = findViewById(R.id.sadMan);
        txtMoment = findViewById(R.id.moment);

        man.setVisibility(View.VISIBLE);
        txtMoment.setVisibility(View.VISIBLE);

        RecyclerBooking.setHasFixedSize(true);
        RecyclerBooking.setLayoutManager( new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        dbRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        final Query queryRef = FirebaseDatabase.getInstance().getReference("Booking").orderByChild("homestayID").equalTo(mAuth.getUid());

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                booking = new ArrayList<Booking>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Booking p = dataSnapshot1.getValue(Booking.class);
                    booking.add(p);
                    man.setVisibility(View.GONE);
                    txtMoment.setVisibility(View.GONE);

                }
                adapter = new BookingAdapter(booking);
                RecyclerBooking.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListBooking.this, "There is an error occur. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
