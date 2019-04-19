package com.edanyma.model;

import com.edanyma.AppConstants;
import com.edanyma.R;

import java.io.Serializable;

public class ActivityState implements Serializable {

    private int mBottomMenuIndex;
    private int drawerMenuIndex;
    private int selectedBottomId;

    public ActivityState( int bottomMenuIndex ) {
        this.mBottomMenuIndex = bottomMenuIndex;
        switch ( bottomMenuIndex ){
            case AppConstants.COMPANY_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.COMPANY_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_company;
                break;
            case AppConstants.DISH_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.DISH_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_dish;
                break;
            case AppConstants.HOME_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.HOME_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_home;
                break;
            case AppConstants.BASKET_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.BASKET_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_basket;
                break;
            case AppConstants.LOGIN_BOTTOM_INDEX:
                this.drawerMenuIndex = AppConstants.LOGIN_DRAWER_INDEX;
                this.selectedBottomId = R.id.navigation_login;
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

}
