package com.edanyma.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class PrefernceBasket implements Serializable {

    private  List< MenuEntityModel > sharedPreferenceBasket;

    public PrefernceBasket( List< MenuEntityModel > clientBasket ) {
        this.sharedPreferenceBasket = new LinkedList<>(  );
        for( MenuEntityModel menuEntityModel: clientBasket ){
            this.sharedPreferenceBasket.add( menuEntityModel );
        }
    }

    public List< MenuEntityModel > get() {
        return sharedPreferenceBasket;
    }

}
