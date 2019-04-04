package com.edanyma.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;

import java.io.File;
import java.util.regex.Pattern;

public abstract class AppUtils {

    private static boolean isAnimated;

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = ( ConnectivityManager )
                EdaNymaApp.getAppContext().getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isStorageReady() {
        return Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState() );
    }


    public static String declension( String sourceVal ) {
        int count = Integer.valueOf( sourceVal );
        String countSuffix = " заведени";
        String ending = "й";
        int mod = count % 10;
        if ( count < 10 || count > 15 ) {
            if ( mod == 1 ) {
                ending = "е";
            }
            if ( mod == 2 || mod == 3 || mod == 4 ) {
                ending = "я";
            }
        }
        return count + countSuffix + ending;
    }

    public static String declensionFilter( int count ) {
        String countSuffix = " фильтр";
        String ending = "ам";
        int mod = count % 10;
        if ( mod == 1 ) {
            ending = "у";
        }
        return " " + count + countSuffix + ending;
    }

    public static boolean validateEmail( String email ) {
        Pattern pattern = Pattern.compile( AppConstants.EMAIL_PATTERN );
        return pattern.matcher( email ).matches();
    }

    public static boolean validatePhone( String source ) {
        String phone = source.replaceAll( " ", "" ).replace( "(", "" ).replace( ")", "" )
                .replace( "+", "" ).replaceAll( "-", "" );
        Pattern pattern = Pattern.compile( AppConstants.PHONE_PATTERN );
        return pattern.matcher( phone ).matches();
    }


    public static void transitionAnimation( final View sourceView, final View targetView ) {
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
            public void onAnimationRepeat( Animation animation ) {
            }
        } );

        sourceView.startAnimation( fadeOut );
        targetView.startAnimation( fadeIn );
    }

    public static void showToastWithAnimation( final View toastView, String titleMsg, String bodyMsg ) {
        TextView toastTitle = toastView.findViewById( R.id.toastTitleId );
        TextView toastText = toastView.findViewById( R.id.toastTextId );
        toastTitle.setTypeface( AppConstants.B52 );
        toastTitle.setText( titleMsg );
        toastText.setTypeface( AppConstants.ROBOTO_CONDENCED );
        toastText.setText( bodyMsg );
        toastView.startAnimation( AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_in ) );
        toastView.setVisibility( View.VISIBLE );
        ( new Handler() ).postDelayed( new Runnable() {
            @Override
            public void run() {
                Animation fadeOut = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_out );
                fadeOut.setAnimationListener( new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart( Animation animation ) {
                    }

                    @Override
                    public void onAnimationEnd( Animation animation ) {
                        toastView.setVisibility( View.GONE );
                    }

                    @Override
                    public void onAnimationRepeat( Animation animation ) {
                    }
                } );
                toastView.startAnimation( fadeOut );
            }
        }, 2000 );
    }

    public static void clickAnimation( final View view ) {
        if ( Build.VERSION.SDK_INT >= 26 ) {
            ( ( Vibrator ) EdaNymaApp.getAppContext().getSystemService( Context.VIBRATOR_SERVICE ) )
                    .vibrate( VibrationEffect.createOneShot( 50, 10 ) );
        } else {
            ( ( Vibrator ) EdaNymaApp.getAppContext().getSystemService( Context.VIBRATOR_SERVICE ) ).vibrate( 50 );
        }
        Animation bounce = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.short_bounce );
        view.startAnimation( bounce );

    }

    public static void bounceAnimation( final View view ) {
        Animation bounce = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.bounce );
        view.startAnimation( bounce );
        if ( Build.VERSION.SDK_INT >= 26 ) {
            ( ( Vibrator ) EdaNymaApp.getAppContext().getSystemService( Context.VIBRATOR_SERVICE ) )
                    .vibrate( VibrationEffect.createOneShot( 50, 10 ) );
        } else {
            ( ( Vibrator ) EdaNymaApp.getAppContext().getSystemService( Context.VIBRATOR_SERVICE ) ).vibrate( 50 );
        }
    }

    public static int getRandomBetweenRange( int min, int max ) {
        Double x = ( Math.random() * ( ( max - min ) + 1 ) ) + min;
        return x.intValue();
    }

    public static void hideKeyboardFrom( Context context, View view ) {
        InputMethodManager imm = ( InputMethodManager ) context.getSystemService( Activity.INPUT_METHOD_SERVICE );
        if ( imm.isAcceptingText() ) {
            imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
        }
    }

    public static boolean nullOrEmpty( String value ) {
        return value == null || ( value != null && value.trim().length() == 0 );
    }

    public static String getSnapshotPath(){
        return Environment.getExternalStorageDirectory()+File.separator+AppConstants.PICTURE_DIR
                    +File.separator+AppConstants.FILENAME_DISH + AppConstants.EXTENSION_JPG;
    }
}
