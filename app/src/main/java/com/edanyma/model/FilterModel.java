package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.util.List;

@AutoProperty
public class FilterModel {

    List<String> dishesId;
    List<String> kitchenId;
    List<String> payTypes;
    List<String> extraFilters;

    public List< String > getDishesId() {
        return dishesId;
    }

    public void setDishesId( List< String > dishesId ) {
        this.dishesId = dishesId;
    }

    public List< String > getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId( List< String > kitchenId ) {
        this.kitchenId = kitchenId;
    }

    public List< String > getPayTypes() {
        return payTypes;
    }

    public void setPayTypes( List< String > payTypes ) {
        this.payTypes = payTypes;
    }

    public List< String > getExtraFilters() {
        return extraFilters;
    }

    public void setExtraFilters( List< String > extraFilters ) {
        this.extraFilters = extraFilters;
    }

    @Override public boolean equals( Object o) {
        return Pojomatic.equals(this, o);
    }

    @Override public int hashCode() {
        return Pojomatic.hashCode(this);
    }

    @Override public String toString() {
        return Pojomatic.toString(this);
    }
}
