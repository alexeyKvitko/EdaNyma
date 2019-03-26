package com.edanyma.utils;

import com.edanyma.model.CompanyLight;
import com.edanyma.model.CompanyModel;

public abstract class ConvertUtils {

    public static CompanyLight convertToCompanyLight( CompanyModel companyModel ){
        CompanyLight company = new CompanyLight();
        company.setId( companyModel.getId() );
        company.setThumbUrl( companyModel.getThumbUrl() );
        company.setDisplayName( companyModel.getDisplayName() );
        company.setDelivery( "от " + companyModel.getDelivery().toString() + " руб." );
        company.setCommentCount( companyModel.getCommentCount() );
        company.setDeliveryTimeMin( "~" + companyModel.getDeliveryTimeMin() + "мин." );
        company.setDayoffWork( companyModel.getDayoffWork() );
        company.setWeekdayWork( companyModel.getWeekdayWork() );
        return company;
    }

}
