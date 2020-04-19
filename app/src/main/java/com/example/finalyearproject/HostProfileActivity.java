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

import com.example.finalyearproject.Model.HostProfile;
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

public class HostProfileActivity extends AppCompatActivity {

    private CircleImageView profilePic;
    private TextView profileName,profilePhone,profileEmail,changePassword;
    private Button edit;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_profile);

        profilePic = findViewById(R.id.ivProfilePic);
        profileName = findViewById(R.id.tvProfileName);
        profilePhone = findViewById(R.id.tvProfilePhone);
        profileEmail = findViewById(R.id.tvProfileEmail);
        edit = findViewById(R.id.btnEdit);
        changePassword = findViewById(R.id.tvChangePassword);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseReference databaseReference = firebaseDatabase.getReference("Host").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images").child("Host Profile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profilePic);

            }
        });

        edit.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view){
                                        Intent intent = new Intent(getApplicationContext(), UpdateHostActivity.class);
                                        startActivity(intent);
                                    }
                                }
        );

        changePassword.setOnClickListener(new View.OnClickListener(){
                                              @Override
                                              public void onClick(View view){
                                                  Intent intent = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                                                  startActivity(intent);
                                              }
                                          }
        );

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HostProfile hostProfile = dataSnapshot.getValue(HostProfile.class);
                profileName.setText(hostProfile.getHostName());
                profilePhone.setText(hostProfile.getHostPhone());
                profileEmail.setText(hostProfile.getHostEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HostProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });

    }

}
