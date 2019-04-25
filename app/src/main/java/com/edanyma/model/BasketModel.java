package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.util.LinkedList;
import java.util.List;

@AutoProperty
public class BasketModel {

    private CompanyModel company;
    private List<MenuEntityModel> basket;
    private boolean orderPosible;
    private Integer price;

    public BasketModel( ) {
        this.basket = new LinkedList<>(  );
    }

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
        return this.getPrice() >= this.company.getDelivery();
    }

    public Integer getPrice() {
        Integer price = 0;
        for(MenuEntityModel dish: this.getBasket() ){
            price += (dish.getActualPrice()*dish.getCount() );
        }
        return price;
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
