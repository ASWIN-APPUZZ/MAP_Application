package com.example.mapapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView loc;
    Button show, getloc, loclist;

    LocationManager locationManager;
    String lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loc = findViewById(R.id.tv_location);
        show = findViewById(R.id.btn_show);
        getloc = findViewById(R.id.btn_get_cur_loc);
        loclist = findViewById(R.id.btn_loc_lisner);

        //List the providers available
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(new Criteria(), false);

        String provider = "";
        for (String pro : providers) {
            provider += pro + "\n";
        }
        Toast.makeText(getApplicationContext(), provider, Toast.LENGTH_SHORT).show();

        //Show Map
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        getloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!providerEnabled) {
                    EnableGPS();
                } else
                    GetLocation();
            }
        });
    }

    private void GetLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] prmison = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
            ActivityCompat.requestPermissions(this, prmison, 1);

        }else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (!location.equals(null)){
                lat = location.getLatitude()+"";
                lng = location.getLongitude()+"";
                loc.setText("Latituide : "+lat+"\n Longituide : "+lng);
            }
        }
    }

    private void EnableGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable Gps").setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}