package com.edanyma.manager;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

public class MyLocationManager {

    private static final String TAG = "MyLocationManager";

    private static final long LOCATION_CHANGE_TIME = 100;
    private static final long LOCATION_CHANGE_DISTANCE = 3;

    public static final String ACTION_LOCATION = "com.edanyma.manager.ACTION_LOCATION";

    private static MyLocationManager sMyLocationManager;
    private Context mAppContext;
    private LocationManager mLocationManager;

    // Закрытый конструктор заставляет использовать
    // MyLocationManager.getInstance(Context)
    private MyLocationManager( Context appContext ) {
        mAppContext = appContext;
        mLocationManager = (LocationManager) mAppContext.getSystemService( Context.LOCATION_SERVICE );
    }

    public static MyLocationManager getInstance( Context c ) {
        if ( sMyLocationManager == null ) {
            sMyLocationManager = new MyLocationManager( c.getApplicationContext() );
        }
        return sMyLocationManager;
    }

    private PendingIntent getLocationPendingIntent( boolean shouldCreate ) {
        Intent broadcast = new Intent( ACTION_LOCATION );
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast( mAppContext, 0, broadcast, flags );
    }

    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;
        if ( ActivityCompat.checkSelfPermission( mAppContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission( mAppContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }
        Location lastKnown = mLocationManager.getLastKnownLocation( provider );
        if ( lastKnown != null ) {
            lastKnown.setTime( System.currentTimeMillis() );
            broadcastLocation( lastKnown );
        }
        PendingIntent pendingIntent = getLocationPendingIntent( true );
        if ( ActivityCompat.checkSelfPermission( mAppContext,
                Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( mAppContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }
        mLocationManager.requestLocationUpdates( provider, LOCATION_CHANGE_TIME, LOCATION_CHANGE_DISTANCE, pendingIntent );
    }

    public void stopLocationUpdates() {
        PendingIntent pendingIntent = getLocationPendingIntent( false );
        if ( pendingIntent != null ) {
            mLocationManager.removeUpdates( pendingIntent );
            pendingIntent.cancel();
        }
    }

    private void broadcastLocation( Location location ) {
        Intent broadcast = new Intent( ACTION_LOCATION );
        broadcast.putExtra( LocationManager.KEY_LOCATION_CHANGED, location );
        mAppContext.sendBroadcast( broadcast );
    }

    public boolean isTrackingRun() {
        return getLocationPendingIntent( false ) != null;
    }
}
