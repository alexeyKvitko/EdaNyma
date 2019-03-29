package com.edanyma.owncomponent;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.utils.AppUtils;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.PicassoClient;

public class DishEntityCard extends LinearLayout implements View.OnClickListener {

    private static final int DISH_IMAGE_WIDTH = (int) ConvertUtils.convertDpToPixel( 148 );
    private static final int DISH_IMAGE_HEIGHT = (int) ConvertUtils.convertDpToPixel( 142 );


    private ImageView mEntityImage;
    private TextView mEntityTitle;
    private TextView mEntityDesc;
    private TextView mEntityPrice;
    private TextView mEntitySize;
    private FrameLayout mDishCardCollapsed;
    private FrameLayout mDishCardExpanded;


    private String mThumpImgUri;

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
        mDishCardCollapsed = findViewById( R.id.dishCardCollapsedId );
        mDishCardExpanded = findViewById( R.id.dishCardExpandedId );
        mEntitySize =  findViewById( R.id.entitySizeId );

        mEntityTitle.setTypeface( AppConstants.B52);
        mEntityDesc.setTypeface( AppConstants.ROBOTO_CONDENCED);
        mEntitySize.setTypeface( AppConstants.ROBOTO_CONDENCED);
        mEntityPrice.setTypeface( AppConstants.OFFICE, Typeface.BOLD);
        mDishCardExpanded.setOnClickListener( this );
        mDishCardCollapsed.setOnClickListener( this );
    }

//    private void animateDishCard( int start, int end, final int cardLeftMargin, final boolean isNeedCallback ){
//        final LinearLayout.LayoutParams layoutParams = ( LinearLayout.LayoutParams ) mDishCard.getLayoutParams();
//        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
//        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate( ValueAnimator animator ) {
//                int val = ( Integer ) animator.getAnimatedValue();
//                layoutParams.height = val;
//                mDishCard.setLayoutParams( layoutParams );
//                if(  isNeedCallback && val == 0 ){
//                    layoutParams.leftMargin = cardLeftMargin;
//                    dishCardClick();
//                }
//            }
//        } );
//        valAnimator.setDuration( 150 );
//        valAnimator.start();
//    }


    public void setEntityImage( String uri ) {
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), uri, this.mEntityImage,
                                                               DISH_IMAGE_WIDTH, DISH_IMAGE_HEIGHT);
        mThumpImgUri = uri;
    }


    public void setEntityTitle( String title ) {
        this.mEntityTitle.setText( title );
        TextView textView = findViewById( R.id.expandedTitleTextId );
        textView.setTypeface( AppConstants.B52);
        textView.setText( title );
    }


    public void setEntityDesc( String desc ) {
        this.mEntityDesc.setText( desc );
        TextView textView = findViewById( R.id.expandedTextId );
        textView.setTypeface( AppConstants.ROBOTO_CONDENCED);
        textView.setText( desc );
    }


    public void setEntityPrice( String price ) {
        this.mEntityPrice.setText( price );
        TextView textView = findViewById( R.id.expandedPriceTextId );
        textView.setTypeface( AppConstants.OFFICE, Typeface.BOLD);
        textView.setText( price );
    }

    public void setEntitySize( String value ) {
        this.mEntitySize.setText( value );
        TextView textView = findViewById( R.id.expandedSizeId );
        textView.setTypeface( AppConstants.ROBOTO_CONDENCED);
        textView.setText( value );
    }

    @Override
    public void onClick( View view ) {
        if ( view.getId() == R.id.dishCardCollapsedId ) {
            Animation scale = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(),R.anim.scale_up );
            scale.setAnimationListener( new Animation.AnimationListener() {
                @Override
                public void onAnimationStart( Animation animation ) {

                }

                @Override
                public void onAnimationEnd( Animation animation ) {
                    mDishCardCollapsed.setVisibility( View.GONE );
                }

                @Override
                public void onAnimationRepeat( Animation animation ) {

                }
            } );
            mDishCardExpanded.startAnimation( scale );
        }
        mDishCardExpanded.setVisibility( View.VISIBLE );
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), mThumpImgUri,
                (ImageView) findViewById( R.id.expandedImgId ),
                DISH_IMAGE_WIDTH, DISH_IMAGE_HEIGHT);
//        else {
//            AppUtils.transitionAnimation( mDishCardExpanded, mDishCardCollapsed );
//        }
    }
}
