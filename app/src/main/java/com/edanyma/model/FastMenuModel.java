package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.util.List;

@AutoProperty
public class FastMenuModel {

    private List<Integer> pizzaIds;
    private List<Integer> shushiIds;
    private List<Integer> burgerIds;
    private List<Integer> grillIds;
    private List<Integer> wokIds;

    public List< Integer > getPizzaIds() {
        return pizzaIds;
    }

    public void setPizzaIds( List< Integer > pizzaIds ) {
        this.pizzaIds = pizzaIds;
    }

    public List< Integer > getShushiIds() {
        return shushiIds;
    }

    public void setShushiIds( List< Integer > shushiIds ) {
        this.shushiIds = shushiIds;
    }

    public List< Integer > getBurgerIds() {
        return burgerIds;
    }

    public void setBurgerIds( List< Integer > burgerIds ) {
        this.burgerIds = burgerIds;
    }

    public List< Integer > getGrillIds() {
        return grillIds;
    }

    public void setGrillIds( List< Integer > grillIds ) {
        this.grillIds = grillIds;
    }

    public List< Integer > getWokIds() {
        return wokIds;
    }

    public void setWokIds( List< Integer > wokIds ) {
        this.wokIds = wokIds;
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
