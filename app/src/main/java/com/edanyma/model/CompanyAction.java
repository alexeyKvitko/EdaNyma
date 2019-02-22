package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class CompanyAction {

    private String companyName;
    private int drawableId;

    public CompanyAction(){}

    public CompanyAction( String companyName, int drawableId ) {
        this.companyName = companyName;
        this.drawableId = drawableId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId( int drawableId ) {
        this.drawableId = drawableId;
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
