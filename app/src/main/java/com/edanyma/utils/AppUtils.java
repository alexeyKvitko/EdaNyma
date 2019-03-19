package com.edanyma.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;

import java.util.regex.Pattern;

public abstract class AppUtils {

    private static boolean isAnimated;

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                EdaNymaApp.getAppContext().getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String declension( String sourceVal ){
        int count = Integer.valueOf( sourceVal );
        String countSuffix = " заведени";
        String ending="й";
        int mod = count % 10;
        if ( count < 10 || count > 15 ){
            if( mod == 1 ){
                ending = "е";
            }
            if( mod == 2 || mod == 3 || mod == 4){
                ending = "я";
            }
        }

        return count+countSuffix+ending ;
    }

    public static boolean validateEmail( String email ) {
        Pattern pattern = Pattern.compile( AppConstants.EMAIL_PATTERN );
        return pattern.matcher( email ).matches();
    }

    public static boolean validatePhone( String source ) {
        String phone = source.replaceAll(" ","").replace("(","").replace( ")","" )
                .replace( "+","" ).replaceAll( "-","" );
        Pattern pattern = Pattern.compile( AppConstants.PHONE_PATTERN );
        return pattern.matcher( phone ).matches();
    }

    public static void transitionAnimation( final View sourceView, final View targetView ){
        Animation fadeIn = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_in );
        Animation fadeOut = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_out );
        fadeIn.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {
                targetView.setVisibility( View.VISIBLE );
            }

            @Override
            public void onAnimationEnd( Animation animation ) {
                sourceView.setVisibility( View.GONE );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {}
        } );

        sourceView.startAnimation(  fadeOut );
        targetView.startAnimation(  fadeIn );
    }

    public static void clickAnimation( final View view ) {
        Animation bounce = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.short_bounce );
        view.startAnimation( bounce );
    }

    public static void bounceAnimation( final View view ) {
        Animation bounce = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.bounce );
        view.startAnimation( bounce );
        if ( Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) EdaNymaApp.getAppContext().getSystemService(Context.VIBRATOR_SERVICE))
                    .vibrate( VibrationEffect.createOneShot(50,10));
        } else {
            ((Vibrator) EdaNymaApp.getAppContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
        }
    }

    public static int getRandomBetweenRange( int min, int max ) {
        Double x = ( Math.random( ) * ( ( max - min ) + 1 ) ) + min;
        return x.intValue( );
    }
}
