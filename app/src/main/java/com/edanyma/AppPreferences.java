package com.edanyma;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public abstract class AppPreferences {
     
      private static final SharedPreferences SHARED_PREFERENCES 
                    = PreferenceManager.getDefaultSharedPreferences( EdaNymaApp.getAppContext() );
    
    /**
     *  Set Application Preferences
     *  @param key - key
     *  @param value - value
     */
    public static void setPreference( String key,Object value ){
        SharedPreferences.Editor editor = SHARED_PREFERENCES.edit();
        if ( value instanceof String ){
            editor.putString( key, (String) value );
        } else if ( value instanceof Float ){
            editor.putFloat(key, (Float) value);
        } else if ( value instanceof Integer ){
            editor.putInt(key, (Integer) value);
        } else {
            Gson gson =  new Gson();
            String jsonString = gson.toJson( value );
            editor.putString( key, jsonString );
        }
        editor.apply();
    }

    /**
     *  Get String Application Preferences
     *  @param key - key
     *  @param defValue - value
     */
    public static String getPreference( String key, String defValue ){
        return SHARED_PREFERENCES.getString( key, defValue );
    }

    /**
     *  Get float Application Preferences
     *  @param key - key
     *  @param defValue - value
     */
    public static float getPreference(  String key, double defValue ){
        return SHARED_PREFERENCES.getFloat(key, (float) defValue);
    }

    /**
     *  Get int Application Preferences
     *  @param key - key
     *  @param defValue - value
     */
    public static int getPreference( String key, int defValue ){
        return SHARED_PREFERENCES.getInt(key, defValue);
    }

    public static void removePreference(String key){
        SharedPreferences.Editor editor = SHARED_PREFERENCES.edit();
        editor.remove( key );
        editor.commit();
    }
    
    

}
