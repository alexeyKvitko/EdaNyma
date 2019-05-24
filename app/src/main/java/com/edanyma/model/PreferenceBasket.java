package com.edanyma.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class PreferenceBasket implements Serializable {

    private  List< MenuEntityModel > sharedPreferenceBasket;

    public PreferenceBasket( List< MenuEntityModel > clientBasket ) {
        this.sharedPreferenceBasket = new LinkedList<>(  );
        for( MenuEntityModel menuEntityModel: clientBasket ){
            this.sharedPreferenceBasket.add( menuEntityModel );
        }
    }

    public PreferenceBasket get() {
        return this;
    }

    public List< MenuEntityModel > getSharedPreferenceBasket() {
        return sharedPreferenceBasket;
    }
}
