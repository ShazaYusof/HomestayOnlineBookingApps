package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.List.ListBookingHistory;
import com.example.finalyearproject.Model.Booking;
import com.example.finalyearproject.Model.HomestayProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InfoAppActivity extends AppCompatActivity{

    TextView homeName,in,out,totPrice,status,totDays,homeID,userName;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    Button rate;
    String homeid,homename,userID,bookingID,inDate,outDate,totalPrice,Bstatus,totalDays,hostID,rentalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_app);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homeName = findViewById(R.id.tvHomestayName);
        in = findViewById(R.id.tvInDate);
        out = findViewById(R.id.tvOutDate);
        totPrice = findViewById(R.id.tvTotalPrice);
        status = findViewById(R.id.tvStatus);
        rate = findViewById(R.id.rating);
        totDays = findViewById(R.id.tvTotalDays);
       // userName = findViewById(R.id.tvUserName);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        rate.setVisibility(View.GONE);

        final Intent intent = getIntent();

        final String rentalID = intent.getStringExtra("userID");
        String bookID = intent.getStringExtra("bookingID");

        userID = rentalID;
        bookingID = bookID;


        //DatabaseReference databaseReference = firebaseDatabase.getReference("Booking").child(userID);

        homename = intent.getStringExtra("homestayName");
        //userPhone = intent.getStringExtra("userPhone");
        inDate =intent.getStringExtra("inDate");
        outDate = intent.getStringExtra("outDate");
        totalPrice = intent.getStringExtra("price");
        Bstatus = intent.getStringExtra("status");
        totalDays = intent.getStringExtra("days");
        rentalName = intent.getStringExtra("userName");
        homeid = intent.getStringExtra("homestayID");


        homeName.setText(homename);
        //rentalPhone.setText(userPhone);
        in.setText(inDate);
        out.setText(outDate);
        totPrice.setText(totalPrice);
        totDays.setText(totalDays);
        status.setText(Bstatus);
        //userName.setText(rentalName);
        //homeID.setText(homeid);



        homename = homeName.getText().toString();
        //userPhone = rentalPhone.getText().toString();
        inDate = in.getText().toString();
        outDate = out.getText().toString();
        totalDays = totDays.getText().toString();
        totalPrice = totPrice.getText().toString();
        Bstatus = status.getText().toString();
       // rentalName = userName.getText().toString();
        //homeid = homeID.getText().toString();

        if(Bstatus.equals("Accepted")) {
                    rate.setVisibility(View.VISIBLE);
        }

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent confirm;
                    confirm = new Intent(getApplicationContext(), RateHomestay.class);
                    confirm.putExtra("HomestayID", homeid);
                    confirm.putExtra("HomestayName", homename);
                    confirm.putExtra("UserName", rentalName);
                    //confirm.putExtra("HostID",hostID);
                    Toast.makeText(getApplicationContext(), "The Homestay name is " + homename, Toast.LENGTH_SHORT).show();
                    startActivity(confirm);

            }
        });
    }

}
