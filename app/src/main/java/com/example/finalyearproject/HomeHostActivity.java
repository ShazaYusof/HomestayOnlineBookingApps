package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.List.ListBooking;
import com.example.finalyearproject.List.ListHostReview;
import com.example.finalyearproject.List.ListReview;
import com.example.finalyearproject.Model.HostProfile;
import com.example.finalyearproject.Model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeHostActivity extends AppCompatActivity {

    private Button btnProfile,btnRegister,btnHouse,btnBooked,btnRate;
    TextView hostName;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_host);

        btnProfile = findViewById(R.id.buttonProfile);
        btnRegister = findViewById(R.id.registerHomestay);
        btnHouse = findViewById(R.id.buttonHouse);
        btnBooked = findViewById(R.id.buttonBooked);
        btnRate = findViewById(R.id.rateHouse);
        hostName = findViewById(R.id.tvUserName);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("Host").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HostProfile hostProfile = dataSnapshot.getValue(HostProfile.class);
               hostName.setText( "Hai " + hostProfile.getHostName() + "!!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeHostActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){
                                         @Override
                                         public void onClick(View view){
                                             Intent intent = new Intent(getApplicationContext(), RegisterHomestayActivity.class);
                                             startActivity(intent);
                                         }
                                     }
        );
        btnProfile.setOnClickListener(new View.OnClickListener(){
                                          @Override
                                          public void onClick(View view){
                                              Intent intent = new Intent(getApplicationContext(), HostProfileActivity.class);
                                              startActivity(intent);
                                          }
                                      }
        );
        btnHouse.setOnClickListener(new View.OnClickListener(){
                                          @Override
                                          public void onClick(View view){
                                              Intent intent = new Intent(getApplicationContext(), HomeProfileActivity.class);
                                              startActivity(intent);
                                          }
                                      }
        );
        btnBooked.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View view){
                                            Intent intent = new Intent(getApplicationContext(), ListBooking.class);
                                            startActivity(intent);
                                        }
                                    }
        );

        btnRate.setOnClickListener(new View.OnClickListener(){
                                         @Override
                                         public void onClick(View view){
                                             Intent intent = new Intent(getApplicationContext(), ListHostReview.class);
                                             startActivity(intent);
                                         }
                                     }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.btnLogout:
                firebaseAuth.signOut();
                startActivity(new Intent(HomeHostActivity.this, MainActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
