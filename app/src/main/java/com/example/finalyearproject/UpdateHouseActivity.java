package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalyearproject.Model.HomestayProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UpdateHouseActivity extends AppCompatActivity {


    private EditText upPrice,upBed,upToilet,upAddress,upTitle;
    private ImageView img1;
    private Button save;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    String title,price,room,bath,Haddress;
    private static int PICK_IMAGE = 123;
    private final int CODE_MULTIPLE_IMG_GALLERY=5,IMG2=2,IMG3=3,IMG4=4;
    Uri imagePath;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                img1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_house);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        upTitle = findViewById(R.id.etUpTitle);
        upPrice = findViewById(R.id.etUpPrice);
        upBed = findViewById(R.id.etUpBed);
        upToilet = findViewById(R.id.etUpToilet);
        upAddress = findViewById(R.id.etUpAddress);
        img1 = findViewById(R.id.img1);
        save = findViewById(R.id.btnSave);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        final DatabaseReference databaseReference = firebaseDatabase.getReference("Homestay").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HomestayProfile homestayProfile = dataSnapshot.getValue(HomestayProfile.class);
                upTitle.setText(homestayProfile.getHomestayName());
                upBed.setText(homestayProfile.getNumBed());
                upToilet.setText(homestayProfile.getNumToilet());
                upAddress.setText(homestayProfile.getHomestayAddress());
                upPrice.setText(homestayProfile.getHomestayPrice());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateHouseActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });

        final StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Homestay Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(img1);

            }
        });

       save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                saveHomestayData();
                Intent intent = new Intent(UpdateHouseActivity.this,HomeProfileActivity.class);
                startActivity(intent);
                Toast.makeText(UpdateHouseActivity.this,"Homestay Profile Updated!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(UpdateHouseActivity.this, HomeActivity.class));

            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });

    }

    public void saveHomestayData(){
        title = upTitle.getText().toString();
        room = upBed.getText().toString();
        bath = upToilet.getText().toString();
        price = upPrice.getText().toString();
        Haddress = upAddress.getText().toString();

        HomestayProfile homestayProfile = new HomestayProfile(Haddress,room,title,price,bath,FirebaseAuth.getInstance().getCurrentUser().getUid());
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Homestay").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.setValue(homestayProfile);

        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Homestay Pic");  //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateHouseActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(UpdateHouseActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
            }
        });

        finish();
    }

}
