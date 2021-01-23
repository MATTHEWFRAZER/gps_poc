package com.example.gps_poc;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import static com.example.gps_poc.MainActivity.CHANNEL_ID;


public class LocationTrackerService  extends Service
{
    private LocationTracker locationTracker;

   @Override
   public void onCreate()
   {
       super.onCreate();

       Intent notificationIntent = new Intent(this, LocationTrackerService.class);
       PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
       Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
               .setContentTitle("GPS_POC")
               .setContentText("hi")
               .setSmallIcon(R.drawable.ic_home_black_24dp)
               .setContentIntent(pendingIntent)
               .build();
       startForeground(1, notification);
       System.out.println("Location Tracker Service onCreate");
       locationTracker = new LocationTracker(LocationTrackerService.this);
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId)
   {
       super.onStartCommand(intent, flags, startId);
       System.out.println("Location Tracker Service onStartCommand");
       //return START_NOT_STICKY;
       return START_STICKY;
   }

    @Override
    public IBinder onBind(Intent intent)
    {
        System.out.println("Location Tracker Service onBind");
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        System.out.println("Location Tracker Service onDestroy");
        locationTracker.stopListener();
    }
}
