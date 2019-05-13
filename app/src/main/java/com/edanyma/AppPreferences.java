package com.edanyma;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class AppPreferences {

    /**
     *  Set Application Preferences
     *  @param key - key
     *  @param value - value
     */
    public static void setPreference( String key,Object value ){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( EdaNymaApp.getAppContext() );
        SharedPreferences.Editor editor = prefs.edit();
        if ( value instanceof String ){
            editor.putString( key, (String) value );
        } else if ( value instanceof Float ){
            editor.putFloat(key, (Float) value);
        } else if ( value instanceof Integer ){
            editor.putInt(key, (Integer) value);
        }
        editor.apply();
    }

    /**
     *  Get String Application Preferences
     *  @param key - key
     *  @param defValue - value
     */
    public static String getPreference( String key, String defValue ){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( EdaNymaApp.getAppContext() );
        return prefs.getString( key, defValue );
    }

    /**
     *  Get float Application Preferences
     *  @param key - key
     *  @param defValue - value
     */
    public static float getPreference(  String key, double defValue ){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( EdaNymaApp.getAppContext() );
        return prefs.getFloat(key, (float) defValue);
    }

    /**
     *  Get int Application Preferences
     *  @param key - key
     *  @param defValue - value
     */
    public static int getPreference( String key, int defValue ){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( EdaNymaApp.getAppContext() );
        return prefs.getInt(key, defValue);
    }

}
