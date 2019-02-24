package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class CompanyAction {

    private String companyName;
    private String actionImg;

    public CompanyAction(){}

    public CompanyAction(String companyName, String actionImg) {
        this.companyName = companyName;
        this.actionImg = actionImg;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public String getActionImg() {
        return actionImg;
    }

    public void setActionImg(String actionImg) {
        this.actionImg = actionImg;
    }

    @Override public boolean equals(Object o) {
        return Pojomatic.equals(this, o);
    }

    @Override public int hashCode() {
        return Pojomatic.hashCode(this);
    }

    @Override public String toString() {
        return Pojomatic.toString(this);
    }
}
