package com.ayushgupta2959.smsemailsendingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button sms, email, fetch,ok;
    EditText editTextEmail;
    EditText editTextNumber;
    Location location;
    String latitude;
    String longitude;
    LocationManager locationManager;
    MyLocationListener locationListener;
    Criteria criteria;
    String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sms = (Button) findViewById(R.id.sms);
        email = (Button) findViewById(R.id.email);
        fetch = (Button) findViewById(R.id.fetchlocation);
        editTextEmail = (EditText)findViewById(R.id.edittextemail);
        editTextEmail.setText("Enter Email");
        editTextNumber = (EditText)findViewById(R.id.edittextnumber);
        editTextNumber.setText("Enter Number");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria,false);
        locationListener = new MyLocationListener();
        fetch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,200,1,locationListener);
                latitude = locationListener.latitude;
                longitude = locationListener.longitude;
                Toast.makeText(MainActivity.this, "Latitude : " +latitude + "\n" + "Longitude : " + longitude , Toast.LENGTH_SHORT).show();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] to = new String[1];
                to[0] = editTextEmail.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL,to);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Location");
                intent.putExtra(Intent.EXTRA_TEXT,"Latitude : " +latitude + "\n" + "Longitude : " + longitude);
                startActivity(Intent.createChooser(intent,"Send Email"));
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] to = new String[1];
                to[0] = editTextNumber.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(to[0],null,"Latitude : " +latitude + "\n" + "Longitude : " + longitude,null,null);
                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
class MyLocationListener implements LocationListener{
    String latitude;
    String longitude;
    @Override
    public void onLocationChanged(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}