package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.util.List;

@AutoProperty
public class BasketModel {

    private CompanyModel company;
    private List<MenuEntityModel> basket;
    private boolean orderPosible;
    private Integer price;

    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany( CompanyModel company ) {
        this.company = company;
    }

    public List< MenuEntityModel > getBasket() {
        return basket;
    }

    public void setBasket( List< MenuEntityModel > basket ) {
        this.basket = basket;
    }

    public boolean isOrderPosible() {
        return orderPosible;
    }

    public void setOrderPosible( boolean orderPosible ) {
        this.orderPosible = orderPosible;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice( Integer price ) {
        this.price = price;
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
