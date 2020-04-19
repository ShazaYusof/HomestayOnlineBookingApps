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

import com.example.finalyearproject.List.ListBookingHistory;
import com.example.finalyearproject.Model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private Button btnProfile,btnInfo,btnFind,btnSearch;
    private FirebaseDatabase firebaseDatabase;
    TextView userName;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnProfile = findViewById(R.id.buttonProfile);
        btnInfo = findViewById(R.id.buttonInfo);
        btnFind = findViewById(R.id.buttonFind);
        btnSearch = findViewById(R.id.buttonSearch);
        userName = findViewById(R.id.tvUserName);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                userName.setText( "Hai " + userProfile.getUserName() + "!!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity .this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener(){
                                                           @Override
                                                           public void onClick(View view){
                                                               Intent intent = new Intent(getApplicationContext(), SearchHomeActivity.class);
                                                               startActivity(intent);
                                                           }
                                                       }
        );
        btnProfile.setOnClickListener(new View.OnClickListener(){
                                                            @Override
                                                            public void onClick(View view){
                                                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
        );
        btnFind.setOnClickListener(new View.OnClickListener(){
                                          @Override
                                          public void onClick(View view){
                                              Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                              startActivity(intent);
                                          }
                                      }
        );
        btnInfo.setOnClickListener(new View.OnClickListener(){
                                       @Override
                                       public void onClick(View view){
                                           Intent intent = new Intent(getApplicationContext(), ListBookingHistory.class);
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
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
