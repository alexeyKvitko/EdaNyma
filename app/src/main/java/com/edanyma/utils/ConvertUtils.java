package com.edanyma.utils;

import android.util.DisplayMetrics;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyLight;
import com.edanyma.model.CompanyModel;

public abstract class ConvertUtils {

    public static CompanyLight convertToCompanyLight( CompanyModel companyModel ){
        CompanyLight company = new CompanyLight();
        company.setId( companyModel.getId() );
        company.setThumbUrl( GlobalManager.getInstance().getBootstrapModel().getStaticUrl()+
                String.format( AppConstants.STATIC_COMPANY, companyModel.getThumb() ) );
        company.setDisplayName( companyModel.getDisplayName() );
        company.setDelivery( "от " + companyModel.getDelivery().toString() + " руб." );
        company.setCommentCount( companyModel.getCommentCount() );
        company.setDeliveryTimeMin( "~" + companyModel.getDeliveryTimeMin() + "мин." );
        company.setDayoffWork( companyModel.getDayoffWork() );
        company.setWeekdayWork( companyModel.getWeekdayWork() );
        return company;
    }

    public static float convertDpToPixel( float dp ){
        return dp * ((float) EdaNymaApp.getAppContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp( float px ){
        return px / ((float) EdaNymaApp.getAppContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
