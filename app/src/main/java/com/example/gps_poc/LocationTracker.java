package com.example.gps_poc;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.Console;

import static android.content.Context.LOCATION_SERVICE;

public class LocationTracker extends Service implements LocationListener {

    private final Context mContext;


    boolean checkGPS = false;


    boolean checkNetwork = false;

    boolean canGetLocation = false;

    Location loc;
    double latitude;
    double longitude;


    // in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    // time in ms
    private static final long MIN_TIME_BW_UPDATES = 1000 * 2;
    protected LocationManager locationManager;

    public LocationTracker(Context mContext)
    {
        this.mContext = mContext;
        getLocation();
    }

    protected void finalize()
    {
        System.out.println("destroyed!!!");
    }

    private Location getLocation()
    {
        System.out.println("getting location...");

        try
        {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            if(locationManager == null)
            {
                System.out.println("NULL locationManager");
                return null;
            }

            // get GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network provider status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS || !checkNetwork)
            {
                Toast.makeText(mContext, "No Service Provider is available", Toast.LENGTH_SHORT).show();
            }
            else
            {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS)
                {

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

                    {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            //LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    loc = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (loc != null)
                    {
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();
                    }
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("we got loc!!!");
        return loc;
    }

    public void makeRequest()
    {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                //LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    }

    public double getLongitude() {
        if (loc != null)
        {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null)
        {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        System.out.println("alert");
        alertDialog.setTitle("GPS is not Enabled!");

        alertDialog.setMessage("Do you want to turn on GPS?");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }


    public void stopListener()
    {
        System.out.println("stop listener");
        if (locationManager != null)
        {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(LocationTracker.this);
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        System.out.println("onBind");
        return null;
    }

    @Override
    public void onLocationChanged(Location location)
    {
          if (location == null)
          {
              System.out.println("onLocationChanged null!");
          }
          System.out.println("onLocationChanged");
          Toast.makeText(mContext, "location - longitude: " + location.getLongitude() + " , latitude: " + location.getLatitude() + " , altitude: " + location.getAltitude(), Toast.LENGTH_SHORT).show();
          System.out.println("location - longitude: " + location.getLongitude() + " , latitude: " + location.getLatitude() + " , altitude: " + location.getAltitude());

          if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                  && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
          {
              System.out.println("onLocationChanged location");
              return;
          }

          System.out.println("onLocationChanged locationManager");
          locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                //LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle)
    {
        System.out.println("onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s)
    {
        System.out.println("onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s)
    {
        System.out.println("onProviderDisabled");
    }
}

