package com.edanyma.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.edanyma.AppConstants;
import com.edanyma.AppPreferences;
import com.edanyma.R;
import com.edanyma.manager.BasketOrderManager;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.AuthToken;
import com.edanyma.model.BootstrapModel;
import com.edanyma.model.ClientLocationModel;
import com.edanyma.model.LoginUser;
import com.edanyma.model.OurClientModel;
import com.edanyma.model.PreferenceBasket;
import com.edanyma.receiver.SingleShotLocationProvider;
import com.edanyma.rest.RestApi;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.GeoUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends Activity {

    private final String TAG = "SplashActivity";

    private static int SPLASH_TIME_OUT = 2000;

    static final int UNIQUE_PERMISSION_CODE = 100;

    private String mLatitude;
    private String mLongitude;

    private boolean mBootstrapSuccess;
    private boolean mPermissionGranted;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.splash_screen );
        initialize();
    }

    private void initialize() {
        if ( Build.VERSION.SDK_INT < 23 ) {
            initSplashLayout();
        } else {
            checkPermissions( UNIQUE_PERMISSION_CODE );
        }
    }

    private void checkPermissions( int code ) {
        mPermissionGranted = false;
        String[] permissions_required = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION };
        List permissions_not_granted_list = new ArrayList<>();
        for ( String permission : permissions_required ) {
            if ( ActivityCompat.checkSelfPermission( getApplicationContext(), permission ) != PackageManager.PERMISSION_GRANTED ) {
                permissions_not_granted_list.add( permission );
            }
        }
        if ( permissions_not_granted_list.size() > 0 ) {
            String[] permissions = new String[ permissions_not_granted_list.size() ];
            permissions_not_granted_list.toArray( permissions );
            ActivityCompat.requestPermissions( this, permissions, code );
        } else {
            initSplashLayout();
        }
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults ) {
        if ( requestCode == UNIQUE_PERMISSION_CODE ) {
            boolean ok = true;
            for ( int i = 0; i < grantResults.length; ++i ) {
                ok = ok && ( grantResults[ i ] == PackageManager.PERMISSION_GRANTED );
            }
            if ( ok ) {
                initSplashLayout();
            } else {
                Toast.makeText( this, "Error: required permissions not granted!", Toast.LENGTH_SHORT ).show();
                finish();
            }
        }
    }


    private void initSplashLayout() {
        mBootstrapSuccess = true;
        mPermissionGranted = true;
        final Context activity = this;
        ( ( TextView ) this.findViewById( R.id.splashTextOneId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) this.findViewById( R.id.splashTextTwoId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) this.findViewById( R.id.splashTextSmallOneId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) this.findViewById( R.id.splashTextSmallTwoId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) this.findViewById( R.id.splashTextSmallThreeId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );

        if ( !AppUtils.isNetworkAvailable() ) {
            finishActivity( "Отсутствует интернет соединение" );
        }
        SingleShotLocationProvider.requestSingleUpdate( activity,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable( SingleShotLocationProvider.GPSCoordinates location ) {
                        mLatitude = String.valueOf( location.latitude );
                        mLongitude = String.valueOf( location.longitude );
                        new FetchBootstrapData().execute();
                        new FetchClientLocation().execute();
                    }
                } );
    }


    private void finishActivity( String toastMsg ) {
        Toast.makeText( this, toastMsg, Toast.LENGTH_LONG ).show();
        new Handler().postDelayed( () -> {
                finish();
        }, SPLASH_TIME_OUT );
    }

    @Override
    public void onStart() {
        super.onStart();
        if ( !mPermissionGranted ) {
            Toast.makeText( this, "Error: required permissions not granted!", Toast.LENGTH_SHORT ).show();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private class FetchBootstrapData extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
//            if ( true ) return null;
            LoginUser loginUser = new LoginUser();
            Call< ApiResponse< AuthToken > > responseCall = RestController.getInstance().getApi().register( loginUser );
            try {
                Response< ApiResponse< AuthToken > > response = responseCall.execute();
                if ( response.body() != null ) {
                    ApiResponse< AuthToken > authToken = response.body();
                    GlobalManager.getInstance().setUserToken( authToken.getResult().getToken() );
                    Call< BootstrapModel > bootstrapCall = RestController.getInstance()
                            .getApi().fetchBootstrapData( AppConstants.AUTH_BEARER
                                            + GlobalManager.getInstance().getUserToken(),
                                    mLatitude, mLongitude );
                    Response< BootstrapModel > responseBootstrap = bootstrapCall.execute();
                    if ( responseBootstrap.body() != null ) {
                        GlobalManager.getInstance().setBootstrapModel( responseBootstrap.body() );
                        GlobalManager.getInstance().getClientLocation().setLatitude( Double.valueOf( mLatitude ) );
                        GlobalManager.getClientLocation().setLongitude( Double.valueOf( mLongitude ) );
                        if ( GlobalManager.getInstance().getBootstrapModel().getDeliveryCity() == null ) {
                            mBootstrapSuccess = false;
                        }
                        try{
                        Gson gson =  new Gson();
                        String jsonClient = AppPreferences.getPreference( AppConstants.OUR_CLIENT_PREF, null );
                        if(  jsonClient != null ){
                            OurClientModel client = gson.fromJson( jsonClient, OurClientModel.class );
                            GlobalManager.getInstance().setClient( client );
                        }

                            String jsonBasket = AppPreferences.getPreference( AppConstants.BASKET_PREF, null );
                            if ( jsonBasket != null ){
                                PreferenceBasket preferenceBasket = gson.fromJson( jsonBasket, PreferenceBasket.class );
                                BasketOrderManager.getInstance().setBasket( preferenceBasket );
                                BasketOrderManager.getInstance().sendMessageToActivity();
                            }
                        } catch ( Exception e ){
                            Log.e( TAG, "Can not get data, from preferences:"+e.getMessage() );
                            e.printStackTrace();
                        }
                    }
                }
            } catch ( Exception e ) {
                Log.i( TAG, e.getMessage() );
                e.printStackTrace();
                mBootstrapSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void result ) {
            super.onPostExecute( result );
            if ( mBootstrapSuccess ) {
                Intent intent = new Intent( SplashActivity.this, MainActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION );
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( intent );
                overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
                finish();
            } else {
                finishActivity( "Отсутствует соединение с сервером!" );
            }

        }
    }

    private class FetchClientLocation extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
            try {
                String geocode = mLongitude+","+mLatitude;
                Call< Object > locationCall = RestController.getInstance()
                        .getApi().getLocationAddress( RestApi.API_KEY, RestApi.FORMAT_JSON, geocode );
                Response< Object > locationResponse = locationCall.execute();
                if ( locationResponse.body() != null ) {
                    String source = locationResponse.body().toString();
                    ClientLocationModel clientLocationModel =  GlobalManager.getInstance().getClientLocation();
                    clientLocationModel.setCity( GeoUtils.getValueFromGeocoder( source, GeoUtils.CITY_KEY ) );
                    clientLocationModel.setStreet( GeoUtils.getValueFromGeocoder( source, GeoUtils.STREET_KEY ) );
                    clientLocationModel.setHouse( GeoUtils.getValueFromGeocoder( source, GeoUtils.HOUSE_KEY ) );
                }
            } catch ( Exception e ) {
                Log.e( TAG, e.getMessage() );
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void result ) {
            super.onPostExecute( result );
        }
    }



}
