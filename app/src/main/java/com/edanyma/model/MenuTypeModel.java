package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.util.List;

@AutoProperty
public class MenuTypeModel extends DictionaryModel{

    private List<MenuCategoryModel> menuCategories;
    private boolean menuOpen;

    public List< MenuCategoryModel > getMenuCategories() {
        return menuCategories;
    }

    public void setMenuCategories( List< MenuCategoryModel > menuCategories ) {
        this.menuCategories = menuCategories;
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    public void setMenuOpen( boolean menuOpen ) {
        this.menuOpen = menuOpen;
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
