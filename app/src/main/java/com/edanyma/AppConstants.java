package com.edanyma;

import android.graphics.Typeface;

public class AppConstants {

    public static final Typeface ROBOTO_REGULAR = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/Roboto-Regular.ttf");
    public static final Typeface ROBOTO_THIN = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/Roboto-Thin.ttf");
    public static final Typeface ROBOTO_BLACK = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/Roboto-Black.ttf");
    public static final Typeface ROBOTO_CONDENCED = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/RobotoCondensed.ttf");
    public static final Typeface B52 = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/B52.otf");
    public static final Typeface LOBSTER = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/lobster.ttf");
    public static final Typeface SANDORA = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/sandora.ttf");
    public static final Typeface TAURUS = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/taurus.ttf");
    public static final Typeface TAURUS_BOLD = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/taurus_bold.ttf");

    public static final String DEFAULT_USER = "guest";
    public static final String DEFAULT_PASSWORD = "1111";

    public static final String AUTH_BEARER = "Bearer ";
}
