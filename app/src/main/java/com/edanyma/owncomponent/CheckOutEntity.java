package com.edanyma.owncomponent;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.PicassoClient;

public class CheckOutEntity extends FrameLayout {

    private static final String TAG = "CheckOutEntity";

    private static final int CHECKOUT_IMAGE_WIDTH = ( int ) ConvertUtils.convertDpToPixel( 64 );
    private static final int CHECKOUT_IMAGE_HEIGHT = ( int ) ConvertUtils.convertDpToPixel( 62 );

    private ImageView mEntityImage;
    private TextView mEntityTitle;
    private TextView mEntityWSP;
    private TextView mEntityPrice;
    private TextView mEntityCount;

    private MenuEntityModel mDishEntity;

    public CheckOutEntity( Context context ) {
        super( context );
        inflate( context, R.layout.checkout_entity, this );
        initialize();
    }

    public CheckOutEntity( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs );
        inflate( context, R.layout.checkout_entity, this );
        initialize();
    }

    private void initialize(){
        mEntityImage = findViewById( R.id.checkOutEntityImgId );
        mEntityTitle = findViewById( R.id.checkOutTitleTextId );
        mEntityTitle.setTypeface( AppConstants.B52 );
        mEntityWSP = findViewById( R.id.checkOutWspId );
        mEntityWSP.setTypeface( AppConstants.ROBOTO_CONDENCED );

        mEntityCount = findViewById( R.id.dishCountTextId );
        mEntityCount.setTypeface( AppConstants.ROBOTO_CONDENCED );

        mEntityPrice = findViewById( R.id.checkOutPriceTextId );
        mEntityPrice.setTypeface( AppConstants.OFFICE, Typeface.BOLD );
    }

    public void setDishEntity( MenuEntityModel dishEntity ) {
        this.mDishEntity = dishEntity;
        this.setEntityImage( dishEntity.getImageUrl() );
        this.setEntityTitle( dishEntity.getDisplayName() );
        this.setEntityWSP();
        this.setEntityPrice( dishEntity.getActualPrice() );
        this.setEntityCount( dishEntity.getCount() );
//        this.setEntitySize( dishEntity.getSizeOne() != null ?
//                dishEntity.getSizeOne() : dishEntity.getWeightOne() );
//        mInBasketImage.setVisibility( dishEntity.getCount() > 0 ? View.VISIBLE : View.GONE );
    }

    public void setEntityPrice( Integer price ) {
        this.mEntityPrice.setText( price.toString() );
    }

    public void setEntityCount( Integer count ) {
        this.mEntityCount.setText( count.toString() );
    }

    public void setEntityImage( String uri ) {
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), uri, this.mEntityImage,
                CHECKOUT_IMAGE_WIDTH, CHECKOUT_IMAGE_HEIGHT );
    }

    public void setEntityTitle( String title ) {
        this.mEntityTitle.setText( title );
    }

    public void setEntityWSP() {
        String size = null;
        String weight = null;
        switch ( mDishEntity.getWspType() ){
            case AppConstants.SEL_TYPE_ONE:
                size = mDishEntity.getSizeOne();
                weight = mDishEntity.getWeightOne();
                break;
            case AppConstants.SEL_TYPE_TWO:
                size = mDishEntity.getSizeTwo();
                weight = mDishEntity.getWeightTwo();
                break;
            case AppConstants.SEL_TYPE_THREE:
                size = mDishEntity.getSizeThree();
                weight = mDishEntity.getWeightThree();
                break;
            case AppConstants.SEL_TYPE_FOUR:
                size = mDishEntity.getSizeFour();
                weight = mDishEntity.getWeightFour();
                break;
        }
        String wsp = size != null ? size : null;
        if( weight != null ){
            wsp = wsp != null ? (wsp+"/"+weight) : weight;
        }
        this.mEntityWSP.setText( wsp );
    }
}
