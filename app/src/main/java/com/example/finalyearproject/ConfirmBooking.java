package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.Model.Booking;
import com.example.finalyearproject.Model.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmBooking extends AppCompatActivity {

    Calendar mCurrentDate;
    int day,month,year;
    String bookID,renName,renPhone,renEmail,homestayName,inDate,outDate,totDays,totPrice,status;
    TextView name, phone, email,from,to,totalDays,totalPrice,homeName,HomestayStatus;
    Button confirm,calculate;
    CircleImageView profile;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    String homestayTitle;
    double price,totalprice;
    String homeprice;
    String homeID;

    private NotificationCompat notificationCompat;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        name = findViewById(R.id.tvProfileName);
        phone = findViewById(R.id.tvProfilePhone);
        email = findViewById(R.id.tvProfileEmail);
        confirm = findViewById(R.id.btnConfirm);
        from = findViewById(R.id.dateFrom);
        to = findViewById(R.id.dateTo);
        totalDays = findViewById(R.id.totDays);
        totalPrice = findViewById(R.id.totPrice);
        calculate = findViewById(R.id.btnCalculate);
        homeName = findViewById(R.id.tvHomestayName);
        profile = findViewById(R.id.userProfile);
        HomestayStatus = findViewById(R.id.homeStatus);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        final Intent intent = getIntent();

        homeprice = getIntent().getStringExtra("homePrice");
        final String homename = getIntent().getStringExtra("homeName");
        homeID = getIntent().getStringExtra("HomestayID");


        homeName.setText(homename);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseReference databaseReference = firebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profile);
            }
        });


        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;

        from.setText(day + "/"  +month + "/" + year);
        to.setText(day + "/"  +month + "/" + year);

        from.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view){
                                        DatePickerDialog datePickerDialog = new DatePickerDialog(ConfirmBooking.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                monthOfYear = monthOfYear+1;
                                                from.setText(dayOfMonth + "/" + monthOfYear +"/" + year);
                                            }
                                        },year,month,day);
                                        datePickerDialog.show();
                                    }
                                }
        );

        to.setOnClickListener(new View.OnClickListener(){
                                  @Override
                                  public void onClick(View view){
                                      DatePickerDialog datePickerDialog = new DatePickerDialog(ConfirmBooking.this, new DatePickerDialog.OnDateSetListener() {
                                          @Override
                                          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                              monthOfYear = monthOfYear+1;
                                              to.setText(dayOfMonth + "/" + monthOfYear +"/" + year);
                                          }
                                      },year,month,day);
                                      datePickerDialog.show();
                                  }
                              }
        );

        calculate.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                             calculateDay();

                                         }
                                     }
        );


        confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(validate()) {

                                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Booking").push();


                                            Booking booking = new Booking(renName,renPhone,renEmail,homestayName,inDate,outDate,totDays,totPrice,homeID,FirebaseAuth.getInstance().getCurrentUser().getUid(),status);

                                            addBooking(booking);

                                        }
                                    }
                                }
        );


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                name.setText(  userProfile.getUserName());
                phone.setText( userProfile.getUserPhone());
                email.setText(userProfile.getUserEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ConfirmBooking.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Boolean validate(){
        Boolean result = false;

        renName = name.getText().toString();
        renPhone = phone.getText().toString();
        renEmail = email.getText().toString();
        homestayName = homeName.getText().toString();
        inDate = from.getText().toString();
        outDate = to.getText().toString();
        totDays = totalDays.getText().toString();
        totPrice = totalPrice.getText().toString();
        status = HomestayStatus.getText().toString();



        if( inDate.isEmpty() || outDate.isEmpty() || totDays.isEmpty() || totPrice.isEmpty() ){

            Toast.makeText(this,"Please enter check in and check out date & click " +
                    "CALCULATE button!!", Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return result;

    }

    public void pushNotification()
    {
        String message = "Thank you. You had booking homestay" ;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ConfirmBooking.this)
                .setSmallIcon(R.drawable.ic_comment)
                .setContentTitle("Malacca iHomestay")
                .setContentText(message)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        /*Intent intent = new Intent(ConfirmBooking.this,InfoAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",message);

        PendingIntent pendingIntent = PendingIntent.getActivity(ConfirmBooking.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);*/
        notificationManager.notify(0,builder.build());
    }

    public void calculateDay()
    {
        String sDate = from.getText().toString();
        String eDate = to.getText().toString();
        //homestayPrice = totalPrice.getText().toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = simpleDateFormat.parse(sDate);
            Date date2 = simpleDateFormat.parse(eDate);

            long startDate = date1.getTime();
            long endDate = date2.getTime();

            if(startDate <= endDate){


                Period period = new Period(startDate,endDate, PeriodType.yearMonthDay());
                int years = period.getYears();
                int months = period.getMonths();
                day = period.getDays();

                totalDays.setText( day+ " days");
            }
            else
            {
                Toast.makeText(ConfirmBooking.this,"Check Out date should not be larger than Check In's date!",Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

         // totalPrice = homestayPrice*totalDays;


        //price = 0;

        int HOMEPRICE = Integer.parseInt(homeprice);

        //int price = Double.parseDouble(totPrice);

       totalprice = HOMEPRICE * day;

        totalPrice.setText(""+ totalprice);

    }

    private void addBooking(Booking booking){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Booking").push();

        String key = myref.getKey();
        booking.setBookingID(key);

        //add post data to firebase database
        myref.setValue(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(ConfirmBooking.this,"Thank you for booking. Please wait for host confirmation!!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(ConfirmBooking.this, HomeActivity.class));
            }
        });

    }
}
