package com.edanyma.model;

import java.io.Serializable;

public class FilterDishModel implements Serializable {

    private String kitchenId;
    private String dishId;
    private String dishName;

    public FilterDishModel( String kitchenId, String dishId, String dishName ) {
        this.kitchenId = kitchenId;
        this.dishId = dishId;
        this.dishName = dishName;
    }

    public String getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId( String kitchenId ) {
        this.kitchenId = kitchenId;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId( String dishId ) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName( String dishName ) {
        this.dishName = dishName;
    }
}
