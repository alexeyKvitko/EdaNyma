package com.edanyma.model;

import com.edanyma.AppConstants;
import com.edanyma.R;

import java.io.Serializable;

public class ActivityState implements Serializable {

    private int mBottomMenuIndex;
    private int drawerMenuIndex;
    private int selectedBottomId;
    private int selectedDrawerId;

    public ActivityState( int bottomMenuIndex ) {
        this.mBottomMenuIndex = bottomMenuIndex;
        switch ( bottomMenuIndex ){
            case AppConstants.COMPANY_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.COMPANY_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_company;
                this.selectedDrawerId = R.id.nav_all_company;
                break;
            case AppConstants.DISH_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.DISH_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_dish;
                this.selectedDrawerId = R.id.nav_all_dish;
                break;
            case AppConstants.HOME_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.HOME_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_home;
                this.selectedDrawerId = R.id.nav_home;
                break;
            case AppConstants.BASKET_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.BASKET_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_basket;
                this.selectedDrawerId = R.id.nav_basket;
                break;
            case AppConstants.LOGIN_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.LOGIN_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_login;
                this.selectedDrawerId =  R.id.drawerLoginId;
                break;

        }
    }

    public int getBottomMenuIndex() {
        return mBottomMenuIndex;
    }

    public int getDrawerMenuIndex() {
        return drawerMenuIndex;
    }

    public int getSelectedBottomId() {
        return selectedBottomId;
    }

    public int getSelectedDrawerId() {
        return selectedDrawerId;
    }
}
