package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login02Activity extends AppCompatActivity {

    private EditText HostEmail;
    private EditText HostPassword;
    private Button HostButton;
    private TextView tv_counter;
    private TextView tv_forgotPass;
    private int counter = 3;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login02);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HostEmail = findViewById(R.id.LoginName);
        HostPassword = findViewById(R.id.LoginPassword);
        HostButton = findViewById(R.id.Loginbutton);
        tv_counter = findViewById(R.id.tvCounter);
        tv_forgotPass = findViewById(R.id.tvForgotPassword);

        tv_counter.setText("No of attempts remaining: 3");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(Login02Activity.this, HomeHostActivity.class));
        }

        HostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(enter()) {
                    validate(HostEmail.getText().toString(), HostPassword.getText().toString());
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
                    Toast.makeText(Login02Activity.this,"Login Failed!!",Toast.LENGTH_SHORT).show();
                    counter--;
                    tv_counter.setText("No of attempts remaining: " + counter);
                    progressDialog.dismiss();
                    if(counter ==0){
                        HostButton.setEnabled(false);
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
            startActivity(new Intent(Login02Activity.this,HomeHostActivity.class));
        }
        else{
            Toast.makeText(this,"Please verify your email!!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.HostRegister:
                startActivity(new Intent(Login02Activity.this, SignhostActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean enter(){
        Boolean result = false;

        String password = HostPassword.getText().toString();
        String email = HostEmail.getText().toString();


        if(password.isEmpty() || email.isEmpty() ){

            Toast.makeText(this,"Please enter host email and password!", Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return result;
    }


}
