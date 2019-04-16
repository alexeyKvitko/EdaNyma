package com.edanyma.manager;

import com.edanyma.AppConstants;
import com.edanyma.model.MenuEntityModel;

import java.util.LinkedList;
import java.util.List;

public class BasketOrderManager {

    private static final BasketOrderManager BASKET_MANAGER = new BasketOrderManager();

    private static List< MenuEntityModel > customerBasket;

    private BasketOrderManager() {
    }

    public static BasketOrderManager getInstance() {
        return BASKET_MANAGER;
    }

    public static void addEntityToBasket( MenuEntityModel menuEntity, Integer count ) {
        boolean exist = false;
        if ( customerBasket == null ) {
            customerBasket = new LinkedList<>();
        }
        for ( MenuEntityModel entity : customerBasket ) {
            if ( entity.getId().equals( menuEntity.getId() ) &&
                    entity.getWspType().equals( menuEntity.getWspType() ) ) {
                entity.setCount( entity.getCount() + count );
                exist = true;
            }
        }
        if ( !exist ) {
            menuEntity.setCount( count );
            customerBasket.add( menuEntity );
        }
    }

    public static Integer getBasketPrice() {
        Integer entityPrice = 0;
        for ( MenuEntityModel menuEntity : customerBasket ) {
            entityPrice += calculatePrice( menuEntity );
        }
        return entityPrice;
    }

    public static void clearBasket() {
        customerBasket = new LinkedList<>();
    }

    public static Integer calculatePrice( MenuEntityModel menuEntity ) {
        Integer calc = 0;
        switch ( menuEntity.getWspType() ) {
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

    public static List<MenuEntityModel> getBasket() {
        return customerBasket;
    }

}
