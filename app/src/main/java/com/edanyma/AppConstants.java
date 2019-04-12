package com.edanyma;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;

import com.edanyma.utils.ConvertUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class AppConstants {

    public static final Typeface ROBOTO_BLACK = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/Roboto-Black.ttf");
    public static final Typeface ROBOTO_CONDENCED = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/RobotoCondensed.ttf");
    public static final Typeface B52 = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/B52.otf");
    public static final Typeface SANDORA = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/sandora.ttf");
    public static final Typeface OFFICE = Typeface.createFromAsset( EdaNymaApp.getAppContext().getAssets(), "font/office.ttf");


    public static final String DEFAULT_USER = "guest";
    public static final String DEFAULT_PASSWORD = "1111";
    public static final String PICTURE_DIR = "yummyeat";
    public static final String FILENAME_DISH = "dishinfo";
    public static final String EXTENSION_JPG = ".jpg";
    public static final String EXTENSION_PNG = ".png";
    public static final String EXTENSION_NOMEDIA = ".nomedia";
    public static final String FAST_MENU = "fast_menu";

    public static final String AUTH_BEARER = "Bearer ";

    public static final int HEADER_ACTION_REMOVE = -1;
    public static final int HEADER_ACTION_RESTORE = 1;
    public static final int SUSHI_SET_ID = 9;

    public static final float DECOR_CORNER_RADIUS = ConvertUtils.convertDpToPixel( 18 );
    public static final float DECOR_LEFT_MARGIN = ConvertUtils.convertDpToPixel( 230 );
    public static final float DECOR_HEIGHT = ( ( float ) EdaNymaApp.getAppContext().getResources().getDisplayMetrics().heightPixels -  ConvertUtils.convertDpToPixel( 24 ));
    public static final float DECOR_WIDTH = ( ( float ) EdaNymaApp.getAppContext().getResources().getDisplayMetrics().widthPixels );
    public static final float HORIZONTAL_RATIO = ( DECOR_WIDTH - ConvertUtils.convertDpToPixel( 287 ) ) / DECOR_LEFT_MARGIN;
    public static final float VERTICAL_RATIO = ( DECOR_HEIGHT - ConvertUtils.convertDpToPixel( 510 ) ) / DECOR_LEFT_MARGIN;
    public static final float MARGIN_RATIO = ConvertUtils.convertDpToPixel( 65 ) / DECOR_LEFT_MARGIN;


    public static final String ASTERISKS= "*";
    public static final String CLOSE= "CLOSE";
    public static final String OPEN= "OPEN";

    public static final String SIGN_IN= "SIGN_IN";
    public static final String SIGN_UP= "SIGN_UP";
    public static final String SIGN_TYPE = "signType";

    public static final int FAKE_ID= -1;

    public static final String STATIC_COMPANY= "companies/%s.jpg";
    public static final String STATIC_COMPANY_LOGO= "logos/%s.png";

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

    public static final int HTTP_OK = 200;
    public static final int HTTP_NOT_FOUND = 404;

    public static final String PREV_NAV_STATE = "prev_nav_state";
    public static final String COMPANY_FILTER = "companyFiler";
    public static final String COMPANY_ID = "companyId";

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
