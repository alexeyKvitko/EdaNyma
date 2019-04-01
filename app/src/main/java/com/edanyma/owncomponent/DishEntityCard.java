package com.edanyma.owncomponent;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;
import com.edanyma.R;
import com.edanyma.model.MenuEntityModel;
import com.edanyma.utils.ConvertUtils;
import com.edanyma.utils.PicassoClient;

public class DishEntityCard extends FrameLayout{

    private static final String TAG = "DishEntityCard";

    private static final int DISH_IMAGE_WIDTH = (int) ConvertUtils.convertDpToPixel( 148 );
    private static final int DISH_IMAGE_HEIGHT = (int) ConvertUtils.convertDpToPixel( 142 );
    private static final int COLLAPSED_HEIGHT = (int) ConvertUtils.convertDpToPixel( 190 );
    private static final int EXPANDED_HEIGHT = (int) ConvertUtils.convertDpToPixel( 250 );


    private ImageView mEntityImage;
    private TextView mEntityTitle;
    private TextView mEntityDesc;
    private TextView mEntityPrice;
    private TextView mEntitySize;
    private FrameLayout mDishCardCollapsed;
    private FrameLayout mDishCardExpanded;
    private FrameLayout mDishContainer;

    private MenuEntityModel mDishEntity;

    private boolean mFirstLoadImg;


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
        mFirstLoadImg = true;
        mDishContainer = findViewById( R.id.dishCardMainContainerId );
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
    }

    private void animateContainer( final View view, final int start, final int end ){
        final FrameLayout.LayoutParams layoutParams = ( FrameLayout.LayoutParams ) view.getLayoutParams();
        ValueAnimator valAnimator = ValueAnimator.ofObject( new IntEvaluator(), start, end );
        valAnimator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate( ValueAnimator animator ) {
                int val = ( Integer ) animator.getAnimatedValue();
                layoutParams.height = val;
                view.setLayoutParams( layoutParams );
            }
        } );
        valAnimator.setDuration( 300 );
        valAnimator.start();
    }

    private void animateDishCard( boolean expand ){
        final View collapsedView = expand ? mDishCardCollapsed : mDishCardExpanded;
        final View expandedVew = expand ?  mDishCardExpanded : mDishCardCollapsed;
        collapsedView.setElevation( 8 );
        expandedVew.setElevation( 10 );
        final int start = expand ? COLLAPSED_HEIGHT : EXPANDED_HEIGHT;
        final int end = expand ? EXPANDED_HEIGHT : COLLAPSED_HEIGHT;

        Animation fadeOut = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_out );
        final Animation fadeIn = AnimationUtils.loadAnimation( EdaNymaApp.getAppContext(), R.anim.fade_in );

        fadeOut.setAnimationListener( new Animation.AnimationListener() {
            @Override
            public void onAnimationStart( Animation animation ) {}

            @Override
            public void onAnimationEnd( Animation animation ) {
                collapsedView.setVisibility( View.GONE );
            }

            @Override
            public void onAnimationRepeat( Animation animation ) {}
        } );
        collapsedView.startAnimation( fadeOut );
        expandedVew.setVisibility( View.VISIBLE );
        expandedVew.startAnimation( fadeIn );
        animateContainer( mDishContainer, start, end );

    }

    public ImageView getEntityImage() {
        return mEntityImage;
    }

    public void setEntityImage( String uri ) {
        PicassoClient.downloadImage( EdaNymaApp.getAppContext(), uri, this.mEntityImage,
                                                               DISH_IMAGE_WIDTH, DISH_IMAGE_HEIGHT);
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


    public void dishCardClick( int dishId ) {
        if( getDishEntityId() != dishId ){
            return;
        }
        if ( View.VISIBLE == mDishCardCollapsed.getVisibility()  ) {
            animateDishCard( true );
        } else {
            animateDishCard( false );
        }

        if ( mFirstLoadImg ){
            ((ImageView) findViewById( R.id.expandedImgId )).setImageDrawable( mEntityImage.getDrawable() );
            mFirstLoadImg = false;
        }
    }

    public void setDishEntity( MenuEntityModel dishEntity ) {
        this.mDishEntity = dishEntity;
        this.setEntityImage( dishEntity.getImageUrl() );
        this.setEntityTitle( dishEntity.getDisplayName() );
        this.setEntityDesc( dishEntity.getDescription() );
        this.setEntityPrice( dishEntity.getPriceOne() );
        this.setEntitySize( dishEntity.getSizeOne() != null ?
                dishEntity.getSizeOne() : dishEntity.getWeightOne() );

    }

    public int getDishEntityId(){
        return Integer.valueOf( this.mDishEntity.getId() );
    }
}
