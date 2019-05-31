package com.edanyma.manager;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.BootstrapModel;
import com.edanyma.model.ClientLocationModel;
import com.edanyma.model.ClientOrderModel;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.FavoriteCompanyModel;
import com.edanyma.model.FilterDishModel;
import com.edanyma.model.FilterModel;
import com.edanyma.model.HomeMenuModel;
import com.edanyma.model.OurClientModel;

import java.util.LinkedList;
import java.util.List;

public class GlobalManager {


    private static final GlobalManager GLOBAL_MANAGER = new GlobalManager();

    private static BootstrapModel bootstrapModel;
    private static List< HomeMenuModel > homeMenus;
    private static List< ClientOrderModel > clientOrders = null;
    private static String userToken;
    private static OurClientModel client;
    private static FilterModel companyFilter;
    private static FilterDishModel dishFilter;
    private static int dishEntityPosition;
    private static boolean actionConfirmed;
    private static boolean animationInProgess;
    private static ClientLocationModel mClientLocationModel;

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
                if ( foundItem ) {
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
        setFavoriteSignToCompany( AppConstants.FAKE_ID, false );
        if ( client != null && client.getFavoriteCompanies() != null) {
            for ( FavoriteCompanyModel favoriteCompany : client.getFavoriteCompanies() ) {
                for ( CompanyModel company : bootstrapModel.getCompanies() ) {
                    if ( favoriteCompany.getCompanyId().toString().equals( company.getId() ) ) {
                        company.setFavorite( true );
                    }
                }
            }
            setHomeMenuCount( AppConstants.DISH_FAVOITES, client.getFavoriteCompanies().size() + "" );
        } else {
            setHomeMenuCount( AppConstants.DISH_FAVOITES, "0" );
        }

    }

    public static List< HomeMenuModel > getHomeMenus() {
        return homeMenus;
    }

    public static FilterModel getCompanyFilter() {
        return companyFilter;
    }

    public static void setCompanyFilter( FilterModel companyFilter ) {
        GlobalManager.companyFilter = companyFilter;
    }

    public static int getDishEntityPosition() {
        return dishEntityPosition;
    }

    public static void setDishEntityPosition( int dishEntityPosition ) {
        GlobalManager.dishEntityPosition = dishEntityPosition;
    }

    public static FilterDishModel getDishFilter() {
        return dishFilter;
    }

    public static void setDishFilter( FilterDishModel dishFilter ) {
        GlobalManager.dishFilter = dishFilter;
    }

    public static boolean isActionConfirmed() {
        return actionConfirmed;
    }

    public static void setActionConfirmed( boolean actionConfirmed ) {
        GlobalManager.actionConfirmed = actionConfirmed;
    }

    public static void addToFavorites( FavoriteCompanyModel favorite ) {
        client.getFavoriteCompanies().add( favorite );
        setFavoriteSignToCompany( favorite.getCompanyId(), true );
        setHomeMenuCount( AppConstants.DISH_FAVOITES, client.getFavoriteCompanies().size() + "" );
    }

    public static void removeFromFavorites( Integer companyId ) {
        List< FavoriteCompanyModel > newList = new LinkedList<>();
        for ( FavoriteCompanyModel favorite : client.getFavoriteCompanies() ) {
            if ( !favorite.getCompanyId().equals( companyId ) ) {
                newList.add( favorite );
            }
        }
        setFavoriteSignToCompany( companyId, false );
        client.setFavoriteCompanies( newList );
        setHomeMenuCount( AppConstants.DISH_FAVOITES, client.getFavoriteCompanies().size() + "" );
    }

    public static boolean isFavorite( Integer companyId ) {
        if ( !isSignedIn() ) {
            return false;
        }
        for ( FavoriteCompanyModel favorite : client.getFavoriteCompanies() ) {
            if ( favorite.getCompanyId().equals( companyId ) ) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSignedIn() {
        return client != null && client.getUuid() != null;
    }

    private static void setFavoriteSignToCompany( Integer companyId, boolean status ) {
        for ( CompanyModel company : bootstrapModel.getCompanies() ) {
            if ( AppConstants.FAKE_ID == companyId ) {
                company.setFavorite( false );
            }
            if ( companyId.toString().equals( company.getId() ) ) {
                company.setFavorite( status );
            }
        }
    }

    public static boolean isAnimationInProgess() {
        return animationInProgess;
    }

    public static void setAnimationInProgess( boolean animationInProgess ) {
        GlobalManager.animationInProgess = animationInProgess;
    }

    public static CompanyModel getCompanyById( String companyId ) {
        CompanyModel companyModel = null;
        for (CompanyModel company : getBootstrapModel().getCompanies() ){
            if( companyId.equals( company.getId() ) ){
                companyModel = company;
                break;
            }
        }
        return companyModel;
    }

    public static void setClientLocation( ClientLocationModel mClientLocationModel ) {
        GlobalManager.mClientLocationModel = mClientLocationModel;
    }

    public static ClientLocationModel getClientLocation() {
        if( mClientLocationModel == null ){
            mClientLocationModel = new ClientLocationModel();
        }
        return mClientLocationModel;
    }

    public static List< ClientOrderModel > getClientOrders() {
        return clientOrders;
    }

    public static void setClientOrders( List< ClientOrderModel > clientOrders ) {
        GlobalManager.clientOrders = clientOrders;
    }
}
