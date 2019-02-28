package com.edanyma.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.edanyma.AppConstants;
import com.edanyma.R;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.ApiResponse;
import com.edanyma.model.AuthToken;
import com.edanyma.model.BootstrapModel;
import com.edanyma.model.LoginUser;
import com.edanyma.receiver.SingleShotLocationProvider;
import com.edanyma.rest.RestController;
import com.edanyma.utils.AppUtils;
import com.google.gson.internal.LinkedTreeMap;

import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends Activity {

    private final String TAG = "SplashActivity";

    private static int SPLASH_TIME_OUT = 2000;

    private String mLatitude;
    private String mLongitude;

    private boolean mBootstrapSuccess;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.splash_screen );
        ( ( TextView ) this.findViewById( R.id.splashTextOneId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) this.findViewById( R.id.splashTextTwoId ) ).setTypeface( AppConstants.B52 );
        ( ( TextView ) this.findViewById( R.id.splashTextSmallOneId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) this.findViewById( R.id.splashTextSmallTwoId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        ( ( TextView ) this.findViewById( R.id.splashTextSmallThreeId ) ).setTypeface( AppConstants.ROBOTO_CONDENCED );
        final Context activity = this;
        mBootstrapSuccess = true;
        SingleShotLocationProvider.requestSingleUpdate( activity,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable( SingleShotLocationProvider.GPSCoordinates location ) {
                        mLatitude = String.valueOf( location.latitude );
                        mLongitude = String.valueOf( location.longitude );
                        new FetchBootstrapData().execute();
                    }
                } );
        if ( !AppUtils.isNetworkAvailable() ) {
            finishActivity( "Отсутствует интернет соединение" );
        }
    }


    private void finishActivity( String toastMsg ) {
        Toast.makeText( this, toastMsg, Toast.LENGTH_LONG ).show();
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, SPLASH_TIME_OUT );
    }


    private class FetchBootstrapData extends AsyncTask< Void, Void, Void > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground( Void... arg0 ) {
            if ( true ) return null;
            LoginUser loginUser = new LoginUser();
            Call< ApiResponse< AuthToken > > responseCall = RestController.getInstance().getApi().register( loginUser );
            try {
                Response< ApiResponse< AuthToken > > response = responseCall.execute();
                if ( response.body() != null ) {
                    ApiResponse< AuthToken > authToken = response.body();
                    GlobalManager.getInstance().setUserToken( ( String ) ( ( LinkedTreeMap ) authToken
                            .getResult() ).get( "token" ) );
                    Call< BootstrapModel > bootstrapCall = RestController.getInstance()
                            .getApi().fetchBootstrapData( AppConstants.AUTH_BEARER
                                            + GlobalManager.getInstance().getUserToken(),
                                    mLatitude, mLongitude );
                    Response< BootstrapModel > responseBootstrap = bootstrapCall.execute();
                    if ( responseBootstrap.body() != null ) {
                        GlobalManager.getInstance().setBootstrapModel( responseBootstrap.body() );
                    }
                }
                if ( GlobalManager.getInstance().getBootstrapModel().getDeliveryCity() == null ) {
                    mBootstrapSuccess = false;
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

    @Override
    public void onBackPressed() {
        finish();
    }

}
