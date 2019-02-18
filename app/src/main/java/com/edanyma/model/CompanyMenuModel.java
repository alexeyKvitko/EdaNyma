package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.util.List;

@AutoProperty
public class CompanyMenuModel {

    private List<MenuEntityModel> menuEntities;

    public List< MenuEntityModel > getMenuEntities() {
        return menuEntities;
    }

    public void setMenuEntities( List< MenuEntityModel > menuEntities ) {
        this.menuEntities = menuEntities;
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
