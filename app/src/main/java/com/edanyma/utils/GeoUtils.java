package com.edanyma.utils;

import com.edanyma.AppConstants;

public abstract class GeoUtils {

    public static final String CITY_KEY = "kind=locality, name=";
    public static final String STREET_KEY = "kind=street, name=";
    public static final String HOUSE_KEY = "kind=house, name=";

    public static String getValueFromGeocoder( String source, String key ){
        String value = null;
        int startPos = source.indexOf( key );
        if( AppConstants.FAKE_ID != startPos ){
            String leftover = source.substring( startPos );
            int endPos = leftover.indexOf( "}" );
            if ( AppConstants.FAKE_ID != endPos ){
                value = leftover.substring( key.length(),endPos );
            }
        }
        return value;
    }
}
