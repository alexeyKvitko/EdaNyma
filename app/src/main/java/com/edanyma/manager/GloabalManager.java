package com.edanyma.manager;

public class GloabalManager {


    private static final GloabalManager gloabalManager = new GloabalManager();

    private static String deliveryCity;

    private GloabalManager() {}


    public static GloabalManager getInstance() {
        return gloabalManager;
    }

    public static String getDeliveryCity() {
        return deliveryCity;
    }

    public static void setDeliveryCity( String deliveryCity ) {
        GloabalManager.deliveryCity = deliveryCity;
    }


}
