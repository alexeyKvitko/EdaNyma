package com.edanyma.owncomponent;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.utils.PicassoClient;

public class DishEntityCard extends LinearLayout {

    private ImageView mEntityImage;
    private TextView mEntityTitle;
    private TextView mEntityDesc;
    private TextView mEntityPrice;

    public DishEntityCard( Context context ) {
        super( context );
        inflate(context, R.layout.dish_entity_card, this);
        initialize();
    }

    public DishEntityCard( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate(context, R.layout.dish_entity_card, this);
        initialize();
    }

    private void initialize(){
        mEntityImage = findViewById( R.id.entityImgId );
        mEntityTitle = findViewById( R.id.entityTitleTextId );
        mEntityDesc = findViewById( R.id.entityDescTextId );
        mEntityPrice = findViewById( R.id.entityPriceTextId );
        mEntityTitle.setTypeface( AppConstants.B52);
        mEntityDesc.setTypeface( AppConstants.ROBOTO_CONDENCED);
        ((TextView) findViewById( R.id.entityBuyId )).setTypeface( AppConstants.ROBOTO_CONDENCED);
        mEntityPrice.setTypeface( AppConstants.OFFICE, Typeface.BOLD);
    }

    public ImageView getEntityImage() {
        return mEntityImage;
    }

    public void setEntityImage( String uri ) {
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), uri, this.mEntityImage, 444,444 );
    }

    public TextView getEntityTitle() {
        return mEntityTitle;
    }

    public void setEntityTitle( String title ) {
        this.mEntityTitle.setText( title );
    }

    public TextView getEntityDesc() {
        return mEntityDesc;
    }

    public void setEntityDesc( String desc ) {
        this.mEntityDesc.setText( desc );
    }

    public TextView getEntityPrice() {
        return mEntityPrice;
    }

    public void setEntityPrice( String price ) {
        this.mEntityPrice.setText( price );
    }
}
