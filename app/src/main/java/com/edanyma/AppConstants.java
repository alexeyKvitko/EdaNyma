package com.edanyma;

import android.graphics.Typeface;

import java.util.LinkedHashMap;
import java.util.Map;

public class AppConstants {

    public static final Typeface ROBOTO_BLACK = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/Roboto-Black.ttf");
    public static final Typeface ROBOTO_CONDENCED = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/RobotoCondensed.ttf");
    public static final Typeface B52 = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/B52.otf");
    public static final Typeface SANDORA = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/sandora.ttf");

    public static final String DEFAULT_USER = "guest";
    public static final String DEFAULT_PASSWORD = "1111";

    public static final String AUTH_BEARER = "Bearer ";

    public static final String ASTERISKS= "*";
    public static final String CLOSE= "CLOSE";
    public static final String OPEN= "OPEN";

    public static final int FAKE_ID= -1;
    public static final String STR_FAKE_ID= "-1";

    public static final int CLOSE_DISH_FILTER_BUTTON = 6;
    public static final int CLOSE_KITCHEN_FILTER_BUTTON = 4;

    public static final int COMPANY_BOTTOM_INDEX = 0;
    public static final int DISH_BOTTOM_INDEX = 1;
    public static final int HOME_BOTTOM_INDEX = 2;
    public static final int BASKET_BOTTOM_INDEX = 3;
    public static final int LOGIN_BOTTOM_INDEX = 4;

    public static final int LOGIN_DRAWER_INDEX = -1;
    public static final int HOME_DRAWER_INDEX = 0;
    public static final int COMPANY_DRAWER_INDEX = 1;
    public static final int DISH_DRAWER_INDEX = 2;
    public static final int BASKET_DRAWER_INDEX = 3;

    public static final int BANNER_LEFT = 0;
    public static final int BANNER_CENTER = 540;
    public static final int BANNER_END = 984;

    public static final String PREV_NAV_STATE = "prev_nav_state";
    public static final String COMPANY_FILTER = "companyFiler";

    public static final String SEND_PHONE_CODE = "1111";

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    public static final String PHONE_PATTERN = "^\\d{10}$";

    public static final String NEW_CLIENT = "NEW";
    public static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";
    public static final String FAKE_PASSWORD = "12345678";

    public static final String ALL_COMPANIES = "ВСЕ ЗАВЕДЕНИЯ";
    public static final String ALL_DISHES = "ВСЕ БЛЮДА";
    public static final String DISH_PIZZA = "ПИЦЦА";
    public static final String DISH_SUSHI = "СУШИ/СЭТЫ";
    public static final String DISH_BURGERS = "БУРГЕРЫ";
    public static final String DISH_GRILL = "МАНГАЛ МЕНЮ";
    public static final String DISH_WOK = "WOK МЕНЮ";
    public static final String DISH_FAVOITES = "ИЗБРАННОЕ";
    public static final String CUSTOM_FILTER = "CUSTOM_FILTER";

    public static final String PAY_TYPE_CASH = "CASH";
    public static final String PAY_TYPE_CARD = "CARD";
    public static final String PAY_TYPE_WALLET = "WALLET";

    public static final Map<String,String> PAY_TYPES = new LinkedHashMap<String,String>(){
        {
            put("CASH","Наличными");
            put("CARD","Банковской Картой");
            put("WALLET","Электронный кошелек");
        }
    };

    public static final Map<String,String> EXTRA_FILTER = new LinkedHashMap<String,String>(){
        {
            put("FAVORITES","Избранное");
            put("ACTION","Акция");
            put("FREE_DELI","Бесплатная Доставка");
            put("HIGH_RATING","Высокий Рейтинг");
            put("NEW","Новые");
        }
    };



}
