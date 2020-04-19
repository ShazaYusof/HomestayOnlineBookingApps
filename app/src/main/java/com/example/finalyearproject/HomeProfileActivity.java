package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.Model.HomestayProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeProfileActivity extends AppCompatActivity {

    private TextView title,address,bed,toilet,price;
    private Button edit;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    CircleImageView homeImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.tvTitle);
        bed = findViewById(R.id.ivBed);
        toilet = findViewById(R.id.ivToilet);
        address = findViewById(R.id.tvAddress);
        edit = findViewById(R.id.btnEdit);
        homeImg = findViewById(R.id.getHomePic);
        price = findViewById(R.id.tvPrice);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseReference databaseReference = firebaseDatabase.getReference("Homestay").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Homestay Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(homeImg);
            }
        });

        // = firebaseDatabase.getReference("Homestay").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //databaseReference = FirebaseDatabase.getInstance().getReference("Homestay");

        /*databaseReference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                            ImageUploadInfo upload = postSnapshot.getValue(ImageUploadInfo.class);
                                                            mUploads.add(upload);
                                                        }

                                                        mAdapter = new ImagesAdapter(HomeProfileActivity.this, mUploads);

                                                        mRecyclerView.setAdapter(mAdapter);
                                                        mProgressCircle.setVisibility(View.INVISIBLE);
                                                    }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });*/



        //StorageReference storageReference = firebaseStorage.getReference();
       /* storageReference.child(firebaseAuth.getUid()).child("HomestayPic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(imgA);
                Picasso.get().load(uri).fit().centerCrop().into(imgB);
                Picasso.get().load(uri).fit().centerCrop().into(imgC);
                Picasso.get().load(uri).fit().centerCrop().into(imgD);

            }

        });*/

        edit.setOnClickListener(new View.OnClickListener(){
                                          @Override
                                          public void onClick(View view){
                                              Intent intent = new Intent(getApplicationContext(), UpdateHouseActivity.class);
                                              startActivity(intent);
                                          }
                                      }
        );

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HomestayProfile homestayProfile = dataSnapshot.getValue(HomestayProfile.class);
                title.setText(homestayProfile.getHomestayName());
                price.setText(homestayProfile.getHomestayPrice() );
                bed.setText( homestayProfile.getNumBed());
                toilet.setText( homestayProfile.getNumToilet());
                address.setText(homestayProfile.getHomestayAddress());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}
