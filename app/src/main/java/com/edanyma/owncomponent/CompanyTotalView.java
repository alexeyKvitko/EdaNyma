package com.edanyma.owncomponent;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.R;

public class CompanyTotalView extends LinearLayout {

    private String mCompanyId;
    private Integer mPrice;
    private TextView mPriceView;

    public CompanyTotalView( Context context ) {
        super( context );
    }

    public CompanyTotalView( Context context, String companyId, Integer price ) {
        super( context );
        inflate( context, R.layout.company_total, this );
        this.mCompanyId = companyId;
        this.mPrice = price;
        initialize();
    }

    private void initialize() {
        mPriceView = findViewById( R.id.companyTotalPriceId );
        mPriceView.setTypeface( AppConstants.OFFICE, Typeface.BOLD );
        mPriceView.setText( mPrice.toString() );
    }

    public void changeCompanyTotal( Integer delta ){
        mPrice += delta;
        mPriceView.setText( mPrice.toString() );
    }


}
