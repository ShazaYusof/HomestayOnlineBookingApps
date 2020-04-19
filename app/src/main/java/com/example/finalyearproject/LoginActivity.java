package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText Email;
    private EditText password;
    private Button LoginUser;
    private TextView info;
    private TextView tv_forgotPass;
    private int counter = 3;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Email = findViewById(R.id.LoginName);
        password = findViewById(R.id.LoginPassword);
        info = findViewById(R.id.tvCounter);
        LoginUser = findViewById(R.id.LoginbuttonUser);
        tv_forgotPass = findViewById(R.id.tvForgotPassword);

        info.setText("No of attempts remaining: 3");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user !=null)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }

        LoginUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(enter()) {
                    validate(Email.getText().toString(), password.getText().toString());
                }
            }
        });

        tv_forgotPass.setOnClickListener(new View.OnClickListener(){
                                             @Override
                                             public void onClick(View view){
                                                 Intent intent = new Intent(getApplicationContext(), PasswordHostActivity.class);
                                                 startActivity(intent);
                                             }
                                         }
        );

    }

    private void validate(String userName, String userPassword)
    {
        progressDialog.setMessage("Please wait until you are verified!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    //Toast.makeText(LoginActivity.this,"Login Successful!!",Toast.LENGTH_SHORT).show();
                    checkEmailVerification();

                }
                else{
                    Toast.makeText(LoginActivity.this,"Login Failed!!",Toast.LENGTH_SHORT).show();
                    counter--;
                    info.setText("No of attempts remaining: " + counter);
                    progressDialog.dismiss();
                    if(counter ==0){
                        LoginUser.setEnabled(false);
                    }
                }
            }
        });
    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if(emailFlag){
            finish();
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));

        }
        else{
            Toast.makeText(this,"Please verify your email!!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }

    private Boolean enter(){
        Boolean result = false;

        String userPassword = password.getText().toString();
        String userEmail = Email.getText().toString();


        if(userPassword.isEmpty() || userEmail.isEmpty() ){

            Toast.makeText(this,"Please enter user email and password!", Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return result;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.UserReg:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}


