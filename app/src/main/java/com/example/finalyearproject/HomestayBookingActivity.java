package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.List.ListReview;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomestayBookingActivity extends AppCompatActivity {

    public TextView houseName,housePrice,houseBed,houseToilet,rating;
    JustifiedTextView houseAddress;
    CircleImageView home;
    Button book;
    String homePrice,homeName,homeID,homeAddress,homeBed,homeToilet,houseID;
    //DatabaseReference myRef;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homestay_booking);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //homestayRef = FirebaseDatabase.getInstance().getReference("Homestay");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        houseName = findViewById(R.id.viewName);
        houseAddress = findViewById(R.id.viewAddress);
        housePrice = findViewById(R.id.viewPrice);
        home = findViewById(R.id.viewHome);
        book = findViewById(R.id.booking);
        houseBed= findViewById(R.id.viewRoom);
        houseToilet = findViewById(R.id.viewToilet);
        rating = findViewById(R.id.homestayRating);

        final Intent intent = getIntent();
        //String name = intent.getStringExtra("homeName");
        //houseID = intent.getStringExtra("homestayID");

       // homeID = houseID;
        //homeName = name;


        homeName= intent.getStringExtra("homeName");
        homeAddress = intent.getStringExtra("homeAddress");
        homeBed =intent.getStringExtra("homeBed");
        homeToilet = intent.getStringExtra("homeToilet");
        homePrice = intent.getStringExtra("homePrice");
        homeID = intent.getStringExtra("homestayID");

        houseName.setText(homeName);
        houseAddress.setText(homeAddress);
        houseBed.setText(homeBed);
        houseToilet.setText(homeToilet);
        housePrice.setText(homePrice);


        homeName = houseName.getText().toString();
        homeAddress = houseAddress.getText().toString();
        homeBed = houseBed.getText().toString();
        homeToilet = houseToilet.getText().toString();
        homePrice = housePrice.getText().toString();

//        StorageReference storageReference = firebaseStorage.getReference();
//
//        storageReference.child(firebaseAuth.getUid()).child("Images/Homestay Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).fit().centerCrop().into(home);
//            }
//        });



        rating.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                             Intent click;
                                             click = new Intent(getApplicationContext(), ListReview.class);
                                             click.putExtra("homestayID",homeID);
                                             Toast.makeText(getApplicationContext(), "Go to Rating Page homestay " + homeName, Toast.LENGTH_SHORT).show();
                                             startActivity(click);
                                         }
                                     }
        );

        book.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent confirm;
                                        confirm = new Intent(getApplicationContext(), ConfirmBooking.class);
                                        confirm.putExtra("homePrice",homePrice);
                                        confirm.putExtra("homeName",homeName);
                                        confirm.putExtra("HomestayID",homeID);
                                        //Toast.makeText(getApplicationContext(), "Homestay Price is " + homePrice, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(), "Homestay name is " + homeName,Toast.LENGTH_SHORT).show();
                                        startActivity(confirm);
                                    }
                                }
        );


    }
}
