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
import com.example.finalyearproject.Adapter.BookingHistoryAdapter;
import com.example.finalyearproject.Adapter.HomestayAdapter;
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

public class ListBookingHistory extends AppCompatActivity {

    RecyclerView RecyclerBooking;
    TextView txtMoment;
    ImageView man;
    ArrayList<Booking> booking;
//    ArrayList<UserProfile> user;
//    ArrayList<HomestayProfile> homestay;
    BookingHistoryAdapter adapter;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef,myRef,userRef;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_booking_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerBooking = findViewById(R.id.RecyclerBooking);
        progressBar = findViewById(R.id.progressBar);
        man = findViewById(R.id.sadMan);
        txtMoment = findViewById(R.id.moment);

        mAuth = FirebaseAuth.getInstance();

        RecyclerBooking.setHasFixedSize(true);
        RecyclerBooking.setLayoutManager( new LinearLayoutManager(this));

        man.setVisibility(View.VISIBLE);
        txtMoment.setVisibility(View.VISIBLE);

        userID = getIntent().getStringExtra("userID");

        dbRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        final Query queryRef = FirebaseDatabase.getInstance().getReference("Booking").orderByChild("userID").equalTo(mAuth.getUid());

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
                adapter = new BookingHistoryAdapter(booking);
                RecyclerBooking.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListBookingHistory.this, "There is an error occur. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

//
//        userRef = FirebaseDatabase.getInstance().getReference().child("User");
//        final Query queryUser = FirebaseDatabase.getInstance().getReference("Homestay").orderByChild("homeName").equalTo(mAuth.getUid());
//
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                user = new ArrayList<UserProfile>();
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
//                {
//
//                    UserProfile u = dataSnapshot1.getValue(UserProfile.class);
//                    user.add(u);
//
//                }
//                adapter = new BookingHistoryAdapter(booking,homestay,user);
//                RecyclerBooking.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(ListBookingHistory.this, "There is an error occur. Please try again.", Toast.LENGTH_LONG).show();
//            }
//        });


    }
}
