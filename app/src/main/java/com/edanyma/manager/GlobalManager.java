package com.edanyma.manager;

import com.edanyma.model.BootstrapModel;

public class GlobalManager {


    private static final GlobalManager GLOBAL_MANAGER = new GlobalManager();

    private static BootstrapModel bootstrapModel;
    private static String userToken;
    private static String userUUID;

    private GlobalManager() {}


    public static GlobalManager getInstance() {
        return GLOBAL_MANAGER;
    }

    public static BootstrapModel getBootstrapModel() {
        return bootstrapModel;
    }

    public static void setBootstrapModel( BootstrapModel bootstrapModel ) {
        GlobalManager.bootstrapModel = bootstrapModel;
    }

    public static String getUserToken() {
        return userToken;
    }

    public static void setUserToken( String userToken ) {
        GlobalManager.userToken = userToken;
    }

    public static String getUserUUID() {
        return userUUID;
    }

    public static void setUserUUID( String userUUID ) {
        GlobalManager.userUUID = userUUID;
    }
}
