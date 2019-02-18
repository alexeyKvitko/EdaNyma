package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.util.List;

@AutoProperty
public class CompanyEditModel {

    private List<MenuOrderModel> menuOrders;
    private List<MenuTypeModel> deliveryMenuTypes;
    private List<MenuCategoryModel> deliveryMenuCategories;
    private List<DictionaryModel> cities;

    public List< MenuOrderModel > getMenuOrders() {
        return menuOrders;
    }

    public void setMenuOrders( List< MenuOrderModel > menuOrders ) {
        this.menuOrders = menuOrders;
    }

    public List< MenuTypeModel > getDeliveryMenuTypes() {
        return deliveryMenuTypes;
    }

    public void setDeliveryMenuTypes( List< MenuTypeModel > deliveryMenuTypes ) {
        this.deliveryMenuTypes = deliveryMenuTypes;
    }

    public List< MenuCategoryModel > getDeliveryMenuCategories() {
        return deliveryMenuCategories;
    }

    public void setDeliveryMenuCategories( List< MenuCategoryModel > deliveryMenuCategories ) {
        this.deliveryMenuCategories = deliveryMenuCategories;
    }

    public List< DictionaryModel > getCities() {
        return cities;
    }

    public void setCities( List< DictionaryModel > cities ) {
        this.cities = cities;
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
