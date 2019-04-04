package com.edanyma.model;

import java.io.Serializable;
import java.util.List;

public class CompanyMenu implements Serializable {

    private List<MenuTypeModel> companyMenus;

    public CompanyMenu( List< MenuTypeModel > companyMenus ) {
        this.companyMenus = companyMenus;
    }

    public List< MenuTypeModel > getCompanyMenus() {
        return companyMenus;
    }

    public void setCompanyMenus( List< MenuTypeModel > companyMenus ) {
        this.companyMenus = companyMenus;
    }
}
