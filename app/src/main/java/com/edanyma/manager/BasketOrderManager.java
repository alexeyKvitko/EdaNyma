package com.edanyma.manager;

import android.app.Activity;
import android.content.Intent;

import com.edanyma.AppConstants;
import com.edanyma.AppPreferences;
import com.edanyma.EdaNymaApp;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.model.PreferenceBasket;
import com.edanyma.owncomponent.ModalMessage;
import com.edanyma.utils.ConvertUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.edanyma.manager.GlobalManager.*;

public class BasketOrderManager {


    private static final BasketOrderManager BASKET_MANAGER = new BasketOrderManager();

    private static List< MenuEntityModel > customerBasket;

    private BasketOrderManager() {
    }

    public static BasketOrderManager getInstance() {
        return BASKET_MANAGER;
    }

    public static void addEntityToBasket( Activity activity, MenuEntityModel menuEntity,
                                                            String companyName, Integer count ) {
        if( !validateCompanyCount( companyName ) ){
            ModalMessage.show( activity, "Сообщение.", new String[]{"Превышен лимит Заведений в заказе"} );
            return;
        }
        boolean exist = false;
        if ( customerBasket == null ) {
            customerBasket = new LinkedList<>();
        }
        for ( MenuEntityModel entity : customerBasket ) {
            if ( entity.getId().equals( menuEntity.getId() ) &&
                    entity.getWspType().equals( menuEntity.getWspType() ) ) {
                entity.setCount( count );
                exist = true;
                break;
            }
        }
        if ( !exist ) {
            MenuEntityModel basketEntity = ConvertUtils.cloneMenuEntity( menuEntity );
            basketEntity.setCompanyName( companyName );
            basketEntity.setCount( count );
            customerBasket.add( basketEntity );
        }
        sendMessageToActivity();
    }

    private static boolean validateCompanyCount( String companyName ){
        boolean isBasketValid = true;
        if( customerBasket == null || customerBasket.size() == 0 ){
            return true;
        }
        Set companies =  new HashSet();
        for ( MenuEntityModel entity : customerBasket ){
            companies.add( entity.getCompanyName() );
        }
        companies.add( companyName );
        if( companies.size() > 2 ){
            isBasketValid = false;
        }
        return isBasketValid;

    }

    public static void removeEntityFromBasket( String removeEntityId ){
        List<MenuEntityModel> newBasket = new LinkedList<>( );
        for( MenuEntityModel entity : customerBasket){
            if( !removeEntityId.equals( entity.getId() ) ){
                newBasket.add( entity );
            }
        }
        customerBasket = newBasket;
        sendMessageToActivity();
    }

    public static Integer getBasketPrice() {
        Integer entityPrice = 0;
        if ( customerBasket != null && customerBasket.size() > 0 ){
            for ( MenuEntityModel menuEntity : customerBasket ) {
                entityPrice += calculatePrice( menuEntity );
            }
        }
        return entityPrice;
    }

    public static Integer getEntityCountInBasket( String entityId ){
        Integer entityCount = 0;
        if ( customerBasket != null && customerBasket.size() > 0 ){
            for ( MenuEntityModel menuEntity : customerBasket ) {
                if ( menuEntity.getId().equals( entityId ) ){
                    entityCount = menuEntity.getCount();
                    break;
                }
            }
        }
        return entityCount;
    }

    public static void clearBasket() {
        customerBasket = new LinkedList<>();
        sendMessageToActivity();
    }

    public static Integer calculatePrice( MenuEntityModel menuEntity ) {
        Integer calc = 0;
        switch ( menuEntity.getWspType().toUpperCase() ) {
            case AppConstants.SEL_TYPE_ONE:
                calc = Integer.valueOf( menuEntity.getPriceOne() ) * menuEntity.getCount();
                break;
            case AppConstants.SEL_TYPE_TWO:
                calc = Integer.valueOf( menuEntity.getPriceTwo() ) * menuEntity.getCount();
                break;
            case AppConstants.SEL_TYPE_THREE:
                calc = Integer.valueOf( menuEntity.getPriceThree() ) * menuEntity.getCount();
                break;
            case AppConstants.SEL_TYPE_FOUR:
                calc = Integer.valueOf( menuEntity.getPriceFour() ) * menuEntity.getCount();
                break;
            default:
                break;
        }
        return calc;
    }

    public static void sendMessageToActivity(){
        Intent intent = new Intent( AppConstants.BASKET_CONTENT_CHANGE );
        intent.putExtra( AppConstants.BASKET_PRICE_SHOW, customerBasket.size() > 0 );
        EdaNymaApp.getAppContext().sendBroadcast( intent );
        if ( isSignedIn() ){
            if ( customerBasket.size() > 0 ){
                AppPreferences.setPreference( AppConstants.BASKET_PREF, new PreferenceBasket( customerBasket ).get() );
            } else {
                AppPreferences.removePreference( AppConstants.BASKET_PREF );
            }
        }
    }

    public static boolean isBasketEmpty(){
        return customerBasket == null || customerBasket.size() == 0;
    }

    public static List<MenuEntityModel> getBasket() {
        return customerBasket;
    }

    public static void setBasket( PreferenceBasket preferenceBasket ){
        customerBasket = new LinkedList<>( );
        for( MenuEntityModel menuEntityModel : preferenceBasket.getSharedPreferenceBasket() ){
            customerBasket.add( menuEntityModel );
        }
    }

}
