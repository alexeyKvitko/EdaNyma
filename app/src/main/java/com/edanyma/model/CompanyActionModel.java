package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class CompanyActionModel {


    private String companyName;
    private String actionImgUrl;
//    936х1680
    private String fullScreenAction;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public String getActionImgUrl() {
        return actionImgUrl;
    }

    public void setActionImgUrl( String actionImgUrl ) {
        this.actionImgUrl = actionImgUrl;
    }

    public String getFullScreenAction() {
        return fullScreenAction;
    }

    public void setFullScreenAction( String fullScreenAction ) {
        this.fullScreenAction = fullScreenAction;
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
