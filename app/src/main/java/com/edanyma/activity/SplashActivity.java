package com.edanyma.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GloabalManager;
import com.edanyma.receiver.SingleShotLocationProvider;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 6000;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.splash_screen );
        ( ( TextView ) this.findViewById( R.id.splashTextOneId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) this.findViewById( R.id.splashTextTwoId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) this.findViewById( R.id.splashTextSmallOneId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) this.findViewById( R.id.splashTextSmallTwoId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) this.findViewById( R.id.splashTextSmallThreeId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
//        final Context activity = this;
//        SingleShotLocationProvider.requestSingleUpdate( activity,
//                new SingleShotLocationProvider.LocationCallback() {
//                    @Override
//                    public void onNewLocationAvailable( SingleShotLocationProvider.GPSCoordinates location ) {
//                        Intent intent = new Intent( SplashActivity.this, MainActivity.class );
//                        startActivity( intent );
//                        finish();
//                    }
//                } );

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                GloabalManager.getInstance().setDeliveryCity("Not Available");
                Intent intent = new Intent( SplashActivity.this, MainActivity.class );
                startActivity( intent );
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
