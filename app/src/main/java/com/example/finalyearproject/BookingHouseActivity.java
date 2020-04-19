package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.List.ListBooking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingHouseActivity extends AppCompatActivity {

    TextView from,to,rentalName,rentalPhone,daysPrice,totDays;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    String rentName,rentPhone,inDate,outDate,price,Bstatus,totalDays,rentEmail,homestayName,homeID,rentID,bookingID;
    Spinner bookStatus;
    Button buttonOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_house);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        from = findViewById(R.id.tvIn);
        to = findViewById(R.id.tvOut);
        rentalName = findViewById(R.id.tvRentalName);
        rentalPhone = findViewById(R.id.tvRentalPhone);
        daysPrice = findViewById(R.id.tvTotPrice);
        bookStatus = findViewById(R.id.spinnerStatus);
        totDays = findViewById(R.id.tvDays);
        buttonOk = findViewById(R.id.btnOk);


        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.status,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookStatus.setAdapter(adapter);*/
       // bookStatus.setOnItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        List<String> listStatus = new ArrayList<>();
        listStatus.add("Accepted");
        listStatus.add("Rejected");

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStatus);

        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bookStatus.setAdapter(adapterStatus);

        final Intent intent = getIntent();

        final String rentalID = intent.getStringExtra("userID");
       final String bookID = intent.getStringExtra("bookingID");
       String houseID = intent.getStringExtra("homeID");

        rentID = rentalID;
        homeID = houseID;
        bookingID = bookID;

        final DatabaseReference myRef = firebaseDatabase.getReference("Booking").child(bookID);


        rentName = intent.getStringExtra("rentName");
        rentPhone = intent.getStringExtra("rentPhone");
        inDate =intent.getStringExtra("inDate");
        outDate = intent.getStringExtra("outDate");
        price = intent.getStringExtra("price");
        Bstatus = intent.getStringExtra("status");
        totalDays = intent.getStringExtra("days");


        rentalName.setText(rentName);
        rentalPhone.setText(rentPhone);
        from.setText(inDate);
        to.setText(outDate);
        daysPrice.setText(price);
        totDays.setText(totalDays);
       // bookStatus.setText(Bstatus);

        rentName = rentalName.getText().toString();
        rentPhone = rentalPhone.getText().toString();
        inDate = from.getText().toString();
        outDate = to.getText().toString();
        totalDays = totDays.getText().toString();
        price = daysPrice.getText().toString();



        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               final String status = bookStatus.getSelectedItem().toString();



                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("homestayStatus").setValue(status);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Toast.makeText(BookingHouseActivity.this,"Thank you for your homestay confirmation!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(BookingHouseActivity.this, ListBooking.class));

            }
        });


       // DatabaseReference databaseReferenceBooking = firebaseDatabase.getReference("Booking").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //DatabaseReference databaseReferenceUser = firebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


       /* databaseReferenceBooking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Booking booking = dataSnapshot.getValue(Booking.class);
                 from.setText( "Check In Date: " + booking.getIn());
                to.setText("Check Out Date: " + booking.getOut());
                daysPrice.setText( booking.getTotPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookingHouseActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });*/


    }

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
