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

import com.example.finalyearproject.Model.HostProfile;
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

public class UpdateHostActivity extends AppCompatActivity {

    private EditText updateName,updateEmail,updatePhone;
    private ImageView updateProfile;
    private Button save;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    String name,email,phone;
    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                updateProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_host);

        updateName = findViewById(R.id.etNameUpdate);
        updateEmail = findViewById(R.id.etEmailUpdate);
        updatePhone = findViewById(R.id.etPhoneUpdate);
        save = findViewById(R.id.btnSave);
        updateProfile = findViewById(R.id.ivProfileUpdate);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DatabaseReference databaseReference = firebaseDatabase.getReference("Host").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HostProfile hostProfile = dataSnapshot.getValue(HostProfile.class);
                updateName.setText(hostProfile.getHostName());
                updatePhone.setText(hostProfile.getHostPhone());
                updateEmail.setText(hostProfile.getHostEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateHostActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });

        final StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Host Profile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(updateProfile);
            }
        });

       save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    name = updateName.getText().toString();
                    email = updateEmail.getText().toString();
                    phone = updatePhone.getText().toString();

                    HostProfile hostProfile = new HostProfile(FirebaseAuth.getInstance().getCurrentUser().getUid(),email, name, phone);

                    databaseReference.setValue(hostProfile);

                    StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Host Profile");  //User id/Images/Profile Pic.jpg
                    UploadTask uploadTask = imageReference.putFile(imagePath);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateHostActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(UpdateHostActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                        }
                    });


                    Toast.makeText(UpdateHostActivity.this, "Profile Updated!!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(UpdateHostActivity.this, HostProfileActivity.class));

            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });

    }

//    private Boolean validate(){
//        Boolean result = false;
//
//        if( imagePath != null){
//
//            StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Host Profile");  //User id/Images/Profile Pic.jpg
//            UploadTask uploadTask = imageReference.putFile(imagePath);
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(UpdateHostActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    Toast.makeText(UpdateHostActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        else{
//            result = true;
//        }
//        return result;
//
//    }

}
