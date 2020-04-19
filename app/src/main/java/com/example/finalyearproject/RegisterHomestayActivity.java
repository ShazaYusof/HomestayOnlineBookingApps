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
import android.widget.Toast;

import com.example.finalyearproject.Model.HomestayProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import com.uncopt.android.widget.text.justify.JustifiedEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterHomestayActivity extends AppCompatActivity {

    EditText bed,toilet,price;
    JustifiedEditText title,address,link;
    Button register;
    CircleImageView img;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    String HomeTitle,HomeBed,HomeToilet,HomeAddress,HomePrice,HomeLink;
    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;

    FirebaseStorage storage;
    FirebaseFirestore firestore;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_homestay);

        title = findViewById(R.id.etTitle);
        bed = findViewById(R.id.etBed);
        toilet = findViewById(R.id.etToilet);
        address = findViewById(R.id.etAddress);
        register = findViewById(R.id.btnReg);
        price = findViewById(R.id.priceNight);
        img = findViewById(R.id.ivHomePic);
        //link = findViewById(R.id.etLink);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){


                    //upload data to database
                   sendHomestayData();
                   uploadImg();
                   Intent intent = new Intent(RegisterHomestayActivity.this,HomeHostActivity.class);
                   startActivity(intent);
                    Toast.makeText(RegisterHomestayActivity.this,"Homestay Registration Successful", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(RegisterHomestayActivity.this, HomeHostActivity.class));

                }
                else{
                    Toast.makeText(RegisterHomestayActivity.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public void sendHomestayData(){

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Homestay").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        HomestayProfile homestayProfile = new HomestayProfile(HomeAddress,HomeBed,HomeTitle,HomePrice,HomeToilet,FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.setValue(homestayProfile);
    }


    private Boolean validate(){
        Boolean result = false;

        HomeTitle = title.getText().toString();
        HomePrice = price.getText().toString();
        HomeBed = bed.getText().toString();
        HomeToilet = toilet.getText().toString();
        HomeAddress = address.getText().toString();




        if(HomeTitle.isEmpty() || HomeBed.isEmpty() || HomeToilet.isEmpty() || HomeAddress.isEmpty() || HomePrice.isEmpty()){

            Toast.makeText(this,"Please enter all the details and pictures!", Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return result;

    }

    public  void uploadImg(){
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Homestay Pic");  //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterHomestayActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(RegisterHomestayActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
