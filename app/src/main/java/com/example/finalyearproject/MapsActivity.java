package com.example.finalyearproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.finalyearproject.List.ListHomestayMaps;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnInfoWindowClickListener{

    private GoogleMap mMap;
    //SearchView searchView;
    ImageView homeSearch;
    BottomSheetDialog bottomSheetDialog;
    private PulsatorLayout pulseView;

    boolean isFirstTimeClick=true;

    private ProgressDialog progressDialog;

    private static final int MY_PERMISSION_REQUEST_CODE = 7192;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 300193;


    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000; //5SEC
    private static int FATEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;



    DatabaseReference ref;
    GeoFire geoFire;
    Marker mCurrent;
    VerticalSeekBar mSeekbar;
    private TextView viewname,viewadd,viewdet;
    DatabaseReference rootRef, HomeRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ref = FirebaseDatabase.getInstance().getReference("MyLocation");
        geoFire = new GeoFire(ref);

        progressDialog = new ProgressDialog(this);

        pulseView = findViewById(R.id.pulsator);

        homeSearch = findViewById(R.id.searchHome);
        homeSearch.setOnClickListener(new View.OnClickListener(){

                                         @Override
                                         public void onClick(View view){
//                                             progressDialog.setMessage("Searching Homestays!");
//                                             progressDialog.show();
                                             //pulseView.setDuration(4000);
                                             pulseView.start();
                                             new Handler().postDelayed(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     pulseView.stop();
                                                     Intent intent = new Intent(getApplicationContext(), ListHomestayMaps.class);
                                                     startActivity(intent);
                                                 }
                                             },7000);

                                             //progressDialog.dismiss();

                                         }
                                     }
        );


//        searchView = findViewById(R.id.sv_location);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit (String query){
//
//                        String location = searchView.getQuery().toString();
//                        List<Address> addressList = null;
//
//                        if (location != null || !location.equals("")) {
//                            Geocoder geocoder = new Geocoder(MapsActivity.this);
//                            try {
//                                addressList = geocoder.getFromLocationName(location, 1);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            Address address = addressList.get(0);
//                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 0));
//                        }
//
//
//
//                    return false;
//                }
//                @Override
//                public boolean onQueryTextChange (String newText){
//                return false;
//                }
//
//        });

        mSeekbar = findViewById(R.id.verticalSeekBar);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(progress),2000,null);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setUpLocation();
        mapFragment.getMapAsync(this);


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_CODE:
                if(grantResults .length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(checkPlayServices()){
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }
    }

    private void setUpLocation(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //request runtime permission
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_REQUEST_CODE);
        }
        else
        {
            if(checkPlayServices()){
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private void displayLocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null)
        {
            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();
            //update to geofire
            geoFire.setLocation("You", new GeoLocation(latitude, longitude),
                    new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            //add marker
                            if(mCurrent != null)
                                mCurrent.remove();//remove old marker

                             mCurrent = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude,longitude))
                            .title("You"));
                            //move camera to this position
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),12.0f));
                        }
                    });
            if(mCurrent != null)
                mCurrent.remove();//remove old marker

            Log.d("Geofence",String.format("Your Location was changed : %f / %f ",latitude,longitude));
        }
        else
            Log.d("Geofence","Can not get your location");
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            else
            {
                Toast.makeText(this,"This device is not supported",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        int height = 50;
        int width = 50;
        //BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.ic_launcher);
        //Bitmap b=bitmapdraw.getBitmap();
        //Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);



        mMap = googleMap;
        //mMap.setOnInfoWindowClickListener(this);

        //mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMinZoomPreference(11);

        //ni buat marker yg warna biru kecik tuu..coding simple af
        try{
            mMap.setMyLocationEnabled(true);

        }catch (SecurityException ex){

        }
        //UiTMJasin
//        LatLng UiTMJasin = new LatLng(2.226145, 102.454494);
//        mMap.addCircle(new CircleOptions()
//                .center(UiTMJasin)
//                .radius(500)//in metre
//                .strokeColor(Color.BLUE)
//                .fillColor(0x220000FF)
//                .strokeWidth(5.0f)
//        );
//
//         mMap.addMarker(new MarkerOptions().position(UiTMJasin).title("UiTM Melaka Kampus Jasin")
//                    //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
//                    .snippet("Price: Rm150/night" )
//
//            );
//
//
//
//
//
//        //add geoQueryhere for UiTMJasin
//        //0.5f = 0.5km = 500m
//        GeoQuery geoQueryUiTM = geoFire.queryAtLocation(new GeoLocation(UiTMJasin.latitude,UiTMJasin.longitude),0.5f);
//        geoQueryUiTM.addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String key, GeoLocation location) {
//                sendNotification("Geofence",String.format("%s entered the event area",key));
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//                sendNotification("Geofence",String.format("%s leaved the event area",key));
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//                Log.d("MOVE",String.format("%s moved within the dangerous area [%f/%f]",key,location.latitude,location.longitude));
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//                Log.e("ERROR",""+error);
//            }
//        });

        //Dpinang Homestay Klebang Melaka
        LatLng Dpinang = new LatLng(2.222748, 102.180620);
        mMap.addCircle(new CircleOptions()
                .center(Dpinang)
                .radius(500)//in metre
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f)
        );

        mMap.addMarker(new MarkerOptions().position(Dpinang).title("Dpinang Homestay Klebang Melaka")
                //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .snippet("Price: Rm146/night" )
        );




        //add geoQueryhere for Dpinang Homestay Klebang Melaka
        //0.5f = 0.5km = 500m
        GeoQuery geoQueryDpinang = geoFire.queryAtLocation(new GeoLocation(Dpinang.latitude,Dpinang.longitude),0.5f);
        geoQueryDpinang.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                sendNotification("Malacca i-Homestay",String.format("%s you nearly reached to your homestay within 500m",key));
            }

            @Override
            public void onKeyExited(String key) {
                sendNotification("Malacca i-Homestay",String.format("%s you leaved the homestay.Thank you !",key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d("MOVE",String.format("%s moved within the dangerous area [%f/%f]",key,location.latitude,location.longitude));
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("ERROR",""+error);
            }
        });
        //A'Famosa Malacca Title Homestay
        LatLng MalaccaTitle = new LatLng(2.377013, 102.232276);
        mMap.addCircle(new CircleOptions()
                .center(MalaccaTitle)
                .radius(500)//in metre
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f)
        );

         mMap.addMarker(new MarkerOptions().position(MalaccaTitle).title("A'Famosa Malacca Title Homestay")
                //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .snippet("Price: Rm157/night" )
        );



        //add geoQueryhere for A'Famosa Malacca Title Homestay
        //0.5f = 0.5km = 500m
        GeoQuery geoQueryMalaccaTitle = geoFire.queryAtLocation(new GeoLocation(MalaccaTitle.latitude,MalaccaTitle.longitude),0.5f);
        geoQueryMalaccaTitle.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                sendNotification("Malacca i-Homestay",String.format("%s you nearly reached to your homestay within 500m",key));
            }

            @Override
            public void onKeyExited(String key) {
                sendNotification("Malacca i-Homestay",String.format("%s you leaved the homestay.Thank you !",key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d("MOVE",String.format("%s moved within the dangerous area [%f/%f]",key,location.latitude,location.longitude));
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("ERROR",""+error);
            }
        });
        //Mutiara Alai Homestay
        LatLng MutiaraAlai = new LatLng(2.177564, 102.309538);
        mMap.addCircle(new CircleOptions()
                .center(MutiaraAlai)
                .radius(500)//in metre
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f)
        );

        mMap.addMarker(new MarkerOptions().position(MutiaraAlai).title("Mutiara Alai Homestay")
                //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .snippet("Price: Rm120/night" )
        );


        //add geoQueryhere for Mutiara Alai Homestay
        //0.5f = 0.5km = 500m
        GeoQuery geoQueryMutiaraAlai = geoFire.queryAtLocation(new GeoLocation(MutiaraAlai.latitude,MutiaraAlai.longitude),0.5f);
        geoQueryMutiaraAlai.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                sendNotification("Malacca i-Homestay",String.format("%s you nearly reached to your homestay within 500m",key));
            }

            @Override
            public void onKeyExited(String key) {
                sendNotification("Malacca i-Homestay",String.format("%s you leaved the homestay.Thank you !",key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d("MOVE",String.format("%s moved within the dangerous area [%f/%f]",key,location.latitude,location.longitude));
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("ERROR",""+error);
            }
        });
        //Homestay Kampung Pak Ali
        LatLng PakAli = new LatLng(2.189682, 102.344681);
        mMap.addCircle(new CircleOptions()
                .center(PakAli)
                .radius(500)//in metre
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f)
        );

        mMap.addMarker(new MarkerOptions().position(PakAli).title("Homestay Kampung Pak Ali")
                //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .snippet("Price: Rm180/night" )
        );


        //add geoQueryhere for Homestay Kampung Pak Ali
        //0.5f = 0.5km = 500m
        GeoQuery geoQueryPakAli = geoFire.queryAtLocation(new GeoLocation(PakAli.latitude,PakAli.longitude),0.5f);
        geoQueryPakAli.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                sendNotification("Malacca i-Homestay",String.format("%s you nearly reached to your homestay within 500m",key));
            }

            @Override
            public void onKeyExited(String key) {
                sendNotification("Malacca i-Homestay",String.format("%s you leaved the homestay.Thank you !",key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d("MOVE",String.format("%s moved within the dangerous area [%f/%f]",key,location.latitude,location.longitude));
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("ERROR",""+error);
            }
        });
        //Homestay Sungai Udang
        LatLng SungaiUdang = new LatLng(2.292602, 102.143678);
        mMap.addCircle(new CircleOptions()
                .center(SungaiUdang)
                .radius(500)//in metre
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f)
        );

        mMap.addMarker(new MarkerOptions().position(SungaiUdang).title("Homestay Sungai Udang")
                //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .snippet("Price: Rm160/night" )
        );

        //add geoQueryhere for Homestay Sungai Udang
        //0.5f = 0.5km = 500m
        GeoQuery geoQuerySungaiUdang = geoFire.queryAtLocation(new GeoLocation(SungaiUdang.latitude,SungaiUdang.longitude),0.5f);
        geoQuerySungaiUdang.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                sendNotification("Malacca i-Homestay",String.format("%s you nearly reached to your homestay within 500m",key));
            }

            @Override
            public void onKeyExited(String key) {
                sendNotification("Malacca i-Homestay",String.format("%s you leaved the homestay.Thank you !",key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d("MOVE",String.format("%s moved within the dangerous area [%f/%f]",key,location.latitude,location.longitude));
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("ERROR",""+error);
            }
        });
        //Umri Homestay
        LatLng Umri = new LatLng(2.138932, 102.377557);
        mMap.addCircle(new CircleOptions()
                .center(Umri)
                .radius(500)//in metre
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f)
        );

        mMap.addMarker(new MarkerOptions().position(Umri).title("Umri Homestay")
                //.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .snippet("Price: Rm110/night" )
        );

        //add geoQueryhere for Umri
        //0.5f = 0.5km = 500m
        GeoQuery geoQueryUmri = geoFire.queryAtLocation(new GeoLocation(Umri.latitude,Umri.longitude),0.5f);
        geoQueryUmri.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                sendNotification("Malacca i-Homestay",String.format("%s you nearly reached to your homestay within 500m",key));
            }

            @Override
            public void onKeyExited(String key) {
                sendNotification("Malacca i-Homestay",String.format("%s you leaved the homestay.Thank you !",key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d("MOVE",String.format("%s moved within the dangerous area [%f/%f]",key,location.latitude,location.longitude));
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("ERROR",""+error);
            }
        });

        mMap.setOnInfoWindowClickListener(this);


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Dpinang));

    }



        @Override
        public void onInfoWindowClick ( final Marker marker){



    }


    private void sendNotification(String title, String content) {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content);
        NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,MapsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;

        manager.notify(new Random().nextInt(),notification);
        Toast.makeText(MapsActivity.this, "You are inside homestay area within 500m", Toast.LENGTH_LONG).show();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();

    }

    private void startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();

    }


}
