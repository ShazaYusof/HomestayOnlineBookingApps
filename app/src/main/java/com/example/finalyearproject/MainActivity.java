package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView btn_User,btn_Host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_User = findViewById(R.id.btnUser);
        btn_Host = findViewById(R.id.btnReg);



        btn_User.setOnClickListener(new View.OnClickListener(){
                                          @Override
                                          public void onClick(View view){
                                              Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                              startActivity(intent);
                                          }
                                      }
        );
        btn_Host.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View view){
                                            Intent intent = new Intent(getApplicationContext(), Login02Activity.class);
                                            startActivity(intent);
                                        }
                                    }
        );



    }
}
