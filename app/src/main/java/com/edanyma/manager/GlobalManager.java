package com.edanyma.manager;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.BootstrapModel;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.HomeMenuModel;
import com.edanyma.model.MenuCategoryModel;
import com.edanyma.model.OurClientModel;

import java.util.LinkedList;
import java.util.List;

public class GlobalManager {


    private static final GlobalManager GLOBAL_MANAGER = new GlobalManager();

    private static BootstrapModel bootstrapModel;
    private static List< HomeMenuModel > homeMenus;
    private static String userToken;
    private static OurClientModel client;

    private GlobalManager() {
    }


    public static GlobalManager getInstance() {
        return GLOBAL_MANAGER;
    }

    private static void initHomeMenus() {
        homeMenus = new LinkedList<>();
        homeMenus.add( new HomeMenuModel( AppConstants.ALL_COMPANIES, EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.restaurant ), "0",
                AppConstants.ALL_DISHES, EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.all_dishes ), "0" ) );
        homeMenus.add( new HomeMenuModel( AppConstants.DISH_PIZZA, EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.pizza ), "0",
                AppConstants.DISH_SUSHI, EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.sushi ), "0" ) );
        homeMenus.add( new HomeMenuModel( AppConstants.DISH_BURGERS, EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.burger ), "0",
                AppConstants.DISH_GRILL, EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.shashlik ), "0" ) );
        homeMenus.add( new HomeMenuModel( AppConstants.DISH_WOK, EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.wok ), "0",
                AppConstants.DISH_FAVOITES, EdaNymaApp.getAppContext().getResources().getDrawable( R.drawable.favorite ), "0" ) );
    }

    private static void setHomeMenuCount( String menu, String count ) {
        for ( HomeMenuModel homeMenu : homeMenus ) {
            if ( menu.equals( homeMenu.getLeftMenuName() ) ) {
                homeMenu.setLeftMenuCount( count );
                break;
            } else if ( menu.equals( homeMenu.getRightMenuName() ) ) {
                homeMenu.setRightMenuCount( count );
                break;
            }
        }
    }

    public static BootstrapModel getBootstrapModel() {
        return bootstrapModel;
    }

    public static void setBootstrapModel( BootstrapModel bootstrapModel ) {
        GlobalManager.bootstrapModel = bootstrapModel;
        if ( homeMenus == null ) {
            initHomeMenus();
        }
        int size = bootstrapModel.getCompanies().size();
        String totalCompanies = size < 10 ? "0" + size : "" + size;
        setHomeMenuCount( AppConstants.ALL_COMPANIES, totalCompanies );
        setHomeMenuCount( AppConstants.ALL_DISHES, totalCompanies );
        setHomeMenuCount( AppConstants.DISH_PIZZA, getMenuCount( bootstrapModel.getFastMenu().getPizzaIds() ) );
        setHomeMenuCount( AppConstants.DISH_SUSHI, getMenuCount( bootstrapModel.getFastMenu().getShushiIds() ) );
        setHomeMenuCount( AppConstants.DISH_BURGERS, getMenuCount( bootstrapModel.getFastMenu().getBurgerIds() ) );
        setHomeMenuCount( AppConstants.DISH_GRILL, getMenuCount( bootstrapModel.getFastMenu().getGrillIds() ) );
        setHomeMenuCount( AppConstants.DISH_WOK, getMenuCount( bootstrapModel.getFastMenu().getWokIds() ) );
    }

    private static String getMenuCount( List< Integer > menuIds ) {
        int menuCount = 0;
        for ( CompanyModel company : bootstrapModel.getCompanies() ) {
            String[] categoryIds = company.getMenuCategoiesIds().split( "," );
            for ( Integer menuId : menuIds ) {
                boolean foundItem = false;
                for ( String categoryId : categoryIds ) {
                    if ( Integer.valueOf( categoryId ).equals( menuId ) ) {
                        menuCount++;
                        foundItem = true;
                        break;
                    }
                }
                if( foundItem ){
                    break;
                }
            }
        }
        return menuCount < 10 ? "0" + menuCount : "" + menuCount;
    }

    public static String getUserToken() {
        return userToken;
    }

    public static void setUserToken( String userToken ) {
        GlobalManager.userToken = userToken;
    }

    public static OurClientModel getClient() {
        return client;
    }

    public static void setClient( OurClientModel client ) {
        GlobalManager.client = client;
    }

    public static List< HomeMenuModel > getHomeMenus() {
        return homeMenus;
    }
}
