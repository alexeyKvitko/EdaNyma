package com.edanyma;

import android.app.Application;
import android.content.Context;

public class EdaNymaApp extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        EdaNymaApp.context = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getAppContext() {
        return EdaNymaApp.context;
    }
}