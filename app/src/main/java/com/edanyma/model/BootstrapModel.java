package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.util.List;

@AutoProperty
public class BootstrapModel {

    private List<CompanyModel> companies;
    private DeliveryMenuModel deliveryMenu;
    private List<DictionaryModel> cities;
    private List<CompanyActionModel> companyActions;
    private FastMenuModel fastMenu;
    private String deliveryCity;
    private boolean isDefault;
    private String staticUrl;

    public List< CompanyModel > getCompanies() {
        return companies;
    }

    public void setCompanies( List< CompanyModel > companies ) {
        this.companies = companies;
    }

    public DeliveryMenuModel getDeliveryMenu() {
        return deliveryMenu;
    }

    public void setDeliveryMenu( DeliveryMenuModel deliveryMenu ) {
        this.deliveryMenu = deliveryMenu;
    }

    public List< DictionaryModel > getCities() {
        return cities;
    }

    public void setCities( List< DictionaryModel > cities ) {
        this.cities = cities;
    }

    public FastMenuModel getFastMenu() {
        return fastMenu;
    }

    public void setFastMenu( FastMenuModel fastMenu ) {
        this.fastMenu = fastMenu;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity( String deliveryCity ) {
        this.deliveryCity = deliveryCity;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault( boolean aDefault ) {
        isDefault = aDefault;
    }

    public List< CompanyActionModel > getCompanyActions() {
        return companyActions;
    }

    public void setCompanyActions( List< CompanyActionModel > companyActions ) {
        this.companyActions = companyActions;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl( String staticUrl ) {
        this.staticUrl = staticUrl;
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
