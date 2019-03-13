package com.edanyma.manager;

import com.edanyma.model.BootstrapModel;
import com.edanyma.model.OurClientModel;

public class GlobalManager {


    private static final GlobalManager GLOBAL_MANAGER = new GlobalManager();

    private static BootstrapModel bootstrapModel;
    private static String userToken;
    private static OurClientModel client;

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

    public static OurClientModel getClient() {
        return client;
    }

    public static void setClient( OurClientModel client ) {
        GlobalManager.client = client;
    }
}
