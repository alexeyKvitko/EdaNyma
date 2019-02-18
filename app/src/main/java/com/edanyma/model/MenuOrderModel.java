package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class MenuOrderModel {

    private Integer id;
    private Integer companyId;
    private Integer menuTypeId;
    private Integer menuCategoryId;
    private Integer order;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId( Integer companyId ) {
        this.companyId = companyId;
    }

    public Integer getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId( Integer menuTypeId ) {
        this.menuTypeId = menuTypeId;
    }

    public Integer getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId( Integer menuCategoryId ) {
        this.menuCategoryId = menuCategoryId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder( Integer order ) {
        this.order = order;
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
