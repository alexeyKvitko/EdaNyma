package com.edanyma.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.edanyma.EdaNymaApp;

public abstract class AppUtils {

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                EdaNymaApp.getAppContext().getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
