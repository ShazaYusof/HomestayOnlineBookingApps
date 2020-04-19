package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.finalyearproject.List.ListReview;
import com.example.finalyearproject.Model.HomestayProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchHomeActivity extends AppCompatActivity implements IFirebaseLoadDone {

    SearchableSpinner search;
    DatabaseReference homestayRef;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<HomestayProfile> homes;
    public TextView title, price, phone,back,room,toilet,textA,to,clickRate;
    JustifiedTextView address;
    CircleImageView homeA;
    Button book,calculate;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    BottomSheetDialog bottomSheetDialog;
    ImageView image;
    String homePrice,homeName,homeid,hostid;


    boolean isFirstTimeClick=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search = findViewById(R.id.find);
        image = findViewById(R.id.img);
        textA = findViewById(R.id.viewText);

        homestayRef = FirebaseDatabase.getInstance().getReference("Homestay");

        bottomSheetDialog = new BottomSheetDialog(this);
        final View homestay = getLayoutInflater().inflate(R.layout.activity_view_homestay, null);

        final Intent intent = getIntent();
        String name = intent.getStringExtra("homeName");
        String homeID = intent.getStringExtra("homestayID");

        homeid=homeID;

        title = homestay.findViewById(R.id.homeName);
        address = homestay.findViewById(R.id.viewAddress);
        price = homestay.findViewById(R.id.viewPrice);
        homeA = homestay.findViewById(R.id.viewHome);
        book = homestay.findViewById(R.id.booking);
        back = homestay.findViewById(R.id.btnBack);
        room = homestay.findViewById(R.id.viewRoom);
        toilet = homestay.findViewById(R.id.viewToilet);
        clickRate = homestay.findViewById(R.id.homestayRating);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        StorageReference storageReference = firebaseStorage.getReference();

        storageReference.child(firebaseAuth.getUid()).child("Images/Homestay Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(homeA);
            }
        });

        //DatabaseReference databaseReference = firebaseDatabase.getReference("Booking").child(userID);

//        StorageReference storageReference = firebaseStorage.getReference();
//        storageReference.child(firebaseAuth.getUid()).child("Images/HomeStay Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).fit().centerCrop().into(home);
//
//            }
//
//        });

        clickRate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent click;
                                        click = new Intent(getApplicationContext(), ListReview.class);
                                        click.putExtra("homestayID",homeid);
                                        //Toast.makeText(getApplicationContext(), "Go to Rating Page homestay " + name, Toast.LENGTH_SHORT).show();
                                        startActivity(click);
                                    }
                                }
        );


        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        bottomSheetDialog.dismiss();
                                        Toast.makeText(SearchHomeActivity.this,"Back to List Page",Toast.LENGTH_SHORT).show();
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
                                        confirm.putExtra("HomestayID",homeid);
                                        //Toast.makeText(getApplicationContext(), "Homestay Price is " + homePrice, Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getApplicationContext(), "Homestay ID is " + homeid, Toast.LENGTH_SHORT).show();
                                        startActivity(confirm);
                                    }
                                }
        );

        bottomSheetDialog.setContentView(homestay);


        search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long I) {

                if(isFirstTimeClick)
                {
                    HomestayProfile home  = homes.get(i) ;
                    homePrice = home.getHomestayPrice();
                    homeName = home.getHomestayName();
                    title.setText(home.getHomestayName());
                    address.setText(home.getHomestayAddress());
                    room.setText("Num of Room :" + home.getNumBed());
                    toilet.setText("Num of Toilet:" + home.getNumToilet());
                    price.setText("Price per night : RM" + home.getHomestayPrice());
                    homeid=home.getHomestayID();
                    //homeA = home.getPictureLink();



                    bottomSheetDialog.show();
                }
                else{
                    isFirstTimeClick=true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        iFirebaseLoadDone = this;
        homestayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<HomestayProfile> homes = new ArrayList<>();

                for(DataSnapshot homeSnapShot:dataSnapshot.getChildren()) {

                    homes.add(homeSnapShot.getValue(HomestayProfile.class));

                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(homes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());

            }
        });

    }



    public void zoomAnimation(View view){
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f,4f,1f,4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setInterpolator(new LinearInterpolator());
        scaleAnimation.setDuration(1800);

        textA.startAnimation(scaleAnimation);
    }

    public void rotateAnimation(View view){
        RotateAnimation rotateAnimation = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setDuration(8000);

        image.startAnimation(rotateAnimation);
    }


    @Override
    public void onFirebaseLoadSuccess(List<HomestayProfile> homestayList) {
        homes = homestayList;

        List<String> name_list = new ArrayList<>();
        //List<String> name_listB = new ArrayList<>();
        for(HomestayProfile homes:homestayList)
            //name_list.add(homes.getHomeName());
            name_list.add(homes.getHomestayPrice());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name_list);
            search.setAdapter(adapter);

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}
