package com.edanyma.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;

import java.util.regex.Pattern;

public abstract class AppUtils {

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
}
