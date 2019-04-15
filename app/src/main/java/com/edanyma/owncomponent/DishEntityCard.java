package com.edanyma.owncomponent;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.PicassoClient;

public class DishEntityCard extends FrameLayout {

    private static final String TAG = "DishEntityCard";

    private static final int DISH_IMAGE_WIDTH = ( int ) ConvertUtils.convertDpToPixel( 148 );
    private static final int DISH_IMAGE_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 142 );

    private ImageView mEntityImage;
    private TextView mEntityTitle;
    private TextView mEntityDesc;
    private TextView mEntityPrice;
    private TextView mEntitySize;
    private TextView mEntityCompanyName;

    private MenuEntityModel mDishEntity;


    public DishEntityCard( Context context ) {
        super( context );
        inflate( context, R.layout.dish_entity_card, this );
        initialize();
    }

    public DishEntityCard( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate( context, R.layout.dish_entity_card, this );
        initialize();
    }

    private void initialize() {
        mEntityImage = findViewById( R.id.entityImgId );
        mEntityTitle = findViewById( R.id.entityTitleTextId );
        mEntityDesc = findViewById( R.id.entityDescTextId );
        mEntityPrice = findViewById( R.id.entityPriceTextId );
        mEntitySize = findViewById( R.id.entitySizeId );
        mEntityCompanyName =  findViewById( R.id.entityCompanyNameId );
        mEntityCompanyName.setVisibility( View.GONE );

        mEntityCompanyName.setTypeface( AppConstants.B52 );
        mEntityTitle.setTypeface( AppConstants.B52 );
        mEntityDesc.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mEntitySize.setTypeface( AppConstants.ROBOTO_CONDENCED );
        mEntityPrice.setTypeface( AppConstants.OFFICE, Typeface.BOLD );
    }

    public void setEntityImage( String uri ) {
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), uri, this.mEntityImage,
                DISH_IMAGE_WIDTH, DISH_IMAGE_HEIGHT );
    }

    public void setEntityTitle( String title ) {
        this.mEntityTitle.setText( title );
    }

    public void setCompanyName( String companyName ) {
        this.mEntityCompanyName.setText( companyName );
        this.mEntityCompanyName.setVisibility( View.VISIBLE );
    }

    public void setEntityDesc( String desc ) {
        this.mEntityDesc.setText( desc );
    }


    public void setEntityPrice( String price ) {
        this.mEntityPrice.setText( price );
    }

    public void setEntitySize( String value ) {
        this.mEntitySize.setText( value );
    }


    public void setDishEntity( MenuEntityModel dishEntity ) {
        this.mDishEntity = dishEntity;
        this.setEntityImage( dishEntity.getImageUrl() );
        if( dishEntity.getCompanyName() != null ){
            this.setCompanyName( dishEntity.getCompanyName() );
        }
        this.setEntityTitle( dishEntity.getDisplayName() );
        this.setEntityDesc( dishEntity.getDescription() );
        this.setEntityPrice( dishEntity.getPriceOne() );
        this.setEntitySize( dishEntity.getSizeOne() != null ?
                dishEntity.getSizeOne() : dishEntity.getWeightOne() );
    }

    public int getDishEntityId() {
        return Integer.valueOf( this.mDishEntity.getId() );
    }


    public MenuEntityModel getDishEntity() {
        return mDishEntity;
    }
}
