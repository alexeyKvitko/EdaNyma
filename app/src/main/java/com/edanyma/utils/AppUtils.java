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
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.FavoriteCompanyModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class AppUtils {

    private static final String TAG = "AppUtils";

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

    public static String declensionDish( int count ) {
        String countSuffix = " блюд";
        String ending = "";
        int mod = count % 10;
        if ( count < 10 || count > 15 ) {
            if ( mod == 1 ) {
                ending = "о";
            }
            if ( mod == 2 || mod == 3 || mod == 4 ) {
                ending = "a";
            }
        }
        return count + countSuffix + ending;
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

            }

            @Override
            public void onAnimationEnd( Animation animation ) {
                sourceView.setVisibility( View.GONE );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {
            }
        } );
        targetView.setVisibility( View.VISIBLE );
        sourceView.startAnimation( fadeOut );
        targetView.startAnimation( fadeIn );
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

    public static void btnClickAnimation( final View view ) {
        if ( Build.VERSION.SDK_INT >= 26 ) {
            ( ( Vibrator ) EdaNymaApp.getAppContext().getSystemService( Context.VIBRATOR_SERVICE ) )
                    .vibrate( VibrationEffect.createOneShot( 50, 10 ) );
        } else {
            ( ( Vibrator ) EdaNymaApp.getAppContext().getSystemService( Context.VIBRATOR_SERVICE ) ).vibrate( 50 );
        }
        Animation rotation = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.icon_rotation );
        view.startAnimation( rotation );
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

    public static String formatDate( String format, Date date ) {
        SimpleDateFormat sdf = new SimpleDateFormat( format );
        String result = null;
        try {
            result = sdf.format(date);
        } catch ( Exception ex) {
            Log.e( TAG, "Ошибка преобразования: "+ex.getMessage() );
        }
        return result;
    }

    public static void showError(TextView view, String text, TextView ... hideViews){
        view.setText( text );
        view.setVisibility( View.VISIBLE );
        new Handler( ).postDelayed( ()->{
            for( TextView hideView: hideViews ){
                hideView.setVisibility( View.GONE );
            }
        },1000 );
    }


}
