package com.edanyma.utils;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Size;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.manager.GlobalManager;
import com.edanyma.model.CompanyLight;
import com.edanyma.model.CompanyModel;
import com.edanyma.model.MenuEntityModel;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.edanyma.manager.GlobalManager.*;

public abstract class ConvertUtils {

    public static CompanyLight convertToCompanyLight( CompanyModel companyModel ){
        CompanyLight company = new CompanyLight();
        company.setId( companyModel.getId() );
        company.setThumbUrl( getBootstrapModel().getStaticUrl()+
                String.format( AppConstants.STATIC_COMPANY, companyModel.getThumb() ) );
        company.setDisplayName( companyModel.getDisplayName() );
        company.setDelivery( "от " + companyModel.getDelivery().toString() + " руб." );
        company.setCommentCount( companyModel.getCommentCount() );
        company.setDeliveryTimeMin( "~" + companyModel.getDeliveryTimeMin() + "мин." );
        company.setDayoffWork( companyModel.getDayoffWork() );
        company.setWeekdayWork( companyModel.getWeekdayWork() );
        company.setFavorite( companyModel.isFavorite() );
        company.setFeedbackRate( companyModel.getFeedbackRate() );
        return company;
    }
    
    public static MenuEntityModel cloneMenuEntity( MenuEntityModel source ){
        MenuEntityModel target = new MenuEntityModel();
        target.setId( source.getId() );
        target.setCompanyId( source.getCompanyId() );
        target.setCompanyName( source.getCompanyName() );
        target.setTypeId( source.getTypeId() );
        target.setCategoryId( source.getCategoryId() );
        target.setName( source.getName() );
        target.setDisplayName( source.getDisplayName() );
        target.setDescription( source.getDescription() );
        target.setImageUrl( source.getImageUrl() );
        target.setWeightOne( source.getWeightOne() );
        target.setSizeOne( source.getSizeOne() );
        target.setPriceOne( source.getPriceOne() );
        target.setWeightTwo( source.getWeightTwo() );
        target.setSizeTwo( source.getSizeTwo() );
        target.setPriceTwo( source.getPriceTwo() );
        target.setWeightThree( source.getWeightThree() );
        target.setSizeThree( source.getSizeThree() );
        target.setPriceThree( source.getPriceThree() );
        target.setWeightFour( source.getWeightFour() );
        target.setSizeFour( source.getSizeFour() );
        target.setPriceFour( source.getPriceFour() );
        target.setStatus( source.getStatus() );
        target.setWspType( source.getWspType() );
        return target;
    }

    public static float convertDpToPixel( float dp ){
        return dp * ((float) EdaNymaApp.getAppContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp( float px ){
        return px / ((float) EdaNymaApp.getAppContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static byte[] bitmapToByteArray( Bitmap bm) {
        int iBytes = bm.getWidth() * bm.getHeight() * 4;
        ByteBuffer buffer = ByteBuffer.allocate(iBytes);
        bm.copyPixelsToBuffer(buffer);
        return buffer.array();
    }



}
